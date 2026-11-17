package com.david.reservas.service;

import com.david.commons.service.CrudService;
import com.david.reservas.dto.ReservaRequest;
import com.david.reservas.dto.ReservaResponse;

import java.util.List;

public interface ReservaService extends CrudService<ReservaRequest, ReservaResponse> {
    @Override
    ReservaResponse actualizar(ReservaRequest request, Long id);

    @Override
    void eliminar(Long id);

    @Override
    List<ReservaResponse> lista();

    @Override
    ReservaResponse obtenerPorId(Long id);

    @Override
    ReservaResponse registrar(ReservaRequest request);

    void actualizarEstadoReserva(Long idReserva, Long idEstado);

}
