package it.zuppa.chuff.stationService;

import static org.junit.jupiter.api.Assertions.*;

import it.zuppa.chuff.domain.station.Station;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.stationService.dto.station.*;
import it.zuppa.chuff.stationService.mapper.StationDataMapper;
import it.zuppa.chuff.stationService.ports.output.repository.StationRepository;
import it.zuppa.chuff.valueObject.StationType;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StationServiceImplTest {
  private final String code = "code";
  private final String name = "name";
  private final StationType stationType = StationType.PASSENGER;
  @Mock private StationRepository repository;
  @Mock private StationDataMapper mapper;
  private StationServiceImpl service;

  @BeforeAll
  public void setup() {
    repository = Mockito.spy(StationRepository.class);
    mapper = Mockito.mock(StationDataMapper.class);
    service = new StationServiceImpl(repository, mapper);
  }

  @AfterEach
  public void reset() {
    Mockito.reset(repository, mapper);
  }

  @Test
  public void itShouldCreateStationWhenCreateStationRequestIsCorrect() throws DomainException {
    CreateStationRequest createStationRequest =
        CreateStationRequest.builder().name(name).code(code).stationType(stationType).build();
    Station station = Station.builder().name(name).code(code).stationType(stationType).build();
    station.setId(UUID.randomUUID());
    StationCompactResponse response =
        StationCompactResponse.builder()
            .id(station.getId())
            .name(name)
            .code(code)
            .enable(station.isEnabled())
            .type(stationType)
            .build();
    Mockito.when(repository.findByCode(code)).thenReturn(Optional.empty());
    Mockito.when(repository.createStation(station)).thenReturn(station);
    Mockito.when(mapper.createStationRequestToStation(createStationRequest)).thenReturn(station);
    Mockito.when(mapper.stationToStationCompactResponse(station)).thenReturn(response);
    Station result = service.createStation(createStationRequest);
    assertEquals(name, result.getName());
    assertEquals(code, result.getCode());
    assertEquals(stationType, result.getStationType());
    assertEquals(station.getId(), result.getId());
    assertEquals(station.isEnabled(), result.isEnabled());
  }

  @Test
  public void itShouldThrowExceptionWhenCodeAreDuplicated() {
    CreateStationRequest createStationRequest =
        CreateStationRequest.builder().name(name).code(code).stationType(stationType).build();
    Station station = Station.builder().name(name).code(code).stationType(stationType).build();
    Mockito.when(repository.findByCode(code)).thenReturn(Optional.of(station));
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              service.createStation(createStationRequest);
            });
    assertEquals(DomainException.Reason.DUPLICATE_RESOURCE, thrown.getReason());
    assertEquals(Station.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldThrowExceptionWhenStationIsNotSaved() {
    CreateStationRequest createStationRequest =
        CreateStationRequest.builder().name(name).code(code).stationType(stationType).build();
    Station station = Station.builder().name(name).code(code).stationType(stationType).build();
    Mockito.when(repository.findByCode(code)).thenReturn(Optional.empty());
    Mockito.when(repository.createStation(station)).thenReturn(null);
    Mockito.when(mapper.createStationRequestToStation(createStationRequest)).thenReturn(station);
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              service.createStation(createStationRequest);
            });
    assertEquals(DomainException.Reason.ERROR_DURING_SAVING, thrown.getReason());
    assertEquals(Station.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldEditStationWhenEditStationRequestIsCorrect() throws DomainException {
    UUID id = UUID.randomUUID();
    String newName = "newName";
    String newCode = "newCode";
    StationType newStationType = StationType.PASSENGER;
    EditStationRequest editStationRequest =
        EditStationRequest.builder()
            .id(id)
            .name(newName)
            .code(newCode)
            .stationType(newStationType)
            .build();
    Station station = Station.builder().name(name).code(code).stationType(stationType).build();
    Station updatedStation =
        Station.builder().name(newName).code(newCode).stationType(newStationType).build();
    station.setId(id);
    updatedStation.setId(id);
    StationCompactResponse response =
        StationCompactResponse.builder()
            .id(id)
            .name(newName)
            .code(newCode)
            .type(newStationType)
            .enable(updatedStation.isEnabled())
            .build();
    Mockito.when(repository.findById(id)).thenReturn(Optional.of(station));
    ArgumentCaptor<Station> captor = ArgumentCaptor.forClass(Station.class);
    Mockito.when(repository.updateStation(Mockito.any())).thenReturn(Optional.of(updatedStation));
    Mockito.when(mapper.stationToStationCompactResponse(updatedStation)).thenReturn(response);

    Station result = service.editStation(editStationRequest);

    Mockito.verify(repository, Mockito.times(1)).updateStation(Mockito.any());
    Mockito.verify(repository).updateStation(captor.capture());
    assertEquals(newName, captor.getValue().getName());
    assertEquals(newCode, captor.getValue().getCode());
    assertEquals(newStationType, captor.getValue().getStationType());
    assertEquals(editStationRequest.enable(), captor.getValue().isEnabled());
    assertEquals(response.id(), result.getId());
    assertEquals(response.name(), result.getName());
    assertEquals(response.code(), result.getCode());
    assertEquals(response.type(), result.getStationType());
    assertEquals(updatedStation.isEnabled(), result.isEnabled());
  }

  @Test
  public void itShouldThrowExceptionWhenStationToUpdateIsNotFound() {
    UUID id = UUID.randomUUID();
    EditStationRequest editStationRequest =
        EditStationRequest.builder()
            .id(id)
            .name(name)
            .code(code)
            .enable(true)
            .stationType(stationType)
            .build();
    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              service.editStation(editStationRequest);
            });
    assertEquals(DomainException.Reason.RESOURCE_NOT_FOUND, thrown.getReason());
    assertEquals(Station.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldThrowExceptionWhenStationIsNotUpdated() {
    UUID id = UUID.randomUUID();
    EditStationRequest editStationRequest =
        EditStationRequest.builder()
            .id(id)
            .name(name)
            .code(code)
            .enable(true)
            .stationType(stationType)
            .build();
    Station station = Station.builder().name(name).code(code).stationType(stationType).build();
    Mockito.when(repository.findById(id)).thenReturn(Optional.of(station));
    Mockito.when(repository.updateStation(station)).thenReturn(Optional.empty());
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              service.editStation(editStationRequest);
            });
    assertEquals(DomainException.Reason.ERROR_DURING_SAVING, thrown.getReason());
    assertEquals(Station.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldDeleteStationWhenIdIsCorrect() throws DomainException {
    UUID id = UUID.randomUUID();
    Station station = Station.builder().name(name).code(code).stationType(stationType).build();
    station.setId(id);
    Mockito.when(repository.findById(id)).thenReturn(Optional.of(station));
    service.deleteStation(id);
    Mockito.verify(repository, Mockito.times(1)).deleteStation(station);
  }

  @Test
  public void itShouldThrowExceptionWhenIdIsNull() {
    assertThrows(
        InvalidParameterException.class,
        () -> {
          service.deleteStation(null);
        });
  }

  @Test
  public void itShouldThrowExceptionWhenStationToDeleteIsNotFound() {
    UUID id = UUID.randomUUID();
    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              service.deleteStation(id);
            });
    assertEquals(DomainException.Reason.RESOURCE_NOT_FOUND, thrown.getReason());
    assertEquals(Station.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldReturnStationWhenIdIsCorrectAndStationExists() {
    UUID id = UUID.randomUUID();
    Station station = Station.builder().name(name).code(code).stationType(stationType).build();
    station.setId(id);
    Mockito.when(repository.findById(id)).thenReturn(Optional.of(station));
    Station result = service.getStation(id).get();
    assertEquals(station.getId(), result.getId());
    assertEquals(station.getName(), result.getName());
    assertEquals(station.getCode(), result.getCode());
    assertEquals(station.getStationType(), result.getStationType());
  }

  @Test
  public void itShouldReturnNullWhenIdIsCorrectAndStationDontExist() {
    UUID id = UUID.randomUUID();
    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
    assertTrue(service.getStation(id).isEmpty());
  }

  @Test
  public void itShouldReturnEmptyWhenIdIsNull() {
    assertTrue(service.getStation(null).isEmpty());
  }

  @Test
  public void
      itShouldReturnStationCompactResponseWhenSearchStationRequestIdDIsNotNullAndStationWithIdExists() {
    UUID id = UUID.randomUUID();
    SearchStationRequest searchStationRequest = SearchStationRequest.builder().id(id).build();
    Station station = Station.builder().name(name).code(code).stationType(stationType).build();
    station.setId(id);
    StationCompactResponse response =
        StationCompactResponse.builder()
            .id(id)
            .name(name)
            .code(code)
            .enable(station.isEnabled())
            .type(stationType)
            .build();
    Mockito.when(repository.findById(id)).thenReturn(Optional.of(station));
    Mockito.when(mapper.stationToStationCompactResponse(station)).thenReturn(response);
    Optional<Station> result =
        service.searchStationBySearchStationRequest(searchStationRequest);
    assertTrue(result.isPresent());
    assertEquals(response.id(), result.get().getId());
    assertEquals(response.name(), result.get().getName());
    assertEquals(response.code(), result.get().getCode());
    assertEquals(response.type(), result.get().getStationType());
    assertEquals(response.enable(), result.get().isEnabled());
  }

  @Test
  public void itShouldReturnNullWhenSearchStationRequestIdDIsNotNullAndStationWithIdDoesNotExist() {
    UUID id = UUID.randomUUID();
    SearchStationRequest searchStationRequest = SearchStationRequest.builder().id(id).build();
    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
    Optional<Station> result =
        service.searchStationBySearchStationRequest(searchStationRequest);
    assertFalse(result.isPresent());
  }

  @Test
  public void
      itShouldReturnStationCompactResponseWhenSearchStationRequestCodeIsNotNullAndStationWithCodeExists() {
    SearchStationRequest searchStationRequest = SearchStationRequest.builder().code(code).build();
    Station station = Station.builder().name(name).code(code).stationType(stationType).build();
    station.setId(UUID.randomUUID());
    StationCompactResponse response =
        StationCompactResponse.builder()
            .id(station.getId())
            .name(name)
            .code(code)
            .enable(station.isEnabled())
            .type(stationType)
            .build();
    Mockito.when(repository.findByCode(code)).thenReturn(Optional.of(station));
    Mockito.when(mapper.stationToStationCompactResponse(station)).thenReturn(response);
    Optional<Station> result =
        service.searchStationBySearchStationRequest(searchStationRequest);
    assertTrue(result.isPresent());
    assertEquals(response.id(), result.get().getId());
    assertEquals(response.name(), result.get().getName());
    assertEquals(response.code(), result.get().getCode());
    assertEquals(response.type(), result.get().getStationType());
    assertEquals(response.enable(), result.get().isEnabled());
  }

  @Test
  public void
      itShouldReturnNullWhenSearchStationRequestCodeIsNotNullAndStationWithCodeDoesNotExist() {
    SearchStationRequest searchStationRequest = SearchStationRequest.builder().code(code).build();
    Mockito.when(repository.findByCode(code)).thenReturn(Optional.empty());
    Optional<Station> result =
        service.searchStationBySearchStationRequest(searchStationRequest);
    assertFalse(result.isPresent());
  }

  @Test
  public void itShouldThrowInvalidParameterExceptionWhenIdAndCodeAreNull() {
    SearchStationRequest searchStationRequest = SearchStationRequest.builder().build();
    assertThrows(
        InvalidParameterException.class,
        () -> {
          service.searchStationBySearchStationRequest(searchStationRequest);
        });
  }

  @Test
  public void itShouldReturnAListOfStationWhenTheyExist() {
    SearchStationRequest searchStationRequest = SearchStationRequest.builder().build();
    SearchStationDto searchStationDto = SearchStationDto.builder().build();
    Station station = Station.builder().name(name).code(code).stationType(stationType).build();
    station.setId(UUID.randomUUID());
    StationCompactResponse response =
        StationCompactResponse.builder()
            .id(station.getId())
            .name(name)
            .code(code)
            .enable(station.isEnabled())
            .type(stationType)
            .build();
    Mockito.when(mapper.searchStationRequestToSearchStationDto(searchStationRequest))
        .thenReturn(searchStationDto);
    Mockito.when(repository.findAll(searchStationDto)).thenReturn(List.of(station));
    Mockito.when(mapper.stationToStationCompactResponse(station)).thenReturn(response);
    List<Station> result =
        service.searchAllStationSearchStationRequest(searchStationRequest);
    assertEquals(1, result.size());
    Station response1 = result.get(0);
    assertEquals(response.id(), response1.getId());
    assertEquals(response.name(), response1.getName());
    assertEquals(response.code(), response1.getCode());
    assertEquals(response.type(), response1.getStationType());
    assertEquals(response.enable(), response1.isEnabled());
  }

  @Test
  public void itShouldReturnAnEmptyListWhenTheyDontExist() {
    SearchStationRequest searchStationRequest = SearchStationRequest.builder().build();
    SearchStationDto searchStationDto = SearchStationDto.builder().build();
    Mockito.when(mapper.searchStationRequestToSearchStationDto(searchStationRequest))
        .thenReturn(searchStationDto);
    Mockito.when(repository.findAll(searchStationDto)).thenReturn(List.of());
    List<Station> result =
        service.searchAllStationSearchStationRequest(searchStationRequest);
    assertTrue(result.isEmpty());
  }

  @Test
  public void itShouldToggleStationStatusWhenIdIsCorrect() throws DomainException {
    UUID id = UUID.randomUUID();
    Station station =
        Station.builder().name(name).code(code).enabled(true).stationType(stationType).build();
    station.setId(id);
    Station stationUpdated =
        Station.builder().name(name).code(code).enabled(false).stationType(stationType).build();
    stationUpdated.setId(id);
    StationCompactResponse response =
        StationCompactResponse.builder()
            .id(id)
            .name(name)
            .code(code)
            .enable(stationUpdated.isEnabled())
            .type(stationType)
            .build();
    ArgumentCaptor<Station> captor = ArgumentCaptor.forClass(Station.class);
    Mockito.when(repository.findById(id)).thenReturn(Optional.of(station));
    Mockito.when(repository.updateStation(Mockito.any())).thenReturn(Optional.of(stationUpdated));
    Mockito.when(mapper.stationToStationCompactResponse(stationUpdated)).thenReturn(response);
    Station result = service.toggleStationStatus(id, stationUpdated.isEnabled());
    Mockito.verify(repository, Mockito.times(1)).updateStation(Mockito.any());
    Mockito.verify(repository).updateStation(captor.capture());
    assertFalse(captor.getValue().isEnabled());
    assertEquals(response.id(), result.getId());
    assertEquals(response.name(), result.getName());
    assertEquals(response.code(), result.getCode());
    assertEquals(response.type(), result.getStationType());
    assertEquals(response.enable(), result.isEnabled());
  }

  @Test
  public void itShouldDontToggleStationStatusWhenStatusIsTheSame() throws DomainException {
    UUID id = UUID.randomUUID();
    Station station =
        Station.builder().name(name).code(code).enabled(true).stationType(stationType).build();
    station.setId(id);
    StationCompactResponse response =
        StationCompactResponse.builder()
            .id(id)
            .name(name)
            .code(code)
            .enable(station.isEnabled())
            .type(stationType)
            .build();
    Mockito.when(repository.findById(id)).thenReturn(Optional.of(station));
    Mockito.when(mapper.stationToStationCompactResponse(station)).thenReturn(response);
    Station result = service.toggleStationStatus(id, station.isEnabled());
    assertEquals(response.id(), result.getId());
    assertEquals(response.name(), result.getName());
    assertEquals(response.code(), result.getCode());
    assertEquals(response.type(), result.getStationType());
    assertEquals(response.enable(), result.isEnabled());
  }

  @Test
  public void itShouldThrowExceptionWhenStationToToggleStatusIsNotFound() {
    UUID id = UUID.randomUUID();
    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              service.toggleStationStatus(id, false);
            });
    assertEquals(DomainException.Reason.RESOURCE_NOT_FOUND, thrown.getReason());
    assertEquals(Station.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldThrowExceptionWhenStationStatusIsNotUpdated() {
    UUID id = UUID.randomUUID();
    Station station = Station.builder().name(name).code(code).stationType(stationType).build();
    station.setId(id);
    Mockito.when(repository.findById(id)).thenReturn(Optional.of(station));
    Mockito.when(repository.updateStation(station)).thenReturn(Optional.empty());
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              service.toggleStationStatus(id, false);
            });
    assertEquals(DomainException.Reason.ERROR_DURING_SAVING, thrown.getReason());
    assertEquals(Station.class, thrown.getDomainClass());
  }
}
