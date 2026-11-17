package com.david.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "habitacion")
public interface HabitacionClient {
        //todo validar habitacion, obtener datos cambiar estado
}
