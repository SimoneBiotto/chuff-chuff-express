package it.zuppa.chuff.trainService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import it.zuppa.chuff.common.valueObject.Time;
import it.zuppa.chuff.domain.schedule.Schedule;
import it.zuppa.chuff.domain.station.Station;
import it.zuppa.chuff.domain.train.Line;
import it.zuppa.chuff.domain.train.Train;
import it.zuppa.chuff.domain.train.TrainStop;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.scheduleService.ports.input.service.ScheduleService;
import it.zuppa.chuff.stationService.ports.input.service.StationService;
import it.zuppa.chuff.trainService.dto.train.CreateTrainRequest;
import it.zuppa.chuff.trainService.dto.train.UpdateTrainScheduleRequest;
import it.zuppa.chuff.trainService.dto.train.UpdateTrainStationsRequest;
import it.zuppa.chuff.trainService.dto.trainStop.CreateTrainStopRequest;
import it.zuppa.chuff.trainService.ports.input.service.LineService;
import it.zuppa.chuff.valueObject.TrainSchedule;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrainServiceHelperTest {
  private final LineService lineService = Mockito.mock(LineService.class);
  private final StationService stationService = Mockito.mock(StationService.class);
  private final ScheduleService scheduleService = Mockito.mock(ScheduleService.class);
  private TrainServiceHelper serviceHelper;

  private final Station departureStation = new Station();
  private final Station arrivalStation = new Station();
  private final Line line = new Line();
  private final Schedule schedule = new Schedule();
  private final TrainStop trainStop = new TrainStop();
  private final Time startTime = new Time(LocalTime.MIDNIGHT);
  private final Time endTime = new Time(LocalTime.MIDNIGHT.plusHours(1));
  private final Time repetition = new Time(LocalTime.MIDNIGHT);

  @BeforeAll
  public void setup() {
    serviceHelper =
        Mockito.spy(new TrainServiceHelper(lineService, stationService, scheduleService));
    departureStation.setId(UUID.randomUUID());
    arrivalStation.setId(UUID.randomUUID());
    line.setId(UUID.randomUUID());
    schedule.setId(UUID.randomUUID());
    trainStop.setId(UUID.randomUUID());
  }

  @AfterEach
  public void reset() {
    Mockito.reset(lineService, stationService, scheduleService, serviceHelper);
  }

  @Test
  public void itShouldCreateTranWhenTrainEntity() throws DomainException {
    CreateTrainRequest createTrainRequest =
        CreateTrainRequest.builder()
            .departureStationId(departureStation.getId())
            .arrivalStationId(arrivalStation.getId())
            .lineId(line.getId())
            .scheduleId(schedule.getId())
            .startTime(startTime)
            .endTime(endTime)
            .repetition(repetition)
            .trainStopRequestList(
                List.of(
                    CreateTrainStopRequest.builder()
                        .arrivalTime(startTime)
                        .departureTime(endTime)
                        .stationId(UUID.randomUUID())
                        .build()))
            .build();
    TrainSchedule trainSchedule =
        TrainSchedule.builder()
            .startTime(startTime)
            .endTime(endTime)
            .repetition(repetition)
            .build();
    Mockito.when(stationService.getStation(departureStation.getId())).thenReturn(departureStation);
    Mockito.when(stationService.getStation(arrivalStation.getId())).thenReturn(arrivalStation);
    Mockito.when(lineService.getLine(line.getId())).thenReturn(line);
    Mockito.when(scheduleService.getSchedule(schedule.getId())).thenReturn(schedule);
    Mockito.doReturn(trainStop)
        .when(serviceHelper)
        .createTrainStopEntity(createTrainRequest.trainStopRequestList().get(0));
    Mockito.doReturn(trainSchedule)
        .when(serviceHelper)
        .createTrainSchedule(
            Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    Train train = serviceHelper.createTrainEntity(createTrainRequest);
    assertEquals(departureStation.getId(), train.getDepartureStation().getId());
    assertEquals(arrivalStation.getId(), train.getArrivalStation().getId());
    assertEquals(line.getId(), train.getLine().getId());
    assertEquals(schedule.getId(), train.getSchedule().getId());
    assertEquals(trainStop.getId(), train.getTrainStopList().get(0).getId());
    assertEquals(createTrainRequest.startTime(), train.getTrainSchedule().getStartTime());
    assertEquals(createTrainRequest.endTime(), train.getTrainSchedule().getEndTime());
    assertEquals(createTrainRequest.repetition(), train.getTrainSchedule().getRepetition());
  }

  @Test
  public void itShouldThrowExceptionWhenTrainStopValid() throws DomainException {
    CreateTrainRequest createTrainRequest =
        CreateTrainRequest.builder()
            .departureStationId(departureStation.getId())
            .arrivalStationId(arrivalStation.getId())
            .lineId(line.getId())
            .scheduleId(schedule.getId())
            .startTime(new Time())
            .endTime(new Time())
            .repetition(new Time())
            .trainStopRequestList(
                List.of(
                    CreateTrainStopRequest.builder()
                        .arrivalTime(new Time())
                        .departureTime(new Time())
                        .stationId(UUID.randomUUID())
                        .build()))
            .build();
    Mockito.when(stationService.getStation(departureStation.getId())).thenReturn(departureStation);
    Mockito.when(stationService.getStation(arrivalStation.getId())).thenReturn(arrivalStation);
    Mockito.when(lineService.getLine(line.getId())).thenReturn(line);
    Mockito.when(scheduleService.getSchedule(schedule.getId())).thenReturn(schedule);
    Mockito.doThrow(
            new DomainException(
                "Error creating train stop entity",
                DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION,
                TrainStop.class))
        .when(serviceHelper)
        .createTrainStopEntity(createTrainRequest.trainStopRequestList().get(0));
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              serviceHelper.createTrainEntity(createTrainRequest);
            });
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldThrowExceptionWhenTrainStopListIsEmpty() {
    CreateTrainRequest createTrainRequest =
        CreateTrainRequest.builder()
            .departureStationId(departureStation.getId())
            .arrivalStationId(arrivalStation.getId())
            .lineId(line.getId())
            .scheduleId(schedule.getId())
            .startTime(new Time())
            .endTime(new Time())
            .repetition(new Time())
            .trainStopRequestList(List.of())
            .build();
    Mockito.when(stationService.getStation(departureStation.getId())).thenReturn(departureStation);
    Mockito.when(stationService.getStation(arrivalStation.getId())).thenReturn(arrivalStation);
    Mockito.when(lineService.getLine(line.getId())).thenReturn(line);
    Mockito.when(scheduleService.getSchedule(schedule.getId())).thenReturn(schedule);
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              serviceHelper.createTrainEntity(createTrainRequest);
            });
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldThrowExceptionWhenThereIsValidationError() {
    CreateTrainRequest createTrainRequest =
        CreateTrainRequest.builder()
            .departureStationId(departureStation.getId())
            .arrivalStationId(arrivalStation.getId())
            .lineId(line.getId())
            .scheduleId(schedule.getId())
            .startTime(new Time())
            .endTime(new Time())
            .repetition(new Time())
            .trainStopRequestList(
                List.of(
                    CreateTrainStopRequest.builder()
                        .arrivalTime(new Time())
                        .departureTime(new Time())
                        .stationId(UUID.randomUUID())
                        .build()))
            .build();
    Mockito.when(stationService.getStation(departureStation.getId())).thenReturn(null);
    Mockito.when(stationService.getStation(arrivalStation.getId())).thenReturn(arrivalStation);
    Mockito.when(lineService.getLine(line.getId())).thenReturn(line);
    Mockito.when(scheduleService.getSchedule(schedule.getId())).thenReturn(schedule);
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              serviceHelper.createTrainEntity(createTrainRequest);
            });
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldThrowExceptionWhenTrainScheduleIsNotFound() {
    CreateTrainRequest createTrainRequest =
        CreateTrainRequest.builder()
            .departureStationId(departureStation.getId())
            .arrivalStationId(arrivalStation.getId())
            .lineId(line.getId())
            .scheduleId(schedule.getId())
            .startTime(new Time())
            .endTime(new Time())
            .repetition(new Time())
            .trainStopRequestList(
                List.of(
                    CreateTrainStopRequest.builder()
                        .arrivalTime(new Time())
                        .departureTime(new Time())
                        .stationId(UUID.randomUUID())
                        .build()))
            .build();
    Mockito.when(stationService.getStation(departureStation.getId())).thenReturn(departureStation);
    Mockito.when(stationService.getStation(arrivalStation.getId())).thenReturn(arrivalStation);
    Mockito.when(lineService.getLine(line.getId())).thenReturn(line);
    Mockito.when(scheduleService.getSchedule(schedule.getId())).thenReturn(null);
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              serviceHelper.createTrainEntity(createTrainRequest);
            });
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldThrowExceptionWhenTrainScheduleIsNotValid() throws DomainException {
    CreateTrainRequest createTrainRequest =
        CreateTrainRequest.builder()
            .departureStationId(departureStation.getId())
            .arrivalStationId(arrivalStation.getId())
            .lineId(line.getId())
            .scheduleId(schedule.getId())
            .startTime(new Time())
            .endTime(new Time())
            .repetition(new Time())
            .trainStopRequestList(
                List.of(
                    CreateTrainStopRequest.builder()
                        .arrivalTime(new Time())
                        .departureTime(new Time())
                        .stationId(UUID.randomUUID())
                        .build()))
            .build();
    Mockito.when(stationService.getStation(departureStation.getId())).thenReturn(departureStation);
    Mockito.when(stationService.getStation(arrivalStation.getId())).thenReturn(arrivalStation);
    Mockito.when(lineService.getLine(line.getId())).thenReturn(line);
    Mockito.when(scheduleService.getSchedule(schedule.getId())).thenReturn(schedule);
    Mockito.doThrow(
            new DomainException(
                "Error creating train schedule",
                DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION,
                Train.class))
        .when(serviceHelper)
        .createTrainSchedule(
            createTrainRequest.startTime(),
            createTrainRequest.endTime(),
            createTrainRequest.repetition(),
            createTrainRequest.startRecurrence(),
            createTrainRequest.endRecurrence());
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              serviceHelper.createTrainEntity(createTrainRequest);
            });
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldUpdateTrainScheduleWhenUpdateTrainScheduleIsValid() throws DomainException {
    Train train = new Train();
    UpdateTrainScheduleRequest updateTrainScheduleRequest =
        UpdateTrainScheduleRequest.builder()
            .trainId(UUID.randomUUID())
            .startTime(startTime)
            .endTime(endTime)
            .repetition(repetition)
            .scheduleId(schedule.getId())
            .build();
    TrainSchedule trainSchedule =
        TrainSchedule.builder()
            .startTime(startTime)
            .endTime(endTime)
            .repetition(repetition)
            .build();
    Mockito.doReturn(trainSchedule)
        .when(serviceHelper)
        .createTrainSchedule(startTime, endTime, repetition, null, null);
    Mockito.when(scheduleService.getSchedule(schedule.getId())).thenReturn(schedule);
    serviceHelper.updateTrainSchedule(updateTrainScheduleRequest, train);
    assertEquals(startTime, train.getTrainSchedule().getStartTime());
    assertEquals(endTime, train.getTrainSchedule().getEndTime());
    assertEquals(repetition, train.getTrainSchedule().getRepetition());
    assertEquals(schedule.getId(), train.getSchedule().getId());
  }

  @Test
  public void itShouldThrowExceptionWhenUpdateTrainScheduleIsNotValid() throws DomainException {
    Train train = new Train();
    UpdateTrainScheduleRequest updateTrainScheduleRequest =
        UpdateTrainScheduleRequest.builder()
            .trainId(UUID.randomUUID())
            .startTime(startTime)
            .endTime(endTime)
            .repetition(repetition)
            .scheduleId(schedule.getId())
            .build();
    Mockito.doThrow(
            new DomainException(
                "Error creating train schedule",
                DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION,
                Train.class))
        .when(serviceHelper)
        .createTrainSchedule(startTime, endTime, repetition, null, null);
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              serviceHelper.updateTrainSchedule(updateTrainScheduleRequest, train);
            });
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldThrowExceptionWhenScheduleIsNotFound() throws DomainException {
    Train train = new Train();
    UpdateTrainScheduleRequest updateTrainScheduleRequest =
        UpdateTrainScheduleRequest.builder()
            .trainId(UUID.randomUUID())
            .startTime(startTime)
            .endTime(endTime)
            .repetition(repetition)
            .scheduleId(schedule.getId())
            .build();
    Mockito.doReturn(new TrainSchedule())
        .when(serviceHelper)
        .createTrainSchedule(startTime, endTime, repetition, null, null);
    Mockito.when(scheduleService.getSchedule(schedule.getId())).thenReturn(null);
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              serviceHelper.updateTrainSchedule(updateTrainScheduleRequest, train);
            });
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldUpdateTrainStations() throws DomainException {
    Train train = new Train();
    UpdateTrainStationsRequest updateTrainStationsRequest =
        UpdateTrainStationsRequest.builder()
            .trainId(UUID.randomUUID())
            .departureStationId(departureStation.getId())
            .arrivalStationId(arrivalStation.getId())
            .trainStopRequestList(
                List.of(
                    CreateTrainStopRequest.builder()
                        .arrivalTime(startTime)
                        .departureTime(endTime)
                        .stationId(departureStation.getId())
                        .build()))
            .build();

    Mockito.when(stationService.getStation(departureStation.getId())).thenReturn(departureStation);
    Mockito.when(stationService.getStation(arrivalStation.getId())).thenReturn(arrivalStation);
    Mockito.doReturn(trainStop)
        .when(serviceHelper)
        .createTrainStopEntity(updateTrainStationsRequest.trainStopRequestList().get(0));
    serviceHelper.updateTrainStations(updateTrainStationsRequest, train);
    assertEquals(departureStation.getId(), train.getDepartureStation().getId());
    assertEquals(arrivalStation.getId(), train.getArrivalStation().getId());
    assertEquals(trainStop.getId(), train.getTrainStopList().get(0).getId());
  }

  @Test
  public void itShouldThrowExceptionWhenUpdateTrainStationsIsNotValid() throws DomainException {
    Train train = new Train();
    UpdateTrainStationsRequest updateTrainStationsRequest =
        UpdateTrainStationsRequest.builder()
            .trainId(UUID.randomUUID())
            .departureStationId(departureStation.getId())
            .arrivalStationId(arrivalStation.getId())
            .trainStopRequestList(
                List.of(
                    CreateTrainStopRequest.builder()
                        .arrivalTime(startTime)
                        .departureTime(endTime)
                        .stationId(departureStation.getId())
                        .build()))
            .build();

    Mockito.when(stationService.getStation(departureStation.getId())).thenReturn(departureStation);
    Mockito.when(stationService.getStation(arrivalStation.getId())).thenReturn(arrivalStation);
    Mockito.doThrow(
            new DomainException(
                "Error creating train stop entity",
                DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION,
                TrainStop.class))
        .when(serviceHelper)
        .createTrainStopEntity(updateTrainStationsRequest.trainStopRequestList().get(0));
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              serviceHelper.updateTrainStations(updateTrainStationsRequest, train);
            });
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldThrowExceptionWhenDepartureStationIsNotFound() {
    Train train = new Train();
    UpdateTrainStationsRequest updateTrainStationsRequest =
        UpdateTrainStationsRequest.builder()
            .trainId(UUID.randomUUID())
            .departureStationId(departureStation.getId())
            .arrivalStationId(arrivalStation.getId())
            .trainStopRequestList(
                List.of(
                    CreateTrainStopRequest.builder()
                        .arrivalTime(startTime)
                        .departureTime(endTime)
                        .stationId(departureStation.getId())
                        .build()))
            .build();

    Mockito.when(stationService.getStation(departureStation.getId())).thenReturn(null);
    Mockito.when(stationService.getStation(arrivalStation.getId())).thenReturn(arrivalStation);
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              serviceHelper.updateTrainStations(updateTrainStationsRequest, train);
            });
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldThrowExceptionWhenArrivalStationIsNotFound() {
    Train train = new Train();
    UpdateTrainStationsRequest updateTrainStationsRequest =
        UpdateTrainStationsRequest.builder()
            .trainId(UUID.randomUUID())
            .departureStationId(departureStation.getId())
            .arrivalStationId(arrivalStation.getId())
            .trainStopRequestList(
                List.of(
                    CreateTrainStopRequest.builder()
                        .arrivalTime(startTime)
                        .departureTime(endTime)
                        .stationId(departureStation.getId())
                        .build()))
            .build();

    Mockito.when(stationService.getStation(departureStation.getId())).thenReturn(departureStation);
    Mockito.when(stationService.getStation(arrivalStation.getId())).thenReturn(null);
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              serviceHelper.updateTrainStations(updateTrainStationsRequest, train);
            });
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldReturnTrainStopWhenCreateTrainStopEntityIsValid() throws DomainException {
    CreateTrainStopRequest createTrainStopRequest =
        CreateTrainStopRequest.builder()
            .stationId(departureStation.getId())
            .arrivalTime(startTime)
            .departureTime(endTime)
            .build();
    Mockito.when(stationService.getStation(createTrainStopRequest.stationId()))
        .thenReturn(departureStation);
    TrainStop trainStop = serviceHelper.createTrainStopEntity(createTrainStopRequest);
    assertEquals(departureStation.getId(), trainStop.getStation().getId());
    assertEquals(startTime, trainStop.getArrivalTime());
    assertEquals(endTime, trainStop.getDepartureTime());
  }

  @Test
  public void itShouldThrowExceptionWhenCreateTrainStopEntityIsNotValid() {
    CreateTrainStopRequest createTrainStopRequest =
        CreateTrainStopRequest.builder()
            .stationId(departureStation.getId())
            .arrivalTime(startTime)
            .departureTime(endTime)
            .build();
    Mockito.when(stationService.getStation(createTrainStopRequest.stationId())).thenReturn(null);
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              serviceHelper.createTrainStopEntity(createTrainStopRequest);
            });
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, thrown.getReason());
    assertEquals(TrainStop.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldCreateTrainScheduleWhenCreateTrainScheduleIsValid() throws DomainException {
    TrainSchedule trainSchedule =
        serviceHelper.createTrainSchedule(startTime, endTime, repetition, null, null);
    assertEquals(startTime, trainSchedule.getStartTime());
    assertEquals(endTime, trainSchedule.getEndTime());
    assertEquals(repetition, trainSchedule.getRepetition());
  }

  @Test
  public void itShouldThrowExceptionWhenCreateTrainScheduleIsNotValid() {
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              serviceHelper.createTrainSchedule(new Time(), null, new Time(), null, null);
            });
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }
}
