package it.zuppa.chuff.trainService;

import it.zuppa.chuff.common.valueObject.Date;
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
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainServiceHelper {
  private final LineService lineService;
  private final StationService stationService;
  private final ScheduleService scheduleService;

  public TrainServiceHelper(
      LineService lineService, StationService stationService, ScheduleService scheduleService) {
    this.lineService = lineService;
    this.stationService = stationService;
    this.scheduleService = scheduleService;
  }

  public Train createTrainEntity(CreateTrainRequest createTrainRequest) throws DomainException {
    List<String> messageError = new ArrayList<>();

    Station departureStation = stationService.getStation(createTrainRequest.departureStationId());
    Station arrivalStation = stationService.getStation(createTrainRequest.arrivalStationId());
    Line line = lineService.getLine(createTrainRequest.lineId());
    Schedule schedule = scheduleService.getSchedule(createTrainRequest.scheduleId());
    if (createTrainRequest.scheduleId() != null && schedule == null)
      messageError.add("Schedule with id " + createTrainRequest.scheduleId() + " not found");

    List<TrainStop> trainStopList = new ArrayList<>();
    createTrainRequest
        .trainStopRequestList()
        .forEach(
            trainStopRequest -> {
              try {
                trainStopList.add(createTrainStopEntity(trainStopRequest));
              } catch (DomainException domainException) {
                messageError.add(domainException.getMessage());
              }
            });
    if (trainStopList.isEmpty()) messageError.add("TrainStopList is required");

    TrainSchedule trainSchedule = null;
    try {
      trainSchedule =
          createTrainSchedule(
              createTrainRequest.startTime(),
              createTrainRequest.endTime(),
              createTrainRequest.repetition(),
              createTrainRequest.startRecurrence(),
              createTrainRequest.endRecurrence());
    } catch (DomainException domainException) {
      messageError.add(domainException.getMessage());
    }
    messageError.addAll(
        Train.validate(departureStation, arrivalStation, line, trainSchedule, trainStopList));
    if (!messageError.isEmpty())
      throw new DomainException(
          "Error creating train entity with "
              + createTrainRequest
              + ", cause: "
              + String.join(", ", messageError),
          DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION,
          Train.class);

    Train entity =
        Train.create(departureStation, arrivalStation, line, trainSchedule, trainStopList);
    entity.setSchedule(schedule);
    return entity;
  }

  protected TrainStop createTrainStopEntity(CreateTrainStopRequest createTrainStopRequest)
      throws DomainException {
    Station station = stationService.getStation(createTrainStopRequest.stationId());
    List<String> messageError =
        TrainStop.validate(
            station, createTrainStopRequest.arrivalTime(), createTrainStopRequest.departureTime());
    if (!messageError.isEmpty())
      throw new DomainException(
          "Error creating train stop entity, cause: " + String.join(", ", messageError),
          DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION,
          TrainStop.class);
    return TrainStop.builder()
        .station(station)
        .arrivalTime(createTrainStopRequest.arrivalTime())
        .departureTime(createTrainStopRequest.departureTime())
        .build();
  }

  protected TrainSchedule createTrainSchedule(
      Time startTime, Time endTime, Time repetition, Date startRecurrence, Date endRecurrence)
      throws DomainException {
    List<String> messageError =
        TrainSchedule.validate(startTime, endTime, repetition, startRecurrence, endRecurrence);
    if (!messageError.isEmpty())
      throw new DomainException(
          String.join(", ", messageError),
          DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION,
          Train.class);
    return TrainSchedule.create(startTime, endTime, repetition, startRecurrence, endRecurrence);
  }

  public Train updateTrainSchedule(
      UpdateTrainScheduleRequest updateTrainScheduleRequest, Train train) throws DomainException {
    List<String> errorMessage = new ArrayList<>();
    TrainSchedule trainSchedule = null;
    try {
      trainSchedule =
          createTrainSchedule(
              updateTrainScheduleRequest.startTime(),
              updateTrainScheduleRequest.endTime(),
              updateTrainScheduleRequest.repetition(),
              updateTrainScheduleRequest.startRecurrence(),
              updateTrainScheduleRequest.endRecurrence());
    } catch (DomainException domainException) {
      errorMessage.add(domainException.getMessage());
    }
    Schedule schedule = scheduleService.getSchedule(updateTrainScheduleRequest.scheduleId());
    if (updateTrainScheduleRequest.scheduleId() != null && schedule == null)
      errorMessage.add(
          "Schedule with id " + updateTrainScheduleRequest.scheduleId() + " not found");
    if (!errorMessage.isEmpty())
      throw new DomainException(
          "Error updating train entity with "
              + updateTrainScheduleRequest
              + ", cause: "
              + String.join(", ", errorMessage),
          DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION,
          Train.class);
    train.setTrainSchedule(trainSchedule);
    train.setSchedule(schedule);
    return train;
  }

  public Train updateTrainStations(
      UpdateTrainStationsRequest updateTrainStationsRequest, Train train) throws DomainException {
    List<String> errorMessage = new ArrayList<>();
    List<TrainStop> trainStopList = new ArrayList<>();
    updateTrainStationsRequest
        .trainStopRequestList()
        .forEach(
            createTrainStopRequest -> {
              try {
                trainStopList.add(createTrainStopEntity(createTrainStopRequest));
              } catch (DomainException domainException) {
                errorMessage.add(domainException.getMessage());
              }
            });
    Station departureStation =
        stationService.getStation(updateTrainStationsRequest.departureStationId());
    if (departureStation == null) errorMessage.add("departureStation is required");
    Station arrivalStation =
        stationService.getStation(updateTrainStationsRequest.arrivalStationId());
    if (arrivalStation == null) errorMessage.add("arrivalStation is required");
    if (!errorMessage.isEmpty())
      throw new DomainException(
          "Error updating train entity with "
              + updateTrainStationsRequest
              + ", cause: "
              + String.join(", ", errorMessage),
          DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION,
          Train.class);
    // TODO delete train stops
    train.setTrainStopList(trainStopList);
    train.setDepartureStation(departureStation);
    train.setArrivalStation(arrivalStation);
    return train;
  }
}
