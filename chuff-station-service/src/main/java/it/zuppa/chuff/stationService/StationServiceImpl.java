package it.zuppa.chuff.stationService;

import it.zuppa.chuff.domain.station.Station;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.stationService.dto.station.*;
import it.zuppa.chuff.stationService.mapper.StationDataMapper;
import it.zuppa.chuff.stationService.ports.input.service.StationService;
import it.zuppa.chuff.stationService.ports.output.repository.StationRepository;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StationServiceImpl implements StationService {
  private final StationRepository repository;
  private final StationDataMapper mapper;

  public StationServiceImpl(StationRepository repository, StationDataMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Station createStation(CreateStationRequest createStationRequest)
      throws DomainException {
    if (repository.findByCode(createStationRequest.code()).isPresent()) {
      throw new DomainException(
          String.format("Find another station with code %s", createStationRequest.code()),
          DomainException.Reason.DUPLICATE_RESOURCE,
          Station.class);
    }
    log.info(
        "Creating station with name {} and code {}",
        createStationRequest.name(),
        createStationRequest.code());
    Station station =
        repository.createStation(mapper.createStationRequestToStation(createStationRequest));
    if (station == null) {
      String message =
          String.format("Could not save Station code: %s", createStationRequest.code());
      log.error(message);
      throw new DomainException(message, DomainException.Reason.ERROR_DURING_SAVING, Station.class);
    }
    log.info("Station created successfully with id {}", station.getId());
    return station;
  }

  @Override
  public Station editStation(EditStationRequest editStationRequest)
      throws DomainException {
    Station station =
        repository
            .findById(editStationRequest.id())
            .orElseThrow(
                () ->
                    new DomainException(
                        "Station not found with id" + editStationRequest.id(),
                        DomainException.Reason.RESOURCE_NOT_FOUND,
                        Station.class));
    log.info("Editing station with id {}", editStationRequest.id());
    station.setName(editStationRequest.name());
    station.setCode(editStationRequest.code());
    station.setEnabled(editStationRequest.enable());
    station.setStationType(editStationRequest.stationType());
    Station updatedStation =
        repository
            .updateStation(station)
            .orElseThrow(
                () ->
                    new DomainException(
                        "Could not update station with id" + editStationRequest.id(),
                        DomainException.Reason.ERROR_DURING_SAVING,
                        Station.class));
    log.info("Station edited successfully with id {}", updatedStation.getId());
    return updatedStation;
  }

  @Override
  public void deleteStation(UUID id) throws DomainException, InvalidParameterException {
    if (id == null) {
      throw new InvalidParameterException("Id cannot be null");
    }
    Station station =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new DomainException(
                        "Station not found with id" + id,
                        DomainException.Reason.RESOURCE_NOT_FOUND,
                        Station.class));
    log.info("Deleting station with id {}", id);
    repository.deleteStation(station);
    log.info("Station deleted successfully with id {}", id);
  }

  @Override
  public Optional<Station> getStation(UUID id) {
    if (id == null) return Optional.empty();
    return repository.findById(id);
  }

  @Override
  public Optional<Station> searchStationBySearchStationRequest(
      SearchStationRequest searchStationRequest) throws InvalidParameterException {
    log.info("Searching station: {}", searchStationRequest);
    Optional<Station> station;
    if (searchStationRequest.id() != null) {
      station = repository.findById(searchStationRequest.id());
    } else if (searchStationRequest.code() != null) {
      station = repository.findByCode(searchStationRequest.code());
    } else {
      log.error("Invalid search request");
      throw new InvalidParameterException("Invalid search request");
    }
    return station;
  }

  @Override
  public List<Station> searchAllStationSearchStationRequest(
      SearchStationRequest searchStationRequest) {
    log.info("Searching all stations: {}", searchStationRequest);
      return repository.findAll(mapper.searchStationRequestToSearchStationDto(searchStationRequest));
  }

  @Override
  public Station toggleStationStatus(UUID id, boolean enable)
      throws DomainException {
    Station station =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new DomainException(
                        "Station not found with id" + id,
                        DomainException.Reason.RESOURCE_NOT_FOUND,
                        Station.class));
    log.info("Toggling station status to {} (id: {})", enable, id);
    if (enable == station.isEnabled()) {
      log.info("Station already in requested status {} (id: {})", enable, id);
      return station;
    }
    station.setEnabled(enable);
    Station updatedStation =
        repository
            .updateStation(station)
            .orElseThrow(
                () ->
                    new DomainException(
                        "Could not update station with id" + id,
                        DomainException.Reason.ERROR_DURING_SAVING,
                        Station.class));
    log.info("Station status toggled successfully (id: {})", updatedStation.getId());
    return updatedStation;
  }
}
