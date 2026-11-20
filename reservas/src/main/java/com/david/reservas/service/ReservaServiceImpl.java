package com.david.reservas.service;

import com.david.commons.clients.HabitacionClient;
import com.david.commons.clients.HuespedClient;
import com.david.commons.dto.habitaciones.HabitacionResponse;
import com.david.commons.dto.huespedes.HuespedResponse;
import com.david.commons.enums.EstadoHabitacion;
import com.david.commons.enums.EstadoRegistro;
import com.david.commons.exceptions.ConflictoException;
import com.david.commons.exceptions.RecursoNoEncontradoException;
import com.david.reservas.dto.ActualizarReservaRequest;
import com.david.reservas.dto.ReservaRequest;
import com.david.reservas.dto.ReservaResponse;
import com.david.reservas.entity.Reserva;
import com.david.reservas.enums.EstadoReserva;
import com.david.reservas.mapper.ReservaMapper;
import com.david.reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Objects;

import static com.david.reservas.enums.EstadoReserva.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor

public class ReservaServiceImpl implements ReservaService{
    private final ReservaMapper reservaMapper;
    private final ReservaRepository reservaRepository;

    private final List<EstadoReserva> estadosNoValidos = List.of(CONFIRMADA, EN_CURSO);


    private final HuespedClient huespedClient;
    private final HabitacionClient habitacionClient;
    @Override
    public ReservaResponse actualizar(ReservaRequest request, Long id) {
        Reserva reserva = obtenerReserva(id);
        if (!Objects.equals(reserva.getIdHuesped(), request.idHuesped())
                || !Objects.equals(reserva.getIdHabitacion(), request.idHabitacion())
                || !Objects.equals(reserva.getNumeroPersonas(), request.numeroPersonas()))
            throw new ConflictoException("No se puede cambiar el huésped, la habitación o el número de personas");
        return actualizarReserva(new ActualizarReservaRequest(request.fechaEntrada(), request.fechaSalida()), id);
    }

    @Override
    public ReservaResponse actualizarReserva(ActualizarReservaRequest request, Long id) {
        Reserva reserva = obtenerReserva(id);

        HabitacionResponse habitacion = obtenerHabitacionResponse(reserva.getIdHabitacion());

        HuespedResponse huesped =  obtenerHuespedResponse(reserva.getIdHuesped());

        log.info("Actualizando reserva con id: {}...", id);

        reserva.actualizar(
                request.fechaEntrada(),
                request.fechaSalida()
        );

        reservaRepository.saveAndFlush(reserva);

        log.info("Reserva actualizada exitosamente");
        return reservaMapper.entidadAResponse(reserva,huesped,habitacion);
    }

