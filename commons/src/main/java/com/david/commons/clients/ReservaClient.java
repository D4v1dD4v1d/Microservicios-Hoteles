package com.david.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "reservas")
public interface ReservaClient {

}
