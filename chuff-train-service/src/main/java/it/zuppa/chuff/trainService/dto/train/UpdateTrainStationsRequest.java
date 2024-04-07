package it.zuppa.chuff.trainService.dto.train;

import it.zuppa.chuff.trainService.dto.trainStop.CreateTrainStopRequest;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UpdateTrainStationsRequest(
    @NonNull UUID trainId,
    @NonNull UUID departureStationId,
    @NonNull UUID arrivalStationId,
    @NonNull @NotEmpty List<CreateTrainStopRequest> trainStopRequestList) {}
