package com.leonardo.habitacion.service;

import com.david.commons.dto.habitaciones.HabitacionRequest;
import com.david.commons.dto.habitaciones.HabitacionResponse;
import com.david.commons.service.CrudService;


public interface HabitacionService extends CrudService<HabitacionRequest, HabitacionResponse> {

    // Interno (Reservas): sin importar estadoRegistro
    HabitacionResponse obtenerHabitacionSinValidarEstado(Long id);

    // Manual (ADMIN): matriz Notion 4.2
    HabitacionResponse cambiarEstado(Long id, Long idEstado);

    // Automático (Reservas): DISPONIBLE->OCUPADA, OCUPADA->LIMPIEZA, OCUPADA->DISPONIBLE
    HabitacionResponse cambiarEstadoInterno(Long id, Long idEstado);
}