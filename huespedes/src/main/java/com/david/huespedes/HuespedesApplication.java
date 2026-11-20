package com.david.huespedes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.david.commons.clients.ReservaClient;

@EnableFeignClients(clients = {ReservaClient.class})
@SpringBootApplication(scanBasePackages = {"com.david.huespedes","com.david.commons"})
public class HuespedesApplication {
	public static void main(String[] args) {
		SpringApplication.run(HuespedesApplication.class, args);
	}

}
