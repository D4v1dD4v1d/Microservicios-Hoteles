package com.david.commons.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reservas")
public interface ReservaClient {
    @GetMapping("/id-huesped/{idhuesped}/reservas-asignadas")
    void huespedTieneReservasAsignadas(@PathVariable Long idHuesped);

    @GetMapping("/id-habitacion/{idHabitacion}/reservas-asignadas")
    void habitacionTieneReservasAsignadas(@PathVariable Long idHabitacion);

}
