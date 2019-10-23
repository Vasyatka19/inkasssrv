package ru.sberbank.inkass.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StartPropertyDto {
    private final int graphSize;

    private final int workingDayCount;

    private final int antCount;

    private final double maxSumInPoint;

    private final double maxTimeInPoint;

    private final double maxTimeInWay;

    private final double maxMoneyInAnt;

    private final double workingDayLength;

}
