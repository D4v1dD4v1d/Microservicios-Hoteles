package com.david.commons.enums;


import com.david.commons.exceptions.DatoInvalidoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
public enum TipoDocumento {
    INE(1L, "INE del huesped"),
    LICENCIA(2L, "Licencia de conducir del huesped"),
    PASAPORTE(3L, "Pasaporte del huesped");

    private final Long codigo;

    private final String descripcion;

    public static TipoDocumento obtenerTipoDocumentoPorCodigo(Long codigo) {
        return Arrays.stream(values())
                .filter(d -> Objects.equals(d.codigo, codigo))
                .findFirst()
                .orElseThrow(() -> new DatoInvalidoException("Codigo de disponibilidad no valido: " + codigo));
    }

}