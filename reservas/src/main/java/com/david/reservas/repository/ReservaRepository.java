package com.david.reservas.repository;

import com.david.commons.enums.EstadoRegistro;
import com.david.reservas.entity.Reserva;
import com.david.reservas.enums.EstadoReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva,Long> {
    Optional<Reserva> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    List<Reserva> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    boolean existsByIdHuespedAndEstadoRegistroAndEstadoReservaIn(
            Long idHuesped, EstadoRegistro estadoRegistro, List<EstadoReserva> estadoReserva);

    List<Reserva> findByIdHabitacionAndEstadoRegistroAndEstadoReservaIn(
            Long idHabitacion, EstadoRegistro estadoRegistro, List<EstadoReserva> estadoReserva);

    boolean existsByIdHabitacionAndEstadoRegistroAndEstadoReservaIn(
            Long idHabitacion, EstadoRegistro estadoRegistro, List<EstadoReserva> estadoReserva);
}
