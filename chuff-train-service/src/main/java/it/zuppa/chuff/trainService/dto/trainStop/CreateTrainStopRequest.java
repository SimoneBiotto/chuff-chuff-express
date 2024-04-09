package it.zuppa.chuff.trainService.dto.trainStop;

import it.zuppa.chuff.common.valueObject.Time;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record CreateTrainStopRequest(
    @NonNull UUID stationId, @NonNull Time arrivalTime, @NonNull Time departureTime) {}
