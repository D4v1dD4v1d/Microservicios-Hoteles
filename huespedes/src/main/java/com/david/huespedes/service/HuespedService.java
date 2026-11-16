package com.david.huespedes.service;


import com.david.commons.dto.huespedes.HuespedRequest;
import com.david.commons.dto.huespedes.HuespedResponse;
import com.david.commons.service.CrudService;

public interface HuespedService extends CrudService<HuespedRequest , HuespedResponse> {

    HuespedResponse obtenerHuespedPorIdSinEstado(Long id);

    //void actualizarEstadoResgistroHuesped(Long id);
}