    @Override
    public void eliminar(Long id) {
        Reserva reserva = obtenerReserva(id);

        log.info("Eliminando reserva con id: {}...", id);

        reserva.eliminar();

        reservaRepository.saveAndFlush(reserva);

        log.info("Reserva eliminada.");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> lista() {
        log.info("Listando las reservas...");

        return reservaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map((reserva)->reservaMapper.entidadAResponse(
                        reserva,
                        obtenerHuespedResponse(reserva.getIdHuesped()),
                        obtenerHabitacionResponse(reserva.getIdHabitacion())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponse obtenerPorId(Long id) {
        Reserva reserva = obtenerReserva(id);

        HabitacionResponse habitacion = obtenerHabitacionResponse(reserva.getIdHabitacion());

        HuespedResponse huesped =  obtenerHuespedResponse(reserva.getIdHuesped());

        return reservaMapper.entidadAResponse(reserva,huesped,habitacion);
    }

    @Override
    public ReservaResponse registrar(ReservaRequest request) {
        log.info("Registrando la reserva...");

        HabitacionResponse habitacion = obtenerHabitacionResponse(request.idHabitacion());

        habitacionDisponible(habitacion);

        validarHuespedNoTieneReservas(request.idHuesped());

        HuespedResponse huesped =  obtenerHuespedResponse(request.idHuesped());

        Reserva reserva = reservaMapper.requestAEntidad(request, habitacion);

        reservaRepository.saveAndFlush(reserva);

        cambiarEstadoHabitacion(habitacion.idHabitacion(),EstadoHabitacion.OCUPADA);

        log.info("Reserva registrada");

        return reservaMapper.entidadAResponse(reserva,huesped,habitacion);

    }

    @Override
    public void actualizarEstadoReserva(Long idReserva, Long idEstado) {

        Reserva reserva = obtenerReserva(idReserva);

        log.info("Actualizando estado de la reserva con id: {}", idReserva);

        reserva.actualizarEstadoReserva(
                EstadoReserva.obtenerEstadoCitaPorCodigo(idEstado));

        EstadoHabitacion nuevoEstado = null;
        if (reserva.getEstadoReserva() == CANCELADA)
            nuevoEstado = EstadoHabitacion.DISPONIBLE;
        else if (reserva.getEstadoReserva() == FINALIZADA)
            nuevoEstado = EstadoHabitacion.LIMPIEZA;

        if (nuevoEstado != null)
            cambiarEstadoHabitacion(reserva.getIdHabitacion(), nuevoEstado);
        try {
            reservaRepository.saveAndFlush(reserva);
        } catch (RuntimeException e) {
            if (nuevoEstado != null)
                cambiarEstadoHabitacion(reserva.getIdHabitacion(), EstadoHabitacion.OCUPADA);
            throw e;
        }
        log.info("Estado de la reserva {} actualizado", reserva);
    }

    @Override
    public void huespedTieneReservasAsignadas(Long idHuesped){
        if (reservaRepository.existsByIdHuespedAndEstadoRegistroAndEstadoReservaIn(
                idHuesped,
                EstadoRegistro.ACTIVO,
                estadosNoValidos))
            throw new ConflictoException("No se puede modificar o eliminar el Huésped por que tiene reservas en " +
                    "estad " + estadosNoValidos);
    }

    @Override
    @Transactional
    public void validarEliminacionHuesped(Long idHuesped) {
        if (reservaRepository.existsByIdHuespedAndEstadoRegistroAndEstadoReservaIn(
                idHuesped, EstadoRegistro.ACTIVO, estadosNoValidos)) {
            throw new ConflictoException(
                    "No se puede eliminar el huesped: tiene reservas CONFIRMADAS o EN_CURSO");
        }
    }

    @Override
    public void habitacionTieneReservasAsignadas(Long idHabitacion) {
        if (reservaRepository.existsByIdHabitacionAndEstadoRegistroAndEstadoReservaIn(
                idHabitacion,
                EstadoRegistro.ACTIVO,
                estadosNoValidos))
            throw new ConflictoException("La habitación ya tiene una reserva vigente: " + estadosNoValidos);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer personasReservaVigente(Long idHabitacion) {
        List<Reserva> reservas = reservaRepository.findByIdHabitacionAndEstadoRegistroAndEstadoReservaIn(
                idHabitacion, EstadoRegistro.ACTIVO, estadosNoValidos);
        if (reservas.isEmpty()) return 0;
        return reservas.get(0).getNumeroPersonas().intValue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> historico() {
        return reservaRepository.findAll().stream()
                .map(reserva -> reservaMapper.entidadAResponse(reserva,
                        obtenerHuespedResponse(reserva.getIdHuesped()),
                        obtenerHabitacionResponse(reserva.getIdHabitacion())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<ReservaResponse>> reportePorEstado() {
        List<ReservaResponse> reservas = historico();
        Map<String, List<ReservaResponse>> reporte = new LinkedHashMap<>();
        for (EstadoReserva estado : EstadoReserva.values()) {
            reporte.put(estado.name(), reservas.stream()
                    .filter(reserva -> estado.getDescripcion().equals(reserva.estadoReserva()))
                    .toList());
        }
        return reporte;
    }

    private Reserva obtenerReserva(Long id){
        log.info("Obteniendo reserva con id: {}...", id);

        return reservaRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).
                orElseThrow(()-> new RecursoNoEncontradoException("La reserva no existe."));
    }

    private HuespedResponse obtenerHuespedResponse(Long id){
        log.info("Obteniendo el Huesped response...");

        return huespedClient.obtenerPorIdSinEstado(id);
    }

    private HabitacionResponse obtenerHabitacionResponse(Long id){
        log.info("Obteniendo la habitación response...");
        return habitacionClient.obtenerPorIdSinEstado(id);
    }

    private void habitacionDisponible(HabitacionResponse habitacion){

        if(!habitacion.estado().equals(EstadoHabitacion.DISPONIBLE))
            throw new ConflictoException("La habitación no esta disponible por el momento.");
    }

    private void cambiarEstadoHabitacion(Long id, EstadoHabitacion estado){
        log.info("Cambiando estado de la habitación");

        habitacionClient.cambiarEstado(id,estado.getId());
    }
    private void validarHuespedNoTieneReservas(Long idHuesped){
        log.info("Validando que el huesped no tenga reservas en estado {}",estadosNoValidos);

        if(reservaRepository.existsByIdHuespedAndEstadoRegistroAndEstadoReservaIn(idHuesped,EstadoRegistro.ACTIVO,estadosNoValidos))
            throw new ConflictoException("El húesped ya tiene una reservación confirmada o en curso.");
    }


}
