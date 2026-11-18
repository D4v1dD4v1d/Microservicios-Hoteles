package com.david.reservas.controller;

import com.david.commons.dto.habitaciones.RestriccionesReservaHabitacion;
import com.david.commons.enums.EstadoRegistro;
import com.david.reservas.entity.Reserva;
import com.david.reservas.enums.EstadoReserva;
import com.david.reservas.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservaConsultaHabitacionController {
    private final ReservaRepository repository;

    // Endpoint interno: requiere autenticar la comunicación entre microservicios.
    @GetMapping("/interno/habitaciones/{idHabitacion}/restricciones")
    public RestriccionesReservaHabitacion restricciones(@PathVariable Long idHabitacion) {
        List<Reserva> vigentes = repository.findByIdHabitacionAndEstadoRegistroAndEstadoReservaIn(
                idHabitacion, EstadoRegistro.ACTIVO,
                List.of(EstadoReserva.CONFIRMADA, EstadoReserva.EN_CURSO));
        short maximo = 0;
        for (Reserva reserva : vigentes) {
            if (reserva.getNumeroPersonas() != null && reserva.getNumeroPersonas() > maximo) {
                maximo = reserva.getNumeroPersonas();
            }
        }
        return new RestriccionesReservaHabitacion(!vigentes.isEmpty(), maximo);
    }
}
