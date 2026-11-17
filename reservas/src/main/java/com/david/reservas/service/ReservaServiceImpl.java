package com.david.reservas.service;

import com.david.commons.clients.HuespedClient;
import com.david.commons.dto.huespedes.HuespedResponse;
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
import java.util.Objects;

import static com.david.reservas.enums.EstadoReserva.*;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor

//todo validar habitación obtener datos y cambiar estado
public class ReservaServiceImpl implements ReservaService{
    private final ReservaMapper reservaMapper;
    private final ReservaRepository reservaRepository;

    private final List<EstadoReserva> estadosNoValidos = List.of(CONFIRMADA, EN_CURSO);


    private final HuespedClient huespedClient;

    @Override
    public ReservaResponse actualizar(ReservaRequest request, Long id) {
        return null;
    }

    @Override
    public ReservaResponse actualizarReserva(ActualizarReservaRequest request, Long id) {
        Reserva reserva = obtenerReserva(id);

        log.info("Actualizando reserva con id: {}...", id);

        reserva.actualizar(
                request.fechaEntrada(),
                request.fechaSalida()
        );

        reservaRepository.saveAndFlush(reserva);

        log.info("Reserva actualizada exitosamente");
        return reservaMapper.entidadAResponse(reserva);
    }

    @Override
    public void eliminar(Long id) {
        Reserva reserva = obtenerReserva(id);

        log.info("Eliminando reserva con id: {}...", id);

        reserva.eliminar();

        reservaRepository.saveAndFlush(reserva);

        log.info("Reserva eliminada.");

        //si la reserva estaba en pendiente liberar la habitación
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> lista() {
        log.info("Listando las reservas...");
        return reservaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(reservaMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponse obtenerPorId(Long id) {
        Reserva reserva = obtenerReserva(id);
        return reservaMapper.entidadAResponse(reserva);
    }

    @Override
    public ReservaResponse registrar(ReservaRequest request) {
        log.info("Registrando la reserva...");
        //traer la habitacion
        //Validar habitacion disponible

        validarHuespedNoTieneReservas(request.idHuesped());

        HuespedResponse huesped =  obtenerHuespedResponse(request.idHuesped());

        Reserva reserva = reservaMapper.requestAEntidad(request, huesped);

        reservaRepository.saveAndFlush(reserva);

        //cambiar habitacion a no disponible

        log.info("Reserva registrada");

        return reservaMapper.entidadAResponse(reserva);
    }

    @Override
    public void actualizarEstadoReserva(Long idReserva, Long idEstado) {

        Reserva reserva = obtenerReserva(idReserva);

        log.info("Actualizando estado de la reserva con id: {}", idReserva);

        reserva.actualizarEstadoReserva(
                EstadoReserva.obtenerEstadoCitaPorCodigo(idEstado));

        reservaRepository.saveAndFlush(reserva);

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
    public void habitacionTieneReservasAsignadas(Long idHabitacion) {
        if (reservaRepository.existsByIdHabitacionAndEstadoRegistroAndEstadoReservaIn(
                idHabitacion,
                EstadoRegistro.ACTIVO,
                estadosNoValidos))
            throw new ConflictoException("No se puede modificar o eliminar el Huésped por que tiene reservas en " +
                    "estad " + estadosNoValidos);
    }

    private Reserva obtenerReserva(Long id){
        log.info("Obteniendo reserva con id: {}...", id);

        return reservaRepository.findById(id).
                orElseThrow(()-> new RecursoNoEncontradoException("La reserva no existe."));
    }

    private HuespedResponse obtenerHuespedResponse(Long id){
        log.info("Obteniendo el Huesped response...");

        return huespedClient.obtenerPorId(id);
    }

    private void validarHuespedNoTieneReservas(Long idHuesped){
        log.info("Validando que el huesped no tenga reservas en estado {}",estadosNoValidos);

        if(reservaRepository.existsByIdHuespedAndEstadoRegistroAndEstadoReservaIn(idHuesped,EstadoRegistro.ACTIVO,estadosNoValidos))
            throw new ConflictoException("El húesped ya tiene una reservación confirmada o en curso.");
    }


}
