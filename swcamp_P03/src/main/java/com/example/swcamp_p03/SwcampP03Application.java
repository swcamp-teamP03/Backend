package com.example.swcamp_p03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SwcampP03Application {

	public static void main(String[] args) {
		SpringApplication.run(SwcampP03Application.class, args);
	}

}
