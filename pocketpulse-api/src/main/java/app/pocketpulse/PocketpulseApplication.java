package app.pocketpulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PocketpulseApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocketpulseApplication.class, args);
	}

}
