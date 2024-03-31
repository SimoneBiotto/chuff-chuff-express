package it.zuppa.chuff.stationService.ports.input.service;

import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.stationService.dto.station.*;
import jakarta.validation.Valid;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StationService {
  StationResponseCompact createStation(@Valid CreateStationRequest createStationRequest)
      throws DomainException;

  StationResponseCompact editStation(@Valid EditStationRequest editStationRequest) throws DomainException;

  void deleteStation(UUID id) throws DomainException, InvalidParameterException;

  StationResponse getStation(UUID id);

  Optional<StationResponseCompact> searchStationBySearchStationRequest(@Valid SearchStationRequest searchStationRequest) throws InvalidParameterException;

  List<StationResponseCompact> searchAllStationSearchStationRequest(@Valid SearchStationRequest searchStationRequest) throws InvalidParameterException;

  StationResponseCompact toggleStationStatus(UUID id, boolean enable) throws DomainException;
}
