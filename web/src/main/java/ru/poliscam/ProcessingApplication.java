package ru.poliscam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.poliscam.processing.config.JpaConfig;

@SpringBootApplication
public class ProcessingApplication {

	public static void main(String[] args) throws Exception {

		ConfigurableApplicationContext context = SpringApplication.run(new Class<?>[] {
				ProcessingApplication.class,
				JpaConfig.class
		}, args);

	}
}
