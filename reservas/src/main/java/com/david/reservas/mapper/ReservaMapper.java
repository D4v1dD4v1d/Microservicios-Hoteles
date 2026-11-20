package com.david.reservas.mapper;

import com.david.commons.dto.habitaciones.DatosHabitacion;
import com.david.commons.dto.habitaciones.HabitacionResponse;
import com.david.commons.dto.huespedes.DatosHuesped;
import com.david.commons.dto.huespedes.HuespedResponse;
import com.david.commons.mapper.CommonMapper;
import com.david.reservas.dto.ReservaRequest;
import com.david.reservas.dto.ReservaResponse;
import com.david.reservas.entity.Reserva;
import org.springframework.stereotype.Component;

//TODO OBTENER DATOS HUESPEDES Y DATOS HABITACION DE COMMONS
@Component
public class ReservaMapper implements CommonMapper<ReservaRequest,Reserva,ReservaResponse>{

    @Override
    public Reserva requestAEntidad(ReservaRequest request) {
        return request == null
                ? null
                : Reserva.crear(
                request.idHuesped(),
                request.idHabitacion(),
                request.fechaEntrada(),
                request.fechaSalida(),
                request.numeroPersonas(),
                null,
                null);
    }

    public Reserva requestAEntidad(ReservaRequest request, HuespedResponse huespedResponse) {
        return request == null
                ? null
                : Reserva.crear(
                request.idHuesped(),
                request.idHabitacion(),
                request.fechaEntrada(),
                request.fechaSalida(),
                request.numeroPersonas(),
                null,
                null);
    }


    @Override
    public ReservaResponse entidadAResponse(Reserva entidad) {
        return entidad == null
                ? null
                : new ReservaResponse(
                entidad.getId(),
                entidad.getFechaEntrada().toString(),
                entidad.getFechaSalida().toString(),
                entidad.getNumeroPersonas(),
                null,
                null,
                entidad.getPrecioTotal());
    }



    public ReservaResponse entidadAResponse(Reserva entidad, HuespedResponse huesped, HabitacionResponse habitacion) {
        ReservaResponse reservaResponse = entidadAResponse(entidad);

       //reservaResponse.datosHabitacion().equals(responseADatosHabitacion(habitacion));

        reservaResponse.datosHuesped().equals(responseADatosHuesped(huesped));

        return reservaResponse;

    }

    private DatosHuesped responseADatosHuesped(HuespedResponse huesped){
        return huesped == null
                ? null
                : new DatosHuesped(
                huesped.nombre(),
                huesped.telefono(),
                huesped.tipoDocumento(),
                huesped.numeroDocumento(),
                huesped.nacionalidad(),
                huesped.estadoRegistro());
    }
/*
    private DatosHabitacion responseADatosHabitacion(HabitacionResponse habitacion){
        return habitacion == null
                ? null
                : new DatosHabitacion(
                habitacion.numero(),
                habitacion.tipo(),
                habitacion.precio(),
                habitacion.precio(),
                habitacion.capacidad(),
                habitacion.estado()
                );
    }
    */

}
