package com.david.reservas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
@Schema(description = "Datos para actualizar una reserva")
public record ActualizarReservaRequest(
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
        LocalDate fechaSalida
) {}
