package com.david.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reservas")
public interface ReservaClient {


    @GetMapping("/id-habitacion/{idHabitacion}/personas-vigente")
    Integer personasReservaVigente(@PathVariable("idHabitacion") Long idHabitacion);

    @GetMapping("/id-huesped/{idHuesped}/reservas-asignadas")
    void huespedTieneReservasAsignadas(@PathVariable Long idHuesped);
}
