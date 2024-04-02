package it.zuppa.chuff.stationService.dto.station;

import it.zuppa.chuff.valueObject.StationType;
import it.zuppa.chuff.domain.train.Train;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record StationResponse(
    UUID id,
    String code,
    String name,
    boolean enable,
    StationType type,
    List<Train> departureTrainList,
    List<Train> arrivalTrainList) {}
