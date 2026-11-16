
package com.leonardo.habitacion.service;

import com.leonardo.habitacion.dto.HabitacionRequest;
import com.leonardo.habitacion.dto.HabitacionResponse;

import java.util.List;

public interface HabitacionService {

    HabitacionResponse registrar(HabitacionRequest request);

    List<HabitacionResponse> listar();

    HabitacionResponse buscarPorId(Long idHabitacion);

    HabitacionResponse actualizar(Long idHabitacion, HabitacionRequest request);

    void eliminar(Long idHabitacion);
}
