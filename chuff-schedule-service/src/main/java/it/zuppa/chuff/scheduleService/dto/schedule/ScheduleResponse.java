package it.zuppa.chuff.scheduleService.dto.schedule;

import it.zuppa.chuff.valueObject.ScheduleType;
import java.time.DayOfWeek;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ScheduleResponse(
    UUID id,
    ScheduleType type,
    int priceIncreasedPercentageOnFestive,
    DayOfWeek weekDay,
    boolean enabled) {}
