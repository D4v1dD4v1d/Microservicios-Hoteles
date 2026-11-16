package com.david.huespedes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.david.huespedes","com.david.commons"})
public class HuespedesApplication {
	public static void main(String[] args) {
		SpringApplication.run(HuespedesApplication.class, args);
	}

}
