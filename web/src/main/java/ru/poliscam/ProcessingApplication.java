package ru.poliscam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import ru.poliscam.processing.config.JpaConfig;
import ru.poliscam.web.config.StaticResourceConfig;

@SpringBootApplication
@EnableWebMvc
public class ProcessingApplication {

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext context = SpringApplication.run(new Class<?>[] {
				ProcessingApplication.class,
				JpaConfig.class,
				StaticResourceConfig.class
		}, args);

	}
}
