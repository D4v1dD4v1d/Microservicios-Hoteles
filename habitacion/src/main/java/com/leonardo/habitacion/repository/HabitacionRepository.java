package com.leonardo.habitacion.repository;

import com.david.commons.enums.EstadoRegistro;
import com.leonardo.habitacion.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {


    List<Habitacion> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    Optional<Habitacion> findByIdHabitacionAndEstadoRegistro(Long idHabitacion, EstadoRegistro estadoRegistro);

    boolean existsByNumeroAndEstadoRegistro(Integer numero, EstadoRegistro estadoRegistro);
}

