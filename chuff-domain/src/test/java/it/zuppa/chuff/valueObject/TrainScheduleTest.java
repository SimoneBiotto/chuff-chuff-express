package it.zuppa.chuff.valueObject;

import static org.junit.jupiter.api.Assertions.*;

import it.zuppa.chuff.common.valueObject.Date;
import it.zuppa.chuff.common.valueObject.Time;
import it.zuppa.chuff.domain.train.Train;
import it.zuppa.chuff.exception.DomainException;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

public class TrainScheduleTest {
  private final Time startTime = new Time(LocalTime.MIDNIGHT);
  private final Time endTime = new Time(LocalTime.MIDNIGHT.plusHours(1));
  private final Time repetition = new Time(LocalTime.MIDNIGHT.plusMinutes(1));
  private final Date startRecurrence = new Date(LocalDate.now());
  private final Date endRecurrence = new Date(LocalDate.now().plusDays(1));

  @Test
  public void itShouldCreateTrainSchedule() throws DomainException {
    TrainSchedule trainSchedule =
        TrainSchedule.create(startTime, endTime, repetition, startRecurrence, endRecurrence);
    assertEquals(startTime, trainSchedule.getStartTime());
    assertEquals(endTime, trainSchedule.getEndTime());
    assertEquals(repetition, trainSchedule.getRepetition());
    assertEquals(startRecurrence, trainSchedule.getStartRecurrence());
    assertEquals(endRecurrence, trainSchedule.getEndRecurrence());
  }

  @Test
  public void itShouldCreateTrainScheduleWithoutRecurrence() throws DomainException {
    TrainSchedule trainSchedule = TrainSchedule.create(startTime, endTime, repetition);
    assertEquals(startTime, trainSchedule.getStartTime());
    assertEquals(endTime, trainSchedule.getEndTime());
    assertEquals(repetition, trainSchedule.getRepetition());
    assertNull(trainSchedule.getStartRecurrence());
    assertNull(trainSchedule.getEndRecurrence());
  }

  @Test
  public void itShouldThrowExceptionWhenThereIsValidationConstraint() {
    DomainException exception =
        assertThrows(DomainException.class, () -> TrainSchedule.create(null, endTime, repetition));
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, exception.getReason());
    assertEquals(Train.class, exception.getDomainClass());
  }

  @Test
  void itShouldReturnEmptyListWhenAllFieldsAreValid() {
    assertTrue(
        TrainSchedule.validate(startTime, endTime, repetition, startRecurrence, endRecurrence)
            .isEmpty());
  }

  @Test
  void itShouldReturnErrorMessageWhenStartTimeIsNull() {
    assertEquals(
        "startTime is required",
        TrainSchedule.validate(null, endTime, repetition, null, null).get(0));
  }

  @Test
  void itShouldReturnErrorMessageWhenEndTimeIsNull() {
    assertEquals(
        "endTime is required",
        TrainSchedule.validate(startTime, null, repetition, null, null).get(0));
  }

  @Test
  void itShouldReturnErrorMessageWhenRepetitionIsNull() {
    assertEquals(
        "repetition is required",
        TrainSchedule.validate(startTime, endTime, null, null, null).get(0));
  }

  @Test
  void itShouldReturnErrorMessageWhenStartTimeIsAfterEndTime() {
    Time localStartTime = endTime;
    Time localEndTime = startTime;
    assertEquals(
        "startTime must be before endTime",
        TrainSchedule.validate(localStartTime, localEndTime, repetition, null, null).get(0));
  }

  @Test
  void itShouldReturnErrorMessageWhenStartRecurrenceIsNullAndEndRecurrenceIsPresent() {
    assertEquals(
        "startRecurrence is required when endRecurrence is present",
        TrainSchedule.validate(startTime, endTime, repetition, null, endRecurrence).get(0));
  }

  @Test
  void itShouldReturnErrorMessageWhenStartRecurrenceIsAfterEndRecurrence() {
    Date localStartRecurrence = endRecurrence;
    Date localEndRecurrence = startRecurrence;
    assertEquals(
        "startRecurrence must be before endRecurrence",
        TrainSchedule.validate(
                startTime, endTime, repetition, localStartRecurrence, localEndRecurrence)
            .get(0));
  }

  @Test
  void itShouldReturnMultipleErrorMessages() {
    assertEquals(
        "endTime is required; repetition is required",
        String.join("; ", TrainSchedule.validate(startTime, null, null, null, null)));
  }
}
