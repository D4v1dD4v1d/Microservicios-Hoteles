package com.david.commons.dto.habitaciones;

// Datos de reservas vigentes (CONFIRMADA/EN_CURSO) para validar la habitación.
public record RestriccionesReservaHabitacion(
        boolean tieneReservasVigentes,
        Short maximoPersonasVigentes
) {}
