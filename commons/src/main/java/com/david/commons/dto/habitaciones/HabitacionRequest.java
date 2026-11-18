package com.david.commons.dto.habitaciones;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Datos para registrar o actualizar una habitación")
public record HabitacionRequest(
        @Schema(example = "101")
        @NotNull(message = "El número es requerido")
        @Positive(message = "El número debe ser mayor que cero")
        Integer numero,

        @Schema(example = "DOBLE", allowableValues = {"SENCILLA", "DOBLE", "SUITE"})
        @NotBlank(message = "El tipo es requerido")
        @Pattern(regexp = "SENCILLA|DOBLE|SUITE", message = "El tipo debe ser SENCILLA, DOBLE o SUITE")
        String tipo,

        @Schema(example = "1250.50")
        @NotNull(message = "El precio es requerido")
        @Positive(message = "El precio debe ser mayor que cero")
        Double precio,

        @Schema(example = "2")
        @NotNull(message = "La capacidad es requerida")
        @Min(value = 1, message = "La capacidad mínima es 1")
        Short capacidad
) {}
