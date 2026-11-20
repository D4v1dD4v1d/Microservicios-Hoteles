package com.leonardo.habitacion.mapper;

import com.david.commons.enums.TipoHabitacion;
import com.leonardo.habitacion.entity.Habitacion;
import com.david.commons.dto. habitaciones.HabitacionRequest;
import com.david.commons.dto.habitaciones.HabitacionResponse;
import com.david.commons.mapper.CommonMapper;
import org.springframework.stereotype.Component;

@Component
public class HabitacionMapper implements CommonMapper<HabitacionRequest,Habitacion,HabitacionResponse> {

    @Override
    public Habitacion requestAEntidad(HabitacionRequest request) {
        return request == null
                ? null
                : Habitacion.crear(
                request.numero(),
                request.precio(),
                request.capacidad(),
                TipoHabitacion.obtenerTipoHabitacionPorDescripcion(request.tipo())
        );
    }

    @Override
    public HabitacionResponse entidadAResponse(Habitacion entidad) {
        return entidad == null
                ? null
                : new HabitacionResponse(
                entidad.getId(),
                entidad.getNumero(),
                entidad.getTipoHabitacion().getDescripcion(),
                entidad.getPrecio(),
                entidad.getCapacidad(),
                entidad.getEstadoHabitacion(),
                entidad.getEstadoRegistro()
        );
    }
}