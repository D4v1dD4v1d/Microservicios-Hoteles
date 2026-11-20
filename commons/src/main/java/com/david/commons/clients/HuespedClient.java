package com.david.commons.clients;

import com.david.commons.dto.huespedes.HuespedResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "huespedes")
public interface HuespedClient {

    @GetMapping("/id-huesped/{id}")
    HuespedResponse obtenerPorIdSinEstado(@PathVariable("id") Long id);

    @GetMapping("/{id}")
    HuespedResponse obtenerPorId(@PathVariable("id") Long id);

}
