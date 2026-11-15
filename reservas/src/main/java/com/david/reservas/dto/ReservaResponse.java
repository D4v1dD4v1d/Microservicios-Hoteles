package com.david.reservas.dto;

import com.david.commons.dto.habitaciones.DatosHabitacion;
import com.david.commons.dto.huespedes.DatosHuesped;

public record ReservaResponse(
        String fechaEntrada,
        String FechaSalida,
        Short numeroPesronas,
        DatosHuesped datosHuesped,
        DatosHabitacion datosHabitacion,
        Double precioTotal
) {}
