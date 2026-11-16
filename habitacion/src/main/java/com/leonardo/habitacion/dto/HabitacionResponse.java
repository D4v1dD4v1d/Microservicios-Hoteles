
package com.leonardo.habitacion.dto;

import com.leonardo.habitacion.enums.Estado;
import com.leonardo.habitacion.enums.EstadoRegistro;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información de una habitación registrada")
public record HabitacionResponse(

        @Schema(description = "Identificador único de la habitación", example = "1")
        Long idHabitacion,

        @Schema(description = "Número de la habitación", example = "101")
        Integer numero,

        @Schema(description = "Tipo de habitación", example = "DOBLE")
        String tipo,

        @Schema(description = "Precio por noche", example = "1250.50")
        Double precio,

        @Schema(description = "Capacidad máxima de personas", example = "2")
        Short capacidad,

        @Schema(description = "Estado actual de la habitación", example = "DISPONIBLE")
        Estado estado,

        @Schema(description = "Estado de registro de la habitación", example = "ACTIVO")
        EstadoRegistro estadoRegistro

) {
}
