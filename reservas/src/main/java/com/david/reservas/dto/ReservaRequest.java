package com.david.reservas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservaRequest(
        @Schema(description = "Identificador del paciente", example = "1")
        @NotNull(message = "El id de paciente es requerido")
        @Positive(message = "El id del paciente debe ser positivo")
        Long idHuesped,

        @Schema(description = "Identificador del médico", example = "1")
        @NotNull(message = "El id de médico es requerido")
        @Positive(message = "El id del médico debe ser positivo")
        Long idHabitacion,

        @Schema(
                description = "Fecha de entrada programada para la reserva. Debe ser una fecha presente o futura",
                example = "2026/09/27",
                type = "string",
                format = "date")
        @NotNull(message = "La fecha de entrada es requerido")
        @FutureOrPresent(message = "La fecha de entrada debe ser presente o futura")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
        LocalDate fechaEntrada,

        @Schema(
                description = "Fecha de salida programada para la reserva. Debe ser una fecha futura",
                example = "2026/09/25",
                type = "string",
                format = "date")
        @NotNull(message = "La fecha de salida es requerido")
        @Future(message = "La fecha de salida debe ser futura")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
        LocalDate fechaSalida,

        @Schema(
                description = "Cantidad de personas que ingresarán a la habitación",
                example = "5"
        )
        @NotNull(message = "La cantidad de personas es requerida")
        @Positive(message = "El número de personas debe ser un entero positivo")
        Short numeroPersonas
) {}
