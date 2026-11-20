package com.david.reservas.dto;

import com.david.commons.dto.habitaciones.DatosHabitacion;
import com.david.commons.dto.huespedes.DatosHuesped;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;


@Schema(description = "Datos de una reserva")
public record ReservaResponse(
        @Schema(description = "Id de la reserva")
        Long id,
        @Schema(description = "Fecha de entrada de los huéspedes a la habitación")
        String fechaEntrada,
        @Schema(description = "Fecha de salida de los huéspedes de la habitación")
        String FechaSalida,
        @Schema(description = "Número de personas que ingresarán al hotel")
        Short numeroPesronas,
        @Schema(description = "Estado en el que se encuentra la reserva")
        String estadoReserva,
        @Schema(description = "Dato del huéspede")
        DatosHuesped datosHuesped,
        @Schema(description = "Datos de la habitación")
        DatosHabitacion datosHabitacion,
        @Schema(description = "Monto total de la reserva")
        BigDecimal precioTotal
) {}
