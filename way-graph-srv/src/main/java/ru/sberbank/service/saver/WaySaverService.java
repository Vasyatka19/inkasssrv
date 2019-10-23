package ru.sberbank.service.saver;

import ru.sberbank.inkass.dto.BestWayCandidateDto;

public interface WaySaverService {


    int saveBestWay(BestWayCandidateDto wayCandidate);

}
