package it.zuppa.chuff.trainService.dto.train;

import it.zuppa.chuff.common.valueObject.Date;
import it.zuppa.chuff.common.valueObject.Time;
import java.util.UUID;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UpdateTrainScheduleRequest(
    @NonNull UUID trainId,
    UUID scheduleId,
    @NonNull Time startTime,
    @NonNull Time endTime,
    @NonNull Time repetition,
    Date startRecurrence,
    Date endRecurrence) {}
