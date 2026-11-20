package com.leonardo.habitacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.david.commons.clients.ReservaClient;

@EnableFeignClients(clients = {ReservaClient.class})
@SpringBootApplication(scanBasePackages = {"com.leonardo.habitacion","com.david.commons"})
public class HabitacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(HabitacionApplication.class, args);
	}

}
