
package com.david.commons.dto.habitaciones;

import com.david.commons.enums.EstadoHabitacion;
import com.david.commons.enums.EstadoRegistro;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Información de una habitación registrada")
public record HabitacionResponse(

        @Schema(description = "Identificador único de la habitación", example = "1")
        Long idHabitacion,

        @Schema(description = "Número de la habitación", example = "101")
        Long numero,

        @Schema(description = "Tipo de habitación", example = "DOBLE")
        String tipo,

        @Schema(description = "Precio por noche", example = "1250.50")
        BigDecimal precio,

        @Schema(description = "Capacidad máxima de personas", example = "2")
        Short capacidad,

        @Schema(description = "Estado actual de la habitación", example = "DISPONIBLE")
        EstadoHabitacion estado,

        @Schema(description = "Estado de registro de la habitación", example = "ACTIVO")
        EstadoRegistro estadoRegistro

) {
}
