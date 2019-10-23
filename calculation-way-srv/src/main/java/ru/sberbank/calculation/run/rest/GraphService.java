package ru.sberbank.calculation.run.rest;

import ru.sberbank.inkass.dto.GraphDto;
import ru.sberbank.inkass.property.StartPropertyDto;

public interface GraphService {

    GraphDto getNewGraph();

    StartPropertyDto getProp();
}
