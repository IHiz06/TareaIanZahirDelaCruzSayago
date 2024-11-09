package com.codigo.msopenfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsopenfeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsopenfeignApplication.class, args);
	}

}
