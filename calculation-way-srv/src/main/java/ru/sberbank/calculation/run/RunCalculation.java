package ru.sberbank.calculation.run;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RunCalculation {


    @Bean
    public CommandLineRunner run(CalculationService calculationService) {
        return args -> calculationService.calcWay();
    }
}
