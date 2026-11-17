
package com.leonardo.habitacion.service;



import com.david.commons.dto.habitaciones.HabitacionRequest;
import com.david.commons.dto.habitaciones.HabitacionResponse;

import java.util.List;

public interface HabitacionService {

    HabitacionResponse registrar(HabitacionRequest request);

    List<HabitacionResponse> listar();

    HabitacionResponse buscarPorId(Long idHabitacion);

    HabitacionResponse actualizar(Long idHabitacion, HabitacionRequest request);

    void eliminar(Long idHabitacion);
}
