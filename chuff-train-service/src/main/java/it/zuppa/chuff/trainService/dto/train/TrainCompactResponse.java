package it.zuppa.chuff.trainService.dto.train;

import it.zuppa.chuff.stationService.dto.station.StationCompactResponse;
import it.zuppa.chuff.trainService.dto.line.LineResponse;
import it.zuppa.chuff.valueObject.TrainSchedule;
import java.util.UUID;
import lombok.Builder;

@Builder
public record TrainCompactResponse(
    UUID id,
    StationCompactResponse departureStation,
    StationCompactResponse arrivalStation,
    LineResponse line,
    TrainSchedule schedule,
    boolean enabled) {}
