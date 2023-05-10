package com.webdev.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // AuditingEntityListener 를 활성화 해주기 위한 애너테이션
public class JavaWebDev3Application {

	public static void main(String[] args) {
		SpringApplication.run(JavaWebDev3Application.class, args);
	}

}
