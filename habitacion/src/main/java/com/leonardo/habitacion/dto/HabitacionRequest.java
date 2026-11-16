
package com.leonardo.habitacion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos necesarios para registrar o actualizar una habitación")
public record HabitacionRequest(

        @Schema(description = "Número de la habitación", example = "101")
        @NotNull(message = "El número de habitación es requerido")
        @Positive(message = "El número de habitación debe ser mayor que cero")
        Integer numero,

        @Schema(
                description = "Tipo de habitación",
                example = "DOBLE",
                allowableValues = {"SENCILLA", "DOBLE", "SUITE"}
        )
        @NotBlank(message = "El tipo de habitación es requerido")
        @Size(max = 50, message = "El tipo de habitación no debe superar los 50 caracteres")
        String tipo,

        @Schema(description = "Precio por noche de la habitación", example = "1250.50")
        @NotNull(message = "El precio de la habitación es requerido")
        @Positive(message = "El precio debe ser mayor que cero")
        Double precio,

        @Schema(description = "Capacidad máxima de personas", example = "2")
        @NotNull(message = "La capacidad de la habitación es requerida")
        @Positive(message = "La capacidad debe ser mayor que cero")
        Short capacidad


) {
}
