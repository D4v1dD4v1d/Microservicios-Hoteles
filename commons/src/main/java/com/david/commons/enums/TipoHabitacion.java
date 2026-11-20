package com.david.commons.enums;

import com.david.commons.exceptions.DatoInvalidoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public enum TipoHabitacion {
    SENCILLA(1l,"Sencilla"),
    DOBLE(2l,"Doble"),
    SUITE(3l,"Suite");

    private final Long codigo;
    private final String descripcion;

    public static TipoHabitacion obtenerTipoHabitacionPorCodigo(Long codigo) {
        for (TipoHabitacion e : values()) {
            if (Objects.equals(e.codigo, codigo)) {
                return e;
            }
        }
        throw new DatoInvalidoException("Código de especialidad no válido: " + codigo);
    }

    public static TipoHabitacion obtenerTipoHabitacionPorDescripcion(String descripcion) {
        for (TipoHabitacion e : values()) {
            if (Objects.equals(e.descripcion, descripcion)) {
                return e;
            }
        }
        throw new DatoInvalidoException("Descripción de especialidad no válido: " + descripcion+" debe ser['Suite','Sencilla','Doble']");
    }
}
