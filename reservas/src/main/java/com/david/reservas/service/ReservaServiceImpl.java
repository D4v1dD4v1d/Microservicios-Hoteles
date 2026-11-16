package com.david.reservas.service;

import com.david.commons.enums.EstadoRegistro;
import com.david.commons.exceptions.ConflictoException;
import com.david.commons.exceptions.RecursoNoEncontradoException;
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
public class ReservaServiceImpl implements ReservaService{
    private final ReservaMapper reservaMapper;
    private final ReservaRepository reservaRepository;
    //todo validar y cambiar estado en actualizar, eliminar y agregar
    //una habitacion solo puede tener una reserva en confirmada o en curso (verificar en agregar y en actualizar)
    //no se puede cambiar un huesped ni habitación en actualizar, deben ser los mismos
    @Override
    public ReservaResponse actualizar(ReservaRequest request, Long id) {
        Reserva reserva = obtenerReserva(id);

        validarActualizacion(request,reserva);
        //obtener la habitación
        //obtener el huesped

        //comprobar si el huesped ya tiene reservas asignadas

        log.info("Actualizando reserva con id: {}...", id);

        // los ultimos 2 null los voy a obtener de la habitación
        reserva.actualizar(
                request.idHuesped(),
                request.idHabitacion(),
                request.fechaEntrada(),
                request.fechaSalida(),
                request.numeroPersonas(),
                null,
                null
        );

        reservaRepository.saveAndFlush(reserva);

        log.info("Reserva actualizada exitosamente");
        //TODO enviar el entidad a response que tiene los datos del huesped y los de la habitacion
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
        //TODO ENVIAR EL ENTIDAD A RESPONSE CON LOS DATOS DEL HUESPED Y EL CUARTO
        return reservaMapper.entidadAResponse(obtenerReserva(id));
    }

    @Override
    public ReservaResponse registrar(ReservaRequest request) {
        log.info("Registrando la reserva...");
        //traer la habitacion
        //Validar habitacion disponible
        //traer el huesped
        //validar si el huesped tiene reservas

        Reserva reserva = reservaMapper.requestAEntidad(request);

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

    private Reserva obtenerReserva(Long id){
        log.info("Obteniendo reserva con id: {}...", id);

        return reservaRepository.findById(id).
                orElseThrow(()-> new RecursoNoEncontradoException("La reserva no existe."));
    }

    private void validarActualizacion(ReservaRequest reservaRequest, Reserva reserva){

        // validamos que ni habitacion ni huesped cambien
        if(reservaRequest.idHabitacion()!=reserva.getIdHabitacion())
            throw new ConflictoException("No se puede cambiar la habitación de la reserva.");

        if(reservaRequest.idHuesped()!=reserva.getIdHuesped())
            throw new ConflictoException("No se puede cambiar el responsable de la reserva.");

        //numero de personas no puede cambiar si ya esta confirmada, si cambian las fechas se ignora si se puede
        if(reserva.getEstadoReserva()==CONFIRMADA && reserva.getNumeroPersonas()!= reservaRequest.numeroPersonas())
            throw new ConflictoException("No se puede cambiar el número de ocupantes de la habitacion");

        //Si esta en curso solo puede cambiar la fecha de salida
        log.info("Estado reserva {}, {}<===>{} {}<===>{}",reserva.getEstadoReserva(),reserva.getNumeroPersonas(),reservaRequest.numeroPersonas(),reserva.getFechaEntrada(),reservaRequest.fechaEntrada());
        if(reserva.getEstadoReserva()==EN_CURSO &&
                ((!Objects.equals(reserva.getNumeroPersonas(), reservaRequest.numeroPersonas()) ||
                        !Objects.equals(reserva.getFechaEntrada(), reservaRequest.fechaEntrada()))))
            throw new ConflictoException("No se puede cambiar el número de ocupantes de la habitacion ni la fecha de entrada" +
                    " si el huésped ya esta ocupando la habitación");

        //ya no se puede actualizar
        if (reserva.getEstadoReserva()==CANCELADA||reserva.getEstadoReserva()==FINALIZADA)
            throw new ConflictoException("No se puede actualizar la reserva si esta cancelada o finalizada");
    }

}
