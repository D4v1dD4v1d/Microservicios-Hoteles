package com.david.reservas.repository;

import com.david.commons.enums.EstadoRegistro;
import com.david.reservas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva,Long> {
    List<Reserva> findByEstadoRegistro(EstadoRegistro estadoRegistro);
}
