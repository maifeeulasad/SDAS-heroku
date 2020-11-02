package com.mua.mas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MasApplication.class, args);
	}

	/*
	sudo lsof -t -i:8080
	sudo kill some_number
	 */

}
