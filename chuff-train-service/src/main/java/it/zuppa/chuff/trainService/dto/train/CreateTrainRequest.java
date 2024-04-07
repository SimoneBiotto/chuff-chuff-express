package it.zuppa.chuff.trainService.dto.train;

import it.zuppa.chuff.common.valueObject.Date;
import it.zuppa.chuff.common.valueObject.Time;
import it.zuppa.chuff.trainService.dto.trainStop.CreateTrainStopRequest;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record CreateTrainRequest(
    @NonNull UUID departureStationId,
    @NonNull UUID arrivalStationId,
    @NonNull UUID lineId,
    UUID scheduleId,
    @NonNull Time startTime,
    @NonNull Time endTime,
    @NonNull Time repetition,
    Date startRecurrence,
    Date endRecurrence,
    @NonNull @NotEmpty List<CreateTrainStopRequest> trainStopRequestList) {}
