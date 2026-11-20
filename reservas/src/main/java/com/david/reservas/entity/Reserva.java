package com.david.reservas.entity;

import com.david.commons.exceptions.ConflictoException;
import com.david.commons.exceptions.DatoInvalidoException;
import com.david.commons.utils.ValoresNumericosUtils;
import com.david.reservas.enums.EstadoReserva;
import jakarta.persistence.*;
import com.david.commons.enums.EstadoRegistro;
import com.david.commons.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESERVA")
    private Long id;

    @Column(name = "ID_HUESPED", nullable = false)
    private Long idHuesped;

    @Column(name = "ID_HABITACION", nullable = false)
    private Long idHabitacion;

    @Column(name = "FECHA_ENTRADA", nullable = false)
    private LocalDate fechaEntrada;

    @Column(name = "FECHA_SALIDA", nullable = false)
    private LocalDate fechaSalida;

    @Column(name = "NUMERO_PERSONAS", nullable = false)
    private Short numeroPersonas;

    @Column(name = "PRECIO_NOCHE",columnDefinition = "NUMBER(10,2)",  nullable = false)
    private BigDecimal precioNoche;

    @Column(name = "PRECIO_TOTAL",columnDefinition = "NUMBER(10,2)",  nullable = false)
    private BigDecimal precioTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false)
    private EstadoReserva estadoReserva;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false)
    private EstadoRegistro estadoRegistro;

    private static void validarDatos(
            Long idHuesped,
            Long idHabitacion,
            LocalDate fechaEntrada,
            LocalDate fechaSalida,
            Short numeroPersonas,
            Short capacidadPersonasHabitacion
    ){
        ValoresNumericosUtils.validadLongPositivo(idHuesped, "El id del huesped debe ser un entero positivo");
        ValoresNumericosUtils.validadLongPositivo(idHabitacion, "El id de la habitación debe ser un entero positivo");
        validarFechas(fechaEntrada,fechaSalida);
        ValoresNumericosUtils.validadRangoShort(numeroPersonas,(short) 1, capacidadPersonasHabitacion,
                "El número de huespedes para la reserva debe ser entre 1 y la capacidad de personas de la habitación:"+capacidadPersonasHabitacion);
    }

    private static void  validarFechas(LocalDate fechaEntrada, LocalDate fechaSalida){

        if (fechaEntrada == null || fechaSalida == null || fechaSalida.isBefore(fechaEntrada) || fechaEntrada.equals(fechaSalida))
            throw new DatoInvalidoException("Las fechas de entrada y salida son requeridas y la fecha de salida debe ser posterior" +
                    " a la fecha de entrada.");
    }


    private void validarNoEliminado(){

        if (this.estadoRegistro == EstadoRegistro.ELIMINADO)
            throw new ConflictoException("La reserva ya está eliminada");
    }

    private void validarEliminacionPermitida(){

        validarNoEliminado();

        if (!estadoReserva.isEliminable())
            throw new ConflictoException("La reserva con estado "+estadoRegistro +" no puede eliminarse.");
    }

    private void validarActualizacionPermitida(){

        validarNoEliminado();

        if (!estadoReserva.isActualizable())
            throw new ConflictoException("La reserva con estado "+estadoReserva +" no puede actualizarse.");
    }

    private static BigDecimal calcularPrecioTotal(LocalDate fechaEntrada,LocalDate fechaSalida, BigDecimal precioNoche){

        if(precioNoche== null)
            return BigDecimal.ZERO;

        Double noches = (double)ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);

        return precioNoche.multiply(BigDecimal.valueOf(noches));

    }

    public void eliminar(){

        validarEliminacionPermitida();

        this.estadoRegistro = EstadoRegistro.ELIMINADO;

    }
    public void actualizarEstadoReserva(EstadoReserva nuevoEstado){

        validarActualizacionPermitida();

        if (nuevoEstado == null)
            throw new DatoInvalidoException("El nuevo estado de la cita es requerido");

        if (!estadoReserva.puedeCambiarA(nuevoEstado))
            throw new ConflictoException("La cita con estado "+ estadoReserva+" solo puede " +
                    "cambiar a: "+ estadoReserva.puedeCambiar());

        this.estadoReserva = nuevoEstado;
    }

    public static Reserva crear(Long idHuesped,
                                Long idHabitacion,
                                LocalDate fechaEntrada,
                                LocalDate fechaSalida,
                                Short numeroPersonas,
                                Short capacidadPersonasHabitacion,
                                BigDecimal precioNoche){

        validarDatos(idHuesped,idHabitacion,fechaEntrada,fechaSalida,numeroPersonas,capacidadPersonasHabitacion);

        return Reserva.builder()
                .idHabitacion(idHabitacion)
                .idHuesped(idHuesped)
                .fechaEntrada(fechaEntrada)
                .fechaSalida(fechaSalida)
                .numeroPersonas(numeroPersonas)
                .precioNoche(precioNoche)
                .precioTotal(calcularPrecioTotal(fechaEntrada,fechaSalida,precioNoche))
                .estadoReserva(EstadoReserva.CONFIRMADA)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();

    }

    public void actualizar(LocalDate fechaEntrada, LocalDate fechaSalida){

        validarFechas(fechaEntrada,fechaSalida);

        if(this.getEstadoReserva().equals(EstadoReserva.EN_CURSO)&& !fechaEntrada.equals(this.fechaEntrada))
            throw new ConflictoException("No se puede actualizar la fecha de entrada debido a que el huesped ya esta en la habitación");


        this.fechaSalida=fechaSalida;
        this.fechaEntrada=fechaEntrada;
        this.precioTotal=calcularPrecioTotal(fechaEntrada,fechaSalida,precioNoche);

    }


}
