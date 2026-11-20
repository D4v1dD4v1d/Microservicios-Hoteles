package com.david.commons.dto.habitaciones;

import com.david.commons.enums.EstadoHabitacion;

import java.math.BigDecimal;

// Contrato de consulta interna Feign; intencionalmente sin estadoRegistro.
public record DatosHabitacion(
        Long numero,
        String tipo,
        BigDecimal precio,
        Short capacidad,
        EstadoHabitacion estado
) {}
