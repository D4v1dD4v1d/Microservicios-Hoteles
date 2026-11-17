package com.leonardo.habitacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.david.habitaciones","com.david.commons"})
public class HabitacionApplication {

	public static void main(String[] args) {
		SpringApplication.run(HabitacionApplication.class, args);
	}

}
