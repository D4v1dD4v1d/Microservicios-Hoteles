package com.david.reservas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "Datos para registrar una reserva")
public record ReservaRequest(
        @Schema(description = "Identificador del huésped", example = "1")
        @NotNull(message = "El id del huésped es requerido")
        @Positive(message = "El id del huésped debe ser positivo")
        Long idHuesped,

        @Schema(description = "Identificador de la habitación", example = "1")
        @NotNull(message = "El id de la habitación es requerido")
        @Positive(message = "El id de la habitación debe ser positivo")
        Long idHabitacion,

        @Schema(
                description = "Fecha de entrada programada para la reserva.",
                example = "2026-09-17",
                type = "string",
                format = "date")
        @NotNull(message = "La fecha de entrada es requerido")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate fechaEntrada,

        @Schema(
                description = "Fecha de salida programada para la reserva.",
                example = "2026-09-25",
                type = "string",
                format = "date")
        @NotNull(message = "La fecha de salida es requerido")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate fechaSalida,

        @Schema(
                description = "Cantidad de personas que ingresarán a la habitación",
                example = "5"
        )
        @NotNull(message = "La cantidad de personas es requerida")
        @Positive(message = "El número de personas debe ser un entero positivo")
        Short numeroPersonas
) {}
