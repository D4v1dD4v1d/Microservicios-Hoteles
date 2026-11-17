package com.david.commons.clients;

import com.david.commons.dto.huespedes.HuespedResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "huespedes")
public interface HuespedClient {

        @GetMapping("/{id}")
        HuespedResponse obtenerPorId(@PathVariable("id") Long id);
        @GetMapping("/{id-huesped/{id}")
        HuespedResponse obtenerHuespedSinEstadoPorId(@PathVariable("id") Long id);

    @PutMapping("/{id}/tipoDocumento/{idTipoDocumento}")
    HuespedResponse actualizarTipoDocumento(
            @PathVariable Long id,
            @PathVariable Long idTipoDocumento);
}
