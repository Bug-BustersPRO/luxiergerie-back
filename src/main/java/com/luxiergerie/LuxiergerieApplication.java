package com.luxiergerie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LuxiergerieApplication {

	public static void main(String[] args) {
		SpringApplication.run(LuxiergerieApplication.class, args);
	}
}
