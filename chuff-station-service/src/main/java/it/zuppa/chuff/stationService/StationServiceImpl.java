package it.zuppa.chuff.stationService;

import it.zuppa.chuff.domain.station.Station;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.stationService.dto.station.*;
import it.zuppa.chuff.stationService.mapper.StationDataMapper;
import it.zuppa.chuff.stationService.ports.input.service.StationService;
import it.zuppa.chuff.stationService.ports.output.repository.StationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public StationResponseCompact createStation(CreateStationRequest createStationRequest) throws DomainException {
        if (repository.findByCode(createStationRequest.code()).isPresent()) {
            throw new DomainException(String.format("Find another station with code %s", createStationRequest.code()), DomainException.Reason.DUPLICATE_RESOURCE, Station.class);
        }
        log.info("Creating station with name {} and code {}", createStationRequest.name(), createStationRequest.code());
        Station station = repository.createStation(mapper.createStationRequestToStation(createStationRequest));
        if (station == null) {
            String message = String.format("Could not save Station code: %s", createStationRequest.code());
            log.error(message);
            throw new DomainException(message, DomainException.Reason.ERROR_DURING_SAVING, Station.class);
        }
        log.info("Station created successfully with id {}", station.getId());
        return mapper.stationToStationCompactResponse(station);
    }

    @Override
    public StationResponseCompact editStation(EditStationRequest editStationRequest) throws DomainException {
        Station station = repository.findById(editStationRequest.id()).orElseThrow(() -> new DomainException("Station not found with id" + editStationRequest.id(), DomainException.Reason.RESOURCE_NOT_FOUND, Station.class));
        log.info("Editing station with id {}", editStationRequest.id());
        station.setName(editStationRequest.name());
        station.setCode(editStationRequest.code());
        Station updatedStation = repository.updateStation(station).orElseThrow(() -> new DomainException("Could not update station with id" + editStationRequest.id(), DomainException.Reason.ERROR_DURING_SAVING, Station.class));
        log.info("Station edited successfully with id {}", updatedStation.getId());
        return mapper.stationToStationCompactResponse(updatedStation);
    }

  @Override
  public void deleteStation(UUID id) throws DomainException, InvalidParameterException {
        if (id == null) {
            throw new InvalidParameterException("Id cannot be null");
        }
        Station station = repository.findById(id).orElseThrow(() -> new DomainException("Station not found with id" + id, DomainException.Reason.RESOURCE_NOT_FOUND, Station.class));
        log.info("Deleting station with id {}", id);
        repository.deleteStation(station);
        log.info("Station deleted successfully with id {}", id);
    }

    @Override
    public StationResponse getStation(UUID id) {
        if (id == null) return null;
        return repository.findById(id).map(mapper::stationToStationResponse).orElse(null);
    }

    @Override
    public Optional<StationResponseCompact> searchStationBySearchStationRequest(SearchStationRequest searchStationRequest) throws InvalidParameterException {
        log.info("Searching station: {}", searchStationRequest);
        Optional<Station> station;
        if (searchStationRequest.id() != null) {
            station = repository.searchById(searchStationRequest.id());
        } else if (searchStationRequest.code() != null) {
            station = repository.searchByCode(searchStationRequest.code());
        } else {
            log.error("Invalid search request");
            throw new InvalidParameterException("Invalid search request");
        }
        return station.map(mapper::stationToStationCompactResponse);
    }

    @Override
    public List<StationResponseCompact> searchAllStationSearchStationRequest(SearchStationRequest searchStationRequest) throws InvalidParameterException {
        log.info("Searching all stations: {}", searchStationRequest);
        List<Station> stations = repository.searchAllBySearchStationDto(mapper.searchStationRequestToSearchStationDto(searchStationRequest));
        return stations.stream().map(mapper::stationToStationCompactResponse).toList();
    }
}

