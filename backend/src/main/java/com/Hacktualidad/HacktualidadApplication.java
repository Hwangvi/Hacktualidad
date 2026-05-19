package com.Hacktualidad;

import com.Hacktualidad.service.CategoryService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCaching
public class HacktualidadApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(HacktualidadApplication.class, args);
	}
}
