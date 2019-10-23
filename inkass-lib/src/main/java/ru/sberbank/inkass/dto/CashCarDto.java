package ru.sberbank.inkass.dto;

import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
@Builder
public class CashCarDto {
    private double currentSum;
    private PointDto currentPoint;
    private double timeSpent;
    private double cashLimit;
    private double workingDayLength;
    private static PointDto bankPoint;

    public static Map<PointDto, CashCarDto> reservedPoints = Collections.synchronizedMap(new HashMap<>());

    public static void addReservedPoint(CashCarDto cashCarDto, PointDto pointDto){
        reservedPoints.put(pointDto, cashCarDto);
    }

    public static void removeReservedPoint(PointDto pointDto){
        reservedPoints.remove(pointDto);
    }
}
