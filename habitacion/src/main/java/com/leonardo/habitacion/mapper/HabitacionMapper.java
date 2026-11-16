
package com.leonardo.habitacion.mapper;

import com.leonardo.habitacion.dto.HabitacionRequest;
import com.leonardo.habitacion.dto.HabitacionResponse;
import com.leonardo.habitacion.entity.Habitacion;
import com.leonardo.habitacion.enums.Estado;
import com.leonardo.habitacion.enums.EstadoRegistro;
import org.springframework.stereotype.Component;

@Component
public class HabitacionMapper {

    // Convertir Request a entidad para registrar
    public Habitacion requestAEntidad(HabitacionRequest request) {
        return Habitacion.builder()
                .numero(request.numero())
                .tipo(request.tipo())
                .precio(request.precio())
                .capacidad(request.capacidad())
                .estado(Estado.DISPONIBLE)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    // Convertir entidad a Response
    public HabitacionResponse entidadAResponse(Habitacion habitacion) {
        return new HabitacionResponse(
                habitacion.getIdHabitacion(),
                habitacion.getNumero(),
                habitacion.getTipo(),
                habitacion.getPrecio(),
                habitacion.getCapacidad(),
                habitacion.getEstado(),
                habitacion.getEstadoRegistro()
        );
    }

    // Actualizar únicamente los datos modificables
    public void actualizarEntidad(HabitacionRequest request, Habitacion habitacion) {
        habitacion.setNumero(request.numero());
        habitacion.setTipo(request.tipo());
        habitacion.setPrecio(request.precio());
        habitacion.setCapacidad(request.capacidad());
    }
}
