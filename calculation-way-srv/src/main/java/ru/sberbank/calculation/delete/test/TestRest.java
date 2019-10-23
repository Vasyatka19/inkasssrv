package ru.sberbank.calculation.delete.test;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.sberbank.inkass.dto.GraphDto;

import java.util.logging.Logger;

@Component
public class TestRest {

    private static final Logger logger = Logger.getLogger(TestRest.class.getName());

    private final RestTemplate restTemplate;

    public TestRest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
//        final GraphDto forObject = restTemplate.getForObject(CONNECT + UrlMap.GRAPH_GET_NEW_GRAPH.getUrl(), GraphDto.class);
        final GraphDto forObject = restTemplate.getForObject("http://localhost:8002/graph/getNewGraph", GraphDto.class);

        logger.info(String.valueOf(forObject.getEdgeDtos().size()));

    }

}
