package com.example.Elit17Plus_frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Elit17PlusFrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(Elit17PlusFrontendApplication.class, args);
	}

}
