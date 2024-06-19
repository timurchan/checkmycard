package com.gtimurchan.checkmycard;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CheckmycardApplication {

	public static void main(String[] args) {
		LOG.debug("start start start");
		SpringApplication.run(CheckmycardApplication.class, args);
	}

}
