package com.david.reservas.enums;

import com.david.commons.exceptions.DatoInvalidoException;
import com.david.commons.utils.ValoresNumericosUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum EstadoReserva {
    CONFIRMADA(1L,"Confirmada por el huésped", true, false) {
        @Override
        public Set<EstadoReserva> puedeCambiar() {
            return Set.of(EN_CURSO,CANCELADA);
        }
    },
    EN_CURSO(2L,"El huésped llegó al hotel", true, false) {
        @Override
        public Set<EstadoReserva> puedeCambiar() {
            return Set.of(FINALIZADA);
        }
    },
    FINALIZADA(3L,"Reservación finalizada", false, true) {
        @Override
        public Set<EstadoReserva> puedeCambiar() {
            return Set.of();
        }
    },
    CANCELADA(4L,"Reservación cancelada", false, true) {
        @Override
        public Set<EstadoReserva> puedeCambiar() {
            return Set.of();
        }
    };

    private final Long codigo;

    private final String descripcion;

    private final boolean actualizable;

    private final boolean eliminable;

    public abstract Set<EstadoReserva> puedeCambiar();

    public boolean puedeCambiarA(EstadoReserva estadoReserva){
        return puedeCambiar().contains(estadoReserva);
    }

    public static EstadoReserva obtenerEstadoCitaPorCodigo(Long codigo){
        ValoresNumericosUtils.validadLongPositivo(codigo,"El código es requerido y debe ser positivo");

        for (EstadoReserva e: values()){
            if(Objects.equals(e.codigo, codigo))
                return e;
        }

        throw new DatoInvalidoException("Código de cita no válido: "+ codigo);
    }
}
