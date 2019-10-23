package ru.sberbank.calculation.run.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.sberbank.inkass.dto.GraphDto;
import ru.sberbank.inkass.property.StartPropertyDto;

@Service
public class GraphServiceImpl implements GraphService {

    private static final Logger logger = LoggerFactory.getLogger(GraphServiceImpl.class);

    private final RestTemplate restTemplate;

    public GraphServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public GraphDto getNewGraph() {
        final GraphDto graphDto = restTemplate.getForObject("http://localhost:8002/graph/getNewGraph", GraphDto.class);
        logger.info(String.valueOf(graphDto.getEdgeDtos().size()));
        return graphDto;
    }

    public StartPropertyDto getProp() {
        final StartPropertyDto propertyDto = restTemplate.getForObject("http://localhost:8002/graph/getProp", StartPropertyDto.class);
        logger.info(String.valueOf(propertyDto));
        return propertyDto;
    }

}
