package ru.sberbank.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.sberbank.inkass.property.StartPropertyDto;

@Configuration
@PropertySource("classpath:graph.properties")
public class PropertiesWithJavaConfig {

    @Value("${WORKING_DAY_COUNT}")
    public int workingDayCount;
    @Value("${ANT_COUNT_MULTIPLIER}")
    public int antCountMultiplier;
    @Value("${MAX_SUM_IN_POINT}")
    public double maxSumInPoint;
    @Value("${MAX_TIME_IN_POINT}")
    public double maxTimeInPoint;
    @Value("${MAX_TIME_IN_WAY}")
    public double maxTimeInWay;
    @Value("${MAX_MONEY_IN_ANT_MULTIPLIER}")
    public double maxMoneyInAntMultiplier;
    @Value("${WORKING_DAY_LENGTH}")
    public double workingDayLength;
    @Value("${GRAPH_SIZE}")
    private int graphSize;

    @Bean
    public StartPropertyDto getStartPropertyDto() {
        return new StartPropertyDto(graphSize
                , workingDayCount
                , graphSize * antCountMultiplier
                , maxSumInPoint
                , maxTimeInPoint
                , maxTimeInWay
                , maxSumInPoint * maxMoneyInAntMultiplier
                , workingDayLength);
    }
}
