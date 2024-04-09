package it.zuppa.chuff.stationService.ports.input.service;

import it.zuppa.chuff.domain.station.Station;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.stationService.dto.station.*;
import jakarta.validation.Valid;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StationService {
  Station createStation(@Valid CreateStationRequest createStationRequest)
      throws DomainException;

  Station editStation(@Valid EditStationRequest editStationRequest)
      throws DomainException;

  void deleteStation(UUID id) throws DomainException, InvalidParameterException;

  Optional<Station> getStation(UUID id);

  Optional<Station> searchStationBySearchStationRequest(
      @Valid SearchStationRequest searchStationRequest) throws InvalidParameterException;

  List<Station> searchAllStationSearchStationRequest(
      @Valid SearchStationRequest searchStationRequest) throws InvalidParameterException;

  Station toggleStationStatus(UUID id, boolean enable) throws DomainException;
}
