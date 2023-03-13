package com.life.muna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MunaBackendApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(MunaBackendApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
