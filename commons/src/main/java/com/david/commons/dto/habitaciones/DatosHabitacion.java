package com.david.commons.dto.habitaciones;

import com.david.commons.enums.EstadoHabitacion;

// Contrato de consulta interna Feign; intencionalmente sin estadoRegistro.
public record DatosHabitacion(
        Long idHabitacion,
        Integer numero,
        String tipo,
        Double precio,
        Short capacidad,
        EstadoHabitacion estado
) {}
