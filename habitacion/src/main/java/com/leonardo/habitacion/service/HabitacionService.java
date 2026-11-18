package com.leonardo.habitacion.service;

import com.david.commons.dto.habitaciones.DatosHabitacion;
import com.david.commons.dto.habitaciones.HabitacionRequest;
import com.david.commons.dto.habitaciones.HabitacionResponse;
import com.david.commons.enums.EstadoHabitacion;
import java.util.List;

public interface HabitacionService {
    HabitacionResponse registrar(HabitacionRequest request);
    List<HabitacionResponse> listar();
    HabitacionResponse buscarPorId(Long idHabitacion);
    DatosHabitacion consultarInterno(Long idHabitacion);
    HabitacionResponse actualizar(Long idHabitacion, HabitacionRequest request);
    void cambiarEstadoManual(Long idHabitacion, Long idEstado);
    void cambiarEstadoAutomatico(Long idHabitacion, EstadoHabitacion nuevoEstado);
    void eliminar(Long idHabitacion);
}
