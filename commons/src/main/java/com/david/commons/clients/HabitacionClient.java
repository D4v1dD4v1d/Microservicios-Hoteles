package com.david.commons.clients;

import com.david.commons.dto.habitaciones.DatosHabitacion;
import com.david.commons.enums.EstadoHabitacion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "habitacion")
public interface HabitacionClient {
    @GetMapping("/id-habitacion/{idHabitacion}")
    DatosHabitacion obtenerDatos(@PathVariable("idHabitacion") Long idHabitacion);

    @PatchMapping("/interno/{idHabitacion}/estado/{estado}")
    void cambiarEstado(@PathVariable("idHabitacion") Long idHabitacion,
                       @PathVariable("estado") EstadoHabitacion estado);
}
