package com.david.reservas.mapper;

import com.david.commons.dto.habitaciones.DatosHabitacion;
import com.david.commons.dto.habitaciones.HabitacionResponse;
import com.david.commons.dto.huespedes.DatosHuesped;
import com.david.commons.dto.huespedes.HuespedResponse;
import com.david.commons.mapper.CommonMapper;
import com.david.reservas.dto.ReservaRequest;
import com.david.reservas.dto.ReservaResponse;
import com.david.reservas.entity.Reserva;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReservaMapper  {

    public Reserva requestAEntidad(ReservaRequest request, HabitacionResponse datosHabitacion) {
        return request == null
                ? null
                : Reserva.crear(
                request.idHuesped(),
                request.idHabitacion(),
                request.fechaEntrada(),
                request.fechaSalida(),
                request.numeroPersonas(),
                datosHabitacion.capacidad(),
                datosHabitacion.precio());
    }

    public ReservaResponse entidadAResponse(Reserva entidad, HuespedResponse huesped, HabitacionResponse habitacion) {
        if (entidad == null) {
            return null;
        }

        if (habitacion != null) {
            log.info("id {}", habitacion.idHabitacion());
        }

        return new ReservaResponse(
                entidad.getId(),
                entidad.getFechaEntrada().toString(),
                entidad.getFechaSalida().toString(),
                entidad.getNumeroPersonas(),
                entidad.getEstadoReserva().getDescripcion(),
                responseADatosHuesped(huesped),
                responseADatosHabitacion(habitacion),
                entidad.getPrecioTotal()
        );
    }

    private DatosHuesped responseADatosHuesped(HuespedResponse huesped) {
        return huesped == null
                ? null
                : new DatosHuesped(
                huesped.nombre(),
                huesped.telefono(),
                huesped.tipoDocumento(),
                huesped.numeroDocumento(),
                huesped.nacionalidad());
    }

    private DatosHabitacion responseADatosHabitacion(HabitacionResponse habitacion) {
        log.info("habitacion {}", habitacion);
        return habitacion == null
                ? null
                : new DatosHabitacion(
                habitacion.numero(),
                habitacion.tipo(),
                habitacion.precio(),
                habitacion.capacidad(),
                habitacion.estado());
    }
}