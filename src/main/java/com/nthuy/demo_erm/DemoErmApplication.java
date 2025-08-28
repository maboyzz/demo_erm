package com.nthuy.demo_erm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.nthuy.demo_erm")

public class DemoErmApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoErmApplication.class, args);
	}

}
