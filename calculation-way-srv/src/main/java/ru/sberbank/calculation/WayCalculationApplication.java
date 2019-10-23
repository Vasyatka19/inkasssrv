package ru.sberbank.calculation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WayCalculationApplication {

    public static void main(String[] args) {
        SpringApplication.run(WayCalculationApplication.class, args);
    }
}
