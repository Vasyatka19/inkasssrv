package ru.sberbank.service.saver;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sberbank.inkass.dto.BestWayCandidateDto;

import java.util.logging.Logger;


@RestController
public class WaySaverServiceImpl implements WaySaverService {

    private static final Logger logger = Logger.getLogger(WaySaverServiceImpl.class.getName());

    private final BestWayContainer wayContainer;

    public WaySaverServiceImpl(BestWayContainer wayContainer) {
        this.wayContainer = wayContainer;
    }

    @Override
    @PostMapping(value = "result/saveBestWayCandidate")
    public int saveBestWay(@RequestBody BestWayCandidateDto wayCandidate) {
        logger.info(String.valueOf(wayCandidate));
        return wayContainer.saveBestWay(wayCandidate);
    }
}
