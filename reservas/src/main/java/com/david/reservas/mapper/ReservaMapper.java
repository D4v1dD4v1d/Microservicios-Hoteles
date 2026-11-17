package com.david.reservas.mapper;

import com.david.commons.mapper.CommonMapper;
import com.david.reservas.dto.ReservaRequest;
import com.david.reservas.dto.ReservaResponse;
import com.david.reservas.entity.Reserva;
import org.springframework.stereotype.Component;

//TODO OBTENER DATOS HUESPEDES Y DATOS HABITACION DE COMMONS
@Component
public class ReservaMapper implements CommonMapper<ReservaRequest, Reserva, ReservaResponse> {
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

    //TODO REALIZAR EL MAPPER PERO CON LOS DATOS DEL HUESPED Y EL CAURTO

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
}
