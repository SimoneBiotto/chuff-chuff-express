package it.zuppa.chuff.domain;

import static org.junit.jupiter.api.Assertions.*;

import it.zuppa.chuff.common.valueObject.Time;
import it.zuppa.chuff.domain.station.Station;
import it.zuppa.chuff.domain.train.TrainStop;
import it.zuppa.chuff.exception.DomainException;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

public class TrainStopTest {
  private final Station station = new Station();
  private final Time arrivalTime = new Time(LocalTime.MIDNIGHT);
  private final Time departureTime = new Time(LocalTime.MIDNIGHT.plusHours(1));

  @Test
  public void itShouldCreateTrainStop() throws DomainException {
    TrainStop trainStop = TrainStop.create(station, arrivalTime, departureTime);
    assertEquals(trainStop.getStation(), station);
    assertEquals(trainStop.getArrivalTime(), arrivalTime);
    assertEquals(trainStop.getDepartureTime(), departureTime);
  }

  @Test
  public void itShouldThrowExceptionWhenThereIsValidationConstraint() {
    DomainException exception =
        assertThrows(DomainException.class, () -> TrainStop.create(null, null, null));
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, exception.getReason());
    assertEquals(TrainStop.class, exception.getDomainClass());
  }

  @Test
  public void itShouldReturnEmptyListWhenAllFieldsAreValid() {
    assertTrue(TrainStop.validate(station, arrivalTime, departureTime).isEmpty());
  }

  @Test
  public void itShouldReturnErrorMessageWhenStationIsNull() {
    assertEquals(
        "station is required", TrainStop.validate(null, arrivalTime, departureTime).get(0));
  }

  @Test
  public void itShouldReturnErrorMessageWhenArrivalTimeIsNull() {
    assertEquals(
        "arrivalTime is required", TrainStop.validate(station, null, departureTime).get(0));
  }

  @Test
  public void itShouldReturnErrorMessageWhenDepartureTimeIsNull() {
    assertEquals(
        "departureTime is required", TrainStop.validate(station, arrivalTime, null).get(0));
  }

  @Test
  public void itShouldReturnErrorMessageWhenArrivalTimeIsAfterDepartureTime() {
    assertEquals(
        "arrivalTime must be before departureTime",
        TrainStop.validate(station, departureTime, arrivalTime).get(0));
  }

  @Test
  public void itShouldReturnMultipleErrorMessages() {
    assertEquals(3, TrainStop.validate(null, null, null).size());
  }
}
