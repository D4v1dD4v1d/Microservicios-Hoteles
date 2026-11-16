package com.david.commons.enums;

import com.david.commons.exceptions.DatoInvalidoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

public enum TipoDocumento {
    INE,
    LICENCIA,
    PASAPORTE;
}
