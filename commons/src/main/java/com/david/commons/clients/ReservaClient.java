package com.david.commons.clients;

import com.david.commons.dto.habitaciones.RestriccionesReservaHabitacion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reservas")
public interface ReservaClient {
    @GetMapping("/interno/habitaciones/{idHabitacion}/restricciones")
    RestriccionesReservaHabitacion obtenerRestricciones(@PathVariable("idHabitacion") Long idHabitacion);
}
