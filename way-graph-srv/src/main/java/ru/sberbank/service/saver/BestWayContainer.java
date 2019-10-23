package ru.sberbank.service.saver;

import org.springframework.stereotype.Service;
import ru.sberbank.inkass.dto.BestWayCandidateDto;

import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class BestWayContainer {

    private final CopyOnWriteArrayList<BestWayCandidateDto> bestWayCandidateDtos;

    public BestWayContainer() {
        bestWayCandidateDtos = new CopyOnWriteArrayList<>();
    }

    public int saveBestWay(BestWayCandidateDto wayCandidate) {
        bestWayCandidateDtos.add(wayCandidate);
        return bestWayCandidateDtos.size();
    }
}
