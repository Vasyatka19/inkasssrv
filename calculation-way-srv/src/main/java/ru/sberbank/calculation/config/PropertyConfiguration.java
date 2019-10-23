package ru.sberbank.calculation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sberbank.calculation.run.rest.GraphService;
import ru.sberbank.inkass.property.StartPropertyDto;

@Configuration
public class PropertyConfiguration {

    @Bean
    public StartPropertyDto getProp(GraphService graphService) {
        return graphService.getProp();
    }
}
