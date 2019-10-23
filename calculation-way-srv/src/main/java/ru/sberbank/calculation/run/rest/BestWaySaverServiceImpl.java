package ru.sberbank.calculation.run.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.sberbank.inkass.dto.BestWayCandidateDto;

@Service
public class BestWaySaverServiceImpl implements BestWaySaverService {
    private static final Logger logger = LoggerFactory.getLogger(BestWaySaverServiceImpl.class);

    private final RestTemplate restTemplate;

    public BestWaySaverServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public int saveBestWay(BestWayCandidateDto wayCandidate) {
        final ResponseEntity<Integer> exchange = restTemplate.exchange("http://localhost:8002/graph/getNewGraph", HttpMethod.POST, null, Integer.class);
        return exchange.getBody();
    }
}
