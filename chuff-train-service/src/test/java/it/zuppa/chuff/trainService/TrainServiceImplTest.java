package it.zuppa.chuff.trainService;

import static org.junit.jupiter.api.Assertions.*;

import it.zuppa.chuff.common.valueObject.Time;
import it.zuppa.chuff.domain.train.Line;
import it.zuppa.chuff.domain.train.Train;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.trainService.dto.line.LineResponse;
import it.zuppa.chuff.trainService.dto.train.*;
import it.zuppa.chuff.trainService.mapper.TrainDataMapper;
import it.zuppa.chuff.trainService.ports.output.repository.TrainRepository;
import it.zuppa.chuff.valueObject.TrainSchedule;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrainServiceImplTest {
  private final TrainRepository repository = Mockito.mock(TrainRepository.class);
  private final TrainDataMapper mapper = Mockito.mock(TrainDataMapper.class);
  private final TrainServiceHelper helper = Mockito.mock(TrainServiceHelper.class);
  private TrainServiceImpl service;

  @BeforeAll
  public void setup() {
    service = new TrainServiceImpl(repository, mapper, helper);
  }

  @AfterEach
  public void reset() {
    Mockito.reset(repository, mapper, helper);
  }

  @Test
  public void itShouldReturnTrainCompactResponseWhenTrainIsCreatedSuccessfully()
      throws DomainException {
    CreateTrainRequest request =
        CreateTrainRequest.builder()
            .lineId(UUID.randomUUID())
            .departureStationId(UUID.randomUUID())
            .arrivalStationId(UUID.randomUUID())
            .startTime(new Time())
            .endTime(new Time())
            .repetition(new Time())
            .trainStopRequestList(List.of())
            .build();
    Line line = new Line();
    line.setId(request.lineId());
    Train train = new Train();
    train.setId(UUID.randomUUID());
    train.setLine(line);
    TrainCompactResponse response =
        TrainCompactResponse.builder()
            .id(train.getId())
            .line(LineResponse.builder().id(line.getId()).build())
            .build();
    Mockito.when(helper.createTrainEntity(request)).thenReturn(train);
    Mockito.when(repository.save(train)).thenReturn(train);
    Mockito.when(mapper.trainToTrainCompactResponse(train)).thenReturn(response);
    Train result = service.createTrain(request);
    assertNotNull(result);
    assertEquals(response.id(), result.getId());
    assertEquals(response.line().id(), result.getLine().getId());
  }

  @Test
  public void itShouldThrowsErrorWhenTrainIsNotCreated() throws DomainException {
    CreateTrainRequest request =
        CreateTrainRequest.builder()
            .lineId(UUID.randomUUID())
            .departureStationId(UUID.randomUUID())
            .arrivalStationId(UUID.randomUUID())
            .startTime(new Time())
            .endTime(new Time())
            .repetition(new Time())
            .trainStopRequestList(List.of())
            .build();
    Mockito.when(helper.createTrainEntity(request))
        .thenThrow(
            new DomainException(
                "Error", DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, Train.class));
    DomainException thrown =
        assertThrows(DomainException.class, () -> service.createTrain(request));
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldReturnTrainCompactResponseWhenTrainScheduleIsUpdatedSuccessfully()
      throws DomainException {
    UpdateTrainScheduleRequest request =
        UpdateTrainScheduleRequest.builder()
            .trainId(UUID.randomUUID())
            .startTime(new Time())
            .endTime(new Time())
            .repetition(new Time())
            .build();
    Train train = new Train();
    train.setId(request.trainId());
    train.setTrainSchedule(
        TrainSchedule.builder()
            .startTime(new Time(LocalTime.MIDNIGHT))
            .endTime(new Time(LocalTime.MIDNIGHT))
            .repetition(new Time(LocalTime.MIDNIGHT))
            .build());
    Train trainUpdated = new Train();
    trainUpdated.setId(train.getId());
    trainUpdated.setTrainSchedule(
        TrainSchedule.builder()
            .startTime(new Time(LocalTime.NOON))
            .endTime(new Time(LocalTime.NOON))
            .repetition(new Time(LocalTime.NOON))
            .build());
    Mockito.when(repository.findById(train.getId())).thenReturn(java.util.Optional.of(train));
    Mockito.when(helper.updateTrainSchedule(Mockito.any(), Mockito.any())).thenReturn(trainUpdated);
    Mockito.when(repository.save(trainUpdated)).thenReturn(trainUpdated);
    TrainCompactResponse response = TrainCompactResponse.builder().id(train.getId()).build();
    Mockito.when(mapper.trainToTrainCompactResponse(trainUpdated)).thenReturn(response);
    Train result = service.updateTrainSchedule(request);
    assertNotNull(result);
    assertEquals(response.id(), result.getId());
  }

  @Test
  public void itShouldThrowsErrorWhenTrainIsNotFoundUpdatingSchedule() {
    UpdateTrainScheduleRequest request =
        UpdateTrainScheduleRequest.builder()
            .trainId(UUID.randomUUID())
            .startTime(new Time())
            .endTime(new Time())
            .repetition(new Time())
            .build();
    Mockito.when(repository.findById(request.trainId())).thenReturn(java.util.Optional.empty());
    DomainException thrown =
        assertThrows(DomainException.class, () -> service.updateTrainSchedule(request));
    assertEquals(DomainException.Reason.RESOURCE_NOT_FOUND, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldThrowsErrorWhenTrainScheduleIsNotUpdated() throws DomainException {
    UpdateTrainScheduleRequest request =
        UpdateTrainScheduleRequest.builder()
            .trainId(UUID.randomUUID())
            .startTime(new Time())
            .endTime(new Time())
            .repetition(new Time())
            .build();
    Train train = new Train();
    train.setId(request.trainId());
    train.setTrainSchedule(
        TrainSchedule.builder()
            .startTime(new Time(LocalTime.MIDNIGHT))
            .endTime(new Time(LocalTime.MIDNIGHT))
            .repetition(new Time(LocalTime.MIDNIGHT))
            .build());
    Mockito.when(repository.findById(train.getId())).thenReturn(java.util.Optional.of(train));
    Mockito.when(helper.updateTrainSchedule(Mockito.any(), Mockito.any()))
        .thenThrow(
            new DomainException(
                "Error", DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, Train.class));
    DomainException thrown =
        assertThrows(DomainException.class, () -> service.updateTrainSchedule(request));
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldReturnTrainResponseWhenTrainStationsAreUpdatedSuccessfully()
      throws DomainException {
    UpdateTrainStationsRequest request =
        UpdateTrainStationsRequest.builder()
            .trainId(UUID.randomUUID())
            .trainStopRequestList(List.of())
            .departureStationId(UUID.randomUUID())
            .arrivalStationId(UUID.randomUUID())
            .build();
    Train train = new Train();
    train.setId(request.trainId());
    Train trainUpdated = new Train();
    trainUpdated.setId(train.getId());
    Mockito.when(repository.findById(train.getId())).thenReturn(java.util.Optional.of(train));
    Mockito.when(helper.updateTrainStations(Mockito.any(), Mockito.any())).thenReturn(trainUpdated);
    Mockito.when(repository.save(trainUpdated)).thenReturn(trainUpdated);
    TrainResponse response = TrainResponse.builder().id(train.getId()).build();
    Mockito.when(mapper.trainToTrainResponse(trainUpdated)).thenReturn(response);
    Train result = service.updateTrainStations(request);
    assertNotNull(result);
    assertEquals(response.id(), result.getId());
  }

  @Test
  public void itShouldThrowsErrorWhenTrainStationsAreNotUpdated() throws DomainException {
    UpdateTrainStationsRequest request =
        UpdateTrainStationsRequest.builder()
            .trainId(UUID.randomUUID())
            .trainStopRequestList(List.of())
            .departureStationId(UUID.randomUUID())
            .arrivalStationId(UUID.randomUUID())
            .build();
    Train train = new Train();
    train.setId(request.trainId());
    Mockito.when(repository.findById(train.getId())).thenReturn(java.util.Optional.of(train));
    Mockito.when(helper.updateTrainStations(Mockito.any(), Mockito.any()))
        .thenThrow(
            new DomainException(
                "Error", DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, Train.class));
    DomainException thrown =
        assertThrows(DomainException.class, () -> service.updateTrainStations(request));
    assertEquals(DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldThrowsErrorWhenTrainIsNotFoundUpdatingStations() {
    UpdateTrainStationsRequest request =
        UpdateTrainStationsRequest.builder()
            .trainId(UUID.randomUUID())
            .trainStopRequestList(List.of())
            .departureStationId(UUID.randomUUID())
            .arrivalStationId(UUID.randomUUID())
            .build();
    Mockito.when(repository.findById(request.trainId())).thenReturn(java.util.Optional.empty());
    DomainException thrown =
        assertThrows(DomainException.class, () -> service.updateTrainStations(request));
    assertEquals(DomainException.Reason.RESOURCE_NOT_FOUND, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldDeleteTrainWhenDeleteTrainIsValid() throws DomainException {
    Train train = new Train();
    train.setId(UUID.randomUUID());
    Mockito.when(repository.findById(train.getId())).thenReturn(java.util.Optional.of(train));
    service.deleteTrain(train.getId());
    Mockito.verify(repository, Mockito.times(1)).delete(train);
  }

  @Test
  public void itShouldThrowsErrorWhenTrainIsNotFoundDeleting() {
    Mockito.when(repository.findById(Mockito.any(UUID.class)))
        .thenReturn(java.util.Optional.empty());
    DomainException thrown =
        assertThrows(DomainException.class, () -> service.deleteTrain(UUID.randomUUID()));
    assertEquals(DomainException.Reason.RESOURCE_NOT_FOUND, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldReturnTheSameTrainWhenToggleTrainIsValidButNotSaveTheEntity()
      throws DomainException {
    Train train = new Train();
    train.setId(UUID.randomUUID());
    train.setEnabled(true);
    Mockito.when(repository.findById(train.getId())).thenReturn(Optional.of(train));
    TrainCompactResponse response = TrainCompactResponse.builder().id(train.getId()).build();
    Train result = service.toggleTrain(train.getId(), true);
    assertNotNull(result);
    assertEquals(response.id(), result.getId());
    Mockito.verify(repository, Mockito.never()).save(train);
  }

  @Test
  public void itShouldThrowsErrorWhenTrainIsNotFoundToggling() {
    Mockito.when(repository.findById(Mockito.any(UUID.class)))
        .thenReturn(java.util.Optional.empty());
    DomainException thrown =
        assertThrows(DomainException.class, () -> service.toggleTrain(UUID.randomUUID(), true));
    assertEquals(DomainException.Reason.RESOURCE_NOT_FOUND, thrown.getReason());
    assertEquals(Train.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldReturnTrainCompactResponseWhenTrainIsDisabled() throws DomainException {
    Train train = new Train();
    train.setId(UUID.randomUUID());
    train.setEnabled(true);
    Mockito.when(repository.findById(train.getId())).thenReturn(java.util.Optional.of(train));
    TrainCompactResponse response = TrainCompactResponse.builder().id(train.getId()).build();
    ArgumentCaptor<Train> captor = ArgumentCaptor.forClass(Train.class);
    Mockito.when(repository.save(train)).thenReturn(train);
    Train result = service.toggleTrain(train.getId(), false);
    Mockito.verify(repository).save(captor.capture());
    assertNotNull(result);
    assertEquals(response.id(), result.getId());
    assertFalse(train.isEnabled());
    assertFalse(captor.getValue().isEnabled());
  }

  @Test
  public void itShouldReturnTrainWhenItExists() {
    Train train = new Train();
    train.setId(UUID.randomUUID());
    Mockito.when(repository.findById(train.getId())).thenReturn(Optional.of(train));
    Train result = service.getTrain(train.getId()).orElse(null);
    assertNotNull(result);
    assertEquals(train.getId(), result.getId());
  }

  @Test
  public void itShouldReturnNullWhenTrainIsNotFound() {
    UUID id = UUID.randomUUID();
    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
    Train result = service.getTrain(id).orElse(null);
    assertNull(result);
  }
}
