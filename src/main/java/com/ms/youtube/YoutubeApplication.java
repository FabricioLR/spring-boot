package com.ms.youtube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.youtube.configs.security.JwtConfiguration;


@SpringBootApplication
@RestController
@EnableConfigurationProperties(value = JwtConfiguration.class)
public class YoutubeApplication {

	public static void main(String[] args) {
		SpringApplication.run(YoutubeApplication.class, args);
	}

	@GetMapping("/")
	public ResponseEntity<String> index(){
		return ResponseEntity.status(HttpStatus.OK).body("Server running");
	}
}
