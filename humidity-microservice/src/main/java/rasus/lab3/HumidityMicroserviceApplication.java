package rasus.lab3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HumidityMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HumidityMicroserviceApplication.class, args);
	}

}
