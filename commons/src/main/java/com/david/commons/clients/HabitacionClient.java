package com.david.commons.clients;

import com.david.commons.dto.habitaciones.HabitacionResponse;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "habitacion")
public interface HabitacionClient {
    @GetMapping("/api/habitacion/{id}")
    HabitacionResponse obtenerPorId(@PathVariable("id") Long id);

    @GetMapping("/api/habitacion/id-habitacion/{id}")
    HabitacionResponse obtenerPorIdSinEstado(@PathVariable("id") Long id);

    @PutMapping("/api/habitacion/id-habitacion/{id}/estado/{idEstado}")
    void cambiarEstado(@PathVariable("id") Long idHabitacion,
                       @PathVariable("idEstado") Long idEstado);
}
