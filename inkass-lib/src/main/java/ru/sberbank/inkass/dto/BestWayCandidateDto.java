package ru.sberbank.inkass.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@Data
@AllArgsConstructor
public class BestWayCandidateDto {
    private final List<Pair<PointDto, PointDto>> wayPair;
    private final double totalTime;
    private final double totalMoney;

}
