package com.david.reservas.service;

import com.david.commons.service.CrudService;
import com.david.reservas.dto.ActualizarReservaRequest;
import com.david.reservas.dto.ReservaRequest;
import com.david.reservas.dto.ReservaResponse;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.Map;

public interface ReservaService extends CrudService<ReservaRequest, ReservaResponse> {
    @Override
    ReservaResponse actualizar(ReservaRequest request, Long id);

    ReservaResponse actualizarReserva(ActualizarReservaRequest request, Long id);

    @Override
    void eliminar(Long id);

    @Override
    List<ReservaResponse> lista();

    @Override
    ReservaResponse obtenerPorId(Long id);

    @Override
    ReservaResponse registrar(ReservaRequest request);

    Integer personasReservaVigente(Long idHabitacion);

    List<ReservaResponse> historico();

    Map<String, List<ReservaResponse>> reportePorEstado();

    void actualizarEstadoReserva(Long idReserva, Long idEstado);

    void huespedTieneReservasAsignadas(@Positive(message = "El idHuesped debe ser positivo") Long idHuesped);

    void validarEliminacionHuesped(@Positive (message = "El idHuesped debe ser positivo") Long idHuesped);

    void habitacionTieneReservasAsignadas(@Positive(message = "El idHabitacion debe ser positivo") Long idHabitacion);
}
