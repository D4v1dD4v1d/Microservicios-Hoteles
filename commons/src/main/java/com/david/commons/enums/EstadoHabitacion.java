package com.david.commons.enums;

public enum EstadoHabitacion {
    DISPONIBLE(1l),
    OCUPADA(2l),
    LIMPIEZA(3l),
    MANTENIMIENTO(4l);

    private final Long id;

    EstadoHabitacion(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static EstadoHabitacion fromId(Long id) {
        for (EstadoHabitacion estado : values()) {
            if (estado.id.equals(id)) {
                return estado;
            }
        }
        return null;
    }
}