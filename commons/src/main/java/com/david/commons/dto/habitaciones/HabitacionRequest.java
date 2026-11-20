package com.david.commons.dto.habitaciones;

import com.david.commons.enums.TipoHabitacion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Datos para registrar o actualizar una habitación")
public record HabitacionRequest(
        @Schema(example = "101")
        @NotNull(message = "El número es requerido")
        @Positive(message = "El número debe ser mayor que cero")
        Long numero,

        @Schema(example = "DOBLE", allowableValues = {"Sencilla", "Doble", "Suite"})
        @NotBlank(message = "El tipo es requerido")
        @Pattern(regexp = "Sencilla|Doble|Suite", message = "El tipo debe ser Sencilla, Doble o Suite")
        String tipo,

        @Schema(example = "1250.50")
        @NotNull(message = "El precio es requerido")
        @Positive(message = "El precio debe ser mayor que cero")
        BigDecimal precio,

        @Schema(example = "2")
        @NotNull(message = "La capacidad es requerida")
        @Min(value = 1, message = "La capacidad mínima es 1")
        Short capacidad
) {}
