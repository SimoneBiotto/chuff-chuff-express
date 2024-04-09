package it.zuppa.chuff.scheduleService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import it.zuppa.chuff.domain.schedule.Schedule;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.scheduleService.dto.schedule.CreateScheduleRequest;
import it.zuppa.chuff.scheduleService.dto.schedule.ScheduleResponse;
import it.zuppa.chuff.scheduleService.mapper.ScheduleDataMapper;
import it.zuppa.chuff.scheduleService.ports.output.repositoty.ScheduleRepository;
import it.zuppa.chuff.valueObject.ScheduleType;
import java.security.InvalidParameterException;
import java.time.DayOfWeek;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScheduleServiceImplTest {
  @Mock private ScheduleRepository repository;
  @Mock private ScheduleDataMapper mapper;
  private ScheduleServiceImpl service;

  @BeforeAll
  public void setup() {
    repository = Mockito.mock(ScheduleRepository.class);
    mapper = Mockito.mock(ScheduleDataMapper.class);
    service = new ScheduleServiceImpl(repository, mapper);
  }

  @AfterEach
  public void reset() {
    Mockito.reset(repository, mapper);
  }

  @Test
  public void itShouldCreateScheduleWhenCreateScheduleRequestIsCorrect() throws DomainException {
    CreateScheduleRequest createScheduleRequest =
        CreateScheduleRequest.builder()
            .type(ScheduleType.DAILY)
            .priceIncreasedPercentageOnFestive(1)
            .weekDay(DayOfWeek.of(1))
            .build();
    Schedule schedule =
        Schedule.builder()
            .type(createScheduleRequest.type())
            .priceIncreasedPercentageOnFestive(
                createScheduleRequest.priceIncreasedPercentageOnFestive())
            .weekDay(createScheduleRequest.weekDay())
            .build();
    schedule.setId(UUID.randomUUID());
    ScheduleResponse scheduleResponse =
        ScheduleResponse.builder()
            .id(schedule.getId())
            .type(schedule.getType())
            .priceIncreasedPercentageOnFestive(schedule.getPriceIncreasedPercentageOnFestive())
            .weekDay(schedule.getWeekDay())
            .enabled(schedule.isEnabled())
            .build();
    Mockito.when(repository.createSchedule(schedule)).thenReturn(schedule);
    Mockito.when(mapper.createScheduleRequestoToSchedule(createScheduleRequest))
        .thenReturn(schedule);
    Schedule response = service.createSchedule(createScheduleRequest);
    Mockito.verify(mapper, Mockito.times(1))
        .createScheduleRequestoToSchedule(createScheduleRequest);
    assertEquals(scheduleResponse.id(), response.getId());
    assertEquals(scheduleResponse.type(), response.getType());
    assertEquals(
        scheduleResponse.priceIncreasedPercentageOnFestive(),
        response.getPriceIncreasedPercentageOnFestive());
    assertEquals(scheduleResponse.weekDay(), response.getWeekDay());
    assertEquals(scheduleResponse.enabled(), response.isEnabled());
  }

  @Test
  public void itShouldThrowExceptionWhenScheduleIsNotSaved() {
    CreateScheduleRequest createScheduleRequest =
        CreateScheduleRequest.builder()
            .type(ScheduleType.DAILY)
            .priceIncreasedPercentageOnFestive(1)
            .weekDay(DayOfWeek.of(1))
            .build();
    Schedule schedule =
        Schedule.builder()
            .type(createScheduleRequest.type())
            .priceIncreasedPercentageOnFestive(
                createScheduleRequest.priceIncreasedPercentageOnFestive())
            .weekDay(createScheduleRequest.weekDay())
            .build();
    Mockito.when(repository.createSchedule(schedule)).thenReturn(null);
    Mockito.when(mapper.createScheduleRequestoToSchedule(createScheduleRequest))
        .thenReturn(schedule);
    DomainException exception =
        assertThrows(DomainException.class, () -> service.createSchedule(createScheduleRequest));
    assertEquals(DomainException.Reason.ERROR_DURING_SAVING, exception.getReason());
    assertEquals(Schedule.class, exception.getDomainClass());
  }

  @Test
  public void itShouldDeleteScheduleWhenIdIsCorrect() throws DomainException {
    UUID id = UUID.randomUUID();
    Schedule schedule = Schedule.builder().type(ScheduleType.DAILY).build();
    schedule.setId(id);
    Mockito.when(repository.findById(id)).thenReturn(Optional.of(schedule));
    service.deleteSchedule(id);
    Mockito.verify(repository, Mockito.times(1)).deleteSchedule(schedule);
  }

  @Test
  public void itShouldThrowExceptionWhenIdIsNull() {
    UUID id = null;
    InvalidParameterException exception =
        assertThrows(InvalidParameterException.class, () -> service.deleteSchedule(id));
  }

  @Test
  public void itShouldThrowExceptionWhenScheduleIsNotFound() {
    UUID id = UUID.randomUUID();
    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
    DomainException exception =
        assertThrows(DomainException.class, () -> service.deleteSchedule(id));
    assertEquals(DomainException.Reason.RESOURCE_NOT_FOUND, exception.getReason());
    assertEquals(Schedule.class, exception.getDomainClass());
  }

  @Test
  public void itShouldToggleEnabledStatusOfScheduleWhenIdIsCorrect() throws DomainException {
    UUID id = UUID.randomUUID();
    Schedule schedule = Schedule.builder().type(ScheduleType.DAILY).build();
    schedule.setId(id);
    Mockito.when(repository.findById(id)).thenReturn(Optional.of(schedule));
    Mockito.when(repository.updateSchedule(schedule)).thenReturn(Optional.of(schedule));
    ScheduleResponse scheduleResponse =
        ScheduleResponse.builder()
            .id(schedule.getId())
            .type(schedule.getType())
            .priceIncreasedPercentageOnFestive(schedule.getPriceIncreasedPercentageOnFestive())
            .weekDay(schedule.getWeekDay())
            .enabled(schedule.isEnabled())
            .build();
    Schedule response = service.toggleEnabledStatusSchedule(id, !schedule.isEnabled());
    Mockito.verify(repository, Mockito.times(1)).updateSchedule(schedule);
    assertEquals(scheduleResponse.id(), response.getId());
    assertEquals(scheduleResponse.type(), response.getType());
    assertEquals(
        scheduleResponse.priceIncreasedPercentageOnFestive(),
        response.getPriceIncreasedPercentageOnFestive());
    assertEquals(scheduleResponse.weekDay(), response.getWeekDay());
    assertEquals(scheduleResponse.enabled(), !response.isEnabled());
  }

  @Test
  public void itShouldThrowExceptionWhenScheduleIsNotFoundForToggleEnabledStatus() {
    UUID id = UUID.randomUUID();
    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
    DomainException exception =
        assertThrows(DomainException.class, () -> service.toggleEnabledStatusSchedule(id, true));
    assertEquals(DomainException.Reason.RESOURCE_NOT_FOUND, exception.getReason());
    assertEquals(Schedule.class, exception.getDomainClass());
  }

  @Test
  public void itShouldReturnScheduleWithDesiredStatusWhenScheduleAlreadyHasDesiredStatus()
      throws DomainException {
    UUID id = UUID.randomUUID();
    Schedule schedule = Schedule.builder().type(ScheduleType.DAILY).build();
    schedule.setId(id);
    Mockito.when(repository.findById(id)).thenReturn(Optional.of(schedule));
    ScheduleResponse scheduleResponse =
        ScheduleResponse.builder()
            .id(schedule.getId())
            .type(schedule.getType())
            .priceIncreasedPercentageOnFestive(schedule.getPriceIncreasedPercentageOnFestive())
            .weekDay(schedule.getWeekDay())
            .enabled(schedule.isEnabled())
            .build();
    Schedule response = service.toggleEnabledStatusSchedule(id, schedule.isEnabled());
    assertEquals(scheduleResponse.id(), response.getId());
    assertEquals(scheduleResponse.type(), response.getType());
    assertEquals(
        scheduleResponse.priceIncreasedPercentageOnFestive(),
        response.getPriceIncreasedPercentageOnFestive());
    assertEquals(scheduleResponse.weekDay(), response.getWeekDay());
    assertEquals(scheduleResponse.enabled(), response.isEnabled());
  }
}
