package ru.tsystems.sbb.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public final class TransferTrainsDto {
    private JourneyDto firstTrain;
    private JourneyDto secondTrain;
    private String transferStation;
}
