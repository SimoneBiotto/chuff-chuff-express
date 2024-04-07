package it.zuppa.chuff.trainService.dto.trainStop;

import it.zuppa.chuff.common.valueObject.Time;
import java.util.UUID;
import lombok.Builder;

@Builder
public record TrainStopResponse(UUID id, Time arrivalTime, Time departureTime) {}
