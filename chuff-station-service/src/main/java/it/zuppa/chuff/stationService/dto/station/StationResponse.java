package it.zuppa.chuff.stationService.dto.station;

import it.zuppa.chuff.domain.station.StationType;
import it.zuppa.chuff.domain.train.Train;
import lombok.Builder;
import java.util.List;
import java.util.UUID;

@Builder
public record StationResponse(UUID id, String code, String name, boolean enable, StationType type, List<Train> departureTrainList, List<Train> arrivalTrainList) {}
