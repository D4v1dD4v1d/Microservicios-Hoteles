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
    private Double precioNoche;

    @Column(name = "PRECIO_TOTAL",columnDefinition = "NUMBER(10,2)",  nullable = false)
    private Double precioTotal;

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
        if (fechaEntrada == null || fechaSalida == null || fechaSalida.isBefore(fechaEntrada))
            throw new DatoInvalidoException("Las fechas de entrada y salida son requeridas y la fecha de salida debe ser posterior" +
                    " a la fecha de entrada.");
    }
    private void validarNoEliminado(){
        if (this.estadoRegistro == EstadoRegistro.ELIMINADO)
            throw new ConflictoException("La cita ya está eliminada");
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

    private static Double calcularPrecioTotal(LocalDate fechaEntrada,LocalDate fechaSalida, Double precioNoche){

        if(precioNoche== null)
            return 0.0;

        Long noches = ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);

        return noches * precioNoche;

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
                                Double precioNoche){
        //solo para pruebas
        precioNoche=10.0;
        capacidadPersonasHabitacion = 4;
        numeroPersonas = 4;

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

    public void actualizar(Long idHuesped,
                           Long idHabitacion,
                           LocalDate fechaEntrada,
                           LocalDate fechaSalida,
                           Short numeroPersonas,
                           Short capacidadPersonasHabitacion,
                           Double precioNoche){
        //solo para pruebas
        precioNoche=10.0;
        capacidadPersonasHabitacion = 4;
        numeroPersonas = 4;

        validarActualizacionPermitida();

        validarDatos(idHuesped,idHabitacion,fechaEntrada,fechaSalida,numeroPersonas,capacidadPersonasHabitacion);

        if(
                Objects.equals(this.fechaEntrada,fechaEntrada) ||
                Objects.equals(this.fechaSalida,fechaSalida) ||
                Objects.equals(this.fechaEntrada,fechaEntrada)

        ){
            this.fechaSalida=fechaSalida;
            this.fechaEntrada=fechaEntrada;
            this.precioNoche=precioNoche;
            this.precioTotal=calcularPrecioTotal(fechaEntrada,fechaSalida,precioNoche);
        }

        this.idHuesped = idHuesped;
        this.idHabitacion = idHabitacion;

    }


}
