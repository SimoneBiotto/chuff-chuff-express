package it.zuppa.chuff.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import it.zuppa.chuff.domain.schedule.Schedule;
import it.zuppa.chuff.domain.station.Station;
import it.zuppa.chuff.domain.train.Line;
import it.zuppa.chuff.domain.train.Train;
import it.zuppa.chuff.domain.train.TrainStop;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.valueObject.TrainSchedule;
import java.util.List;
import org.junit.jupiter.api.Test;

public class TrainTest {
  private final Station station = new Station();
  private final Line line = new Line();
  private final TrainSchedule trainSchedule = new TrainSchedule();
  private final Schedule schedule = new Schedule();
  private final List<TrainStop> trainStopList = List.of(new TrainStop());

  @Test
  public void itShouldCreateTrain() throws DomainException {
    Train train = Train.create(station, station, line, trainSchedule, trainStopList, schedule);
    assertEquals(station, train.getArrivalStation());
    assertEquals(station, train.getDepartureStation());
    assertEquals(line, train.getLine());
    assertEquals(trainSchedule, train.getTrainSchedule());
    assertEquals(schedule, train.getSchedule());
    assertEquals(trainStopList.size(), train.getTrainStopList().size());
    assertEquals(trainStopList.get(0), train.getTrainStopList().get(0));
  }

  @Test
  public void itShouldCreateTrainWithoutSchedule() throws DomainException {
    Train train = Train.create(station, station, line, trainSchedule, trainStopList, null);
    assertEquals(station, train.getArrivalStation());
    assertEquals(station, train.getDepartureStation());
    assertEquals(line, train.getLine());
    assertEquals(trainSchedule, train.getTrainSchedule());
    assertEquals(trainStopList.size(), train.getTrainStopList().size());
    assertEquals(trainStopList.get(0), train.getTrainStopList().get(0));
  }

  @Test
  public void itShouldThrowExceptionWhenThereIsValidationConstraint() {
    DomainException exception =
        assertThrows(DomainException.class, () -> Train.create(null, null, null, null, null, null));
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, exception.getReason());
    assertEquals(Train.class, exception.getDomainClass());
  }

  @Test
  public void itShouldReturnEmptyListWhenAllFieldsAreValid() {
    assertEquals(0, Train.validate(station, station, line, trainSchedule, trainStopList).size());
  }

  @Test
  public void itShouldReturnErrorMessageWhenDepartureStationIsNull() {
    assertEquals(
        "departureStation is required",
        Train.validate(null, station, line, trainSchedule, trainStopList).get(0));
  }

  @Test
  public void itShouldReturnErrorMessageWhenArrivalStationIsNull() {
    assertEquals(
        "arrivalStation is required",
        Train.validate(station, null, line, trainSchedule, trainStopList).get(0));
  }

  @Test
  public void itShouldReturnErrorMessageWhenLineIsNull() {
    assertEquals(
        "line is required",
        Train.validate(station, station, null, trainSchedule, trainStopList).get(0));
  }

  @Test
  public void itShouldReturnErrorMessageWhenTrainScheduleIsNull() {
    assertEquals(
        "trainSchedule is required",
        Train.validate(station, station, line, null, trainStopList).get(0));
  }

  @Test
  public void itShouldReturnErrorMessageWhenTrainStopListIsNull() {
    assertEquals(
        "trainStopList is required",
        Train.validate(station, station, line, trainSchedule, null).get(0));
  }

  @Test
  public void itShouldReturnErrorMessageWhenTrainStopListIsEmpty() {
    assertEquals(
        "trainStopList is required",
        Train.validate(station, station, line, trainSchedule, List.of()).get(0));
  }

  @Test
  public void itShouldReturnMultipleErrorMessages() {
    assertEquals(5, Train.validate(null, null, null, null, null).size());
  }
}
