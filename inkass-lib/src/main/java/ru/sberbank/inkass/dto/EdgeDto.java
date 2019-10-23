package ru.sberbank.inkass.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EdgeDto {

    private PointDto from;
    private PointDto to;
    private WayInfoDto wayInfo;
}
