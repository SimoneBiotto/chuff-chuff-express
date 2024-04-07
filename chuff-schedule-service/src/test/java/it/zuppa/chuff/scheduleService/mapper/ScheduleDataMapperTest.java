package it.zuppa.chuff.scheduleService.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.zuppa.chuff.domain.schedule.Schedule;
import it.zuppa.chuff.scheduleService.dto.schedule.CreateScheduleRequest;
import it.zuppa.chuff.scheduleService.dto.schedule.ScheduleResponse;
import it.zuppa.chuff.valueObject.ScheduleType;
import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ScheduleDataMapperTest {
  private final ScheduleDataMapper mapper = new ScheduleDataMapper();

  @ParameterizedTest
  @MethodSource("provideCreateScheduleRequestForItShouldReturnScheduleFromCreateScheduleRequest")
  public void itShouldReturnScheduleFromCreateScheduleRequest(CreateScheduleRequest request) {
    Schedule schedule = mapper.createScheduleRequestoToSchedule(request);
    assertTrue(schedule.isEnabled());
    assertEquals(request.type(), schedule.getType());
    assertEquals(
        request.priceIncreasedPercentageOnFestive(),
        schedule.getPriceIncreasedPercentageOnFestive());
    assertEquals(request.weekDay(), schedule.getWeekDay());
  }

  @ParameterizedTest
  @MethodSource("provideScheduleForItShouldReturnScheduleResponseFromSchedule")
  public void itShouldReturnScheduleResponseFromSchedule(Schedule schedule) {
    ScheduleResponse response = mapper.scheduleToScheduleResponse(schedule);
    assertEquals(schedule.getType(), response.type());
    assertEquals(
        schedule.getPriceIncreasedPercentageOnFestive(),
        response.priceIncreasedPercentageOnFestive());
    assertEquals(schedule.getWeekDay(), response.weekDay());
    assertEquals(schedule.getId(), response.id());
    assertEquals(schedule.isEnabled(), response.enabled());
  }

  private static Stream<Arguments>
      provideCreateScheduleRequestForItShouldReturnScheduleFromCreateScheduleRequest() {
    return Stream.of(
        Arguments.of(CreateScheduleRequest.builder().type(ScheduleType.OCCASIONALLY).build()),
        Arguments.of(
            CreateScheduleRequest.builder()
                .type(ScheduleType.WEEKLY)
                .weekDay(DayOfWeek.of(1))
                .build()),
        Arguments.of(
            CreateScheduleRequest.builder()
                .type(ScheduleType.DAILY)
                .priceIncreasedPercentageOnFestive(10)
                .build()),
        Arguments.of(
            CreateScheduleRequest.builder()
                .type(ScheduleType.WORKDAY)
                .priceIncreasedPercentageOnFestive(2)
                .build()),
        Arguments.of(CreateScheduleRequest.builder().type(ScheduleType.WEEKEND).build()));
  }

  private static Stream<Arguments> provideScheduleForItShouldReturnScheduleResponseFromSchedule() {
    List<Schedule> scheduleList =
        List.of(
            Schedule.builder().type(ScheduleType.OCCASIONALLY).build(),
            Schedule.builder()
                .type(ScheduleType.WEEKLY)
                .priceIncreasedPercentageOnFestive(1)
                .weekDay(DayOfWeek.of(1))
                .build(),
            Schedule.builder()
                .type(ScheduleType.DAILY)
                .priceIncreasedPercentageOnFestive(1)
                .enabled(false)
                .build(),
            Schedule.builder().type(ScheduleType.WORKDAY).enabled(false).build(),
            Schedule.builder().type(ScheduleType.WEEKEND).build());
    scheduleList.forEach(schedule -> schedule.setId(UUID.randomUUID()));
    return scheduleList.stream().map(Arguments::of);
  }
}
