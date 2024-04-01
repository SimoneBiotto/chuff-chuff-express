package it.zuppa.chuff.scheduleService.mapper;

import it.zuppa.chuff.domain.schedule.Schedule;
import it.zuppa.chuff.scheduleService.dto.schedule.CreateScheduleRequest;
import it.zuppa.chuff.scheduleService.dto.schedule.ScheduleResponse;

public class ScheduleDataMapper {
  public Schedule createScheduleRequestoToSchedule(CreateScheduleRequest createScheduleRequest) {
    return Schedule.builder()
        .type(createScheduleRequest.type())
        .priceIncreasedPercentageOnFestive(
            createScheduleRequest.priceIncreasedPercentageOnFestive())
        .weekDay(createScheduleRequest.weekDay())
        .build();
  }

  public ScheduleResponse scheduleToScheduleResponse(Schedule schedule) {
    return ScheduleResponse.builder()
        .id(schedule.getId())
        .type(schedule.getType())
        .priceIncreasedPercentageOnFestive(schedule.getPriceIncreasedPercentageOnFestive())
        .weekDay(schedule.getWeekDay())
        .enabled(schedule.isEnabled())
        .build();
  }
}
