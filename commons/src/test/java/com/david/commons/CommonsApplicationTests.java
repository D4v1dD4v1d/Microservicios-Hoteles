package com.david.commons;

import com.david.commons.enums.EstadoHabitacion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CommonsApplicationTests {
    @Test
    void estadosHabitacionPorId() {
        assertEquals(EstadoHabitacion.DISPONIBLE, EstadoHabitacion.fromId(1L));
        assertEquals(EstadoHabitacion.OCUPADA, EstadoHabitacion.fromId(2L));
        assertEquals(EstadoHabitacion.LIMPIEZA, EstadoHabitacion.fromId(3L));
        assertEquals(EstadoHabitacion.MANTENIMIENTO, EstadoHabitacion.fromId(4L));
        assertNull(EstadoHabitacion.fromId(0L));
    }
}
