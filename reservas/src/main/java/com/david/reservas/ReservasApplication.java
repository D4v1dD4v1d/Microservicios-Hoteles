package com.david.reservas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.david.commons.clients.HabitacionClient;
import com.david.commons.clients.HuespedClient;

@EnableFeignClients(clients = {HabitacionClient.class, HuespedClient.class})
@SpringBootApplication(scanBasePackages = {"com.david.reservas","com.david.commons"})
public class ReservasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservasApplication.class, args);
	}

}
