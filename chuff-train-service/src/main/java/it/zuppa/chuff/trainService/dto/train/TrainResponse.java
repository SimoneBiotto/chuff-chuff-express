package it.zuppa.chuff.trainService.dto.train;

import it.zuppa.chuff.stationService.dto.station.StationCompactResponse;
import it.zuppa.chuff.trainService.dto.line.LineResponse;
import it.zuppa.chuff.trainService.dto.trainStop.TrainStopResponse;
import it.zuppa.chuff.valueObject.TrainSchedule;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record TrainResponse(
    UUID id,
    StationCompactResponse departureStation,
    StationCompactResponse arrivalStation,
    LineResponse line,
    TrainSchedule schedule,
    boolean enabled,
    List<TrainStopResponse> trainStopList) {}
