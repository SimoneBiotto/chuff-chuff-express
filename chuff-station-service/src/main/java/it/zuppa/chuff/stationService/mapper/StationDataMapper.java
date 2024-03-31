package it.zuppa.chuff.stationService.mapper;

import it.zuppa.chuff.domain.station.Station;
import it.zuppa.chuff.stationService.dto.station.*;

public class StationDataMapper {
  public Station createStationRequestToStation(CreateStationRequest createStationRequest) {
    return Station.builder()
        .name(createStationRequest.name())
        .code(createStationRequest.code())
        .stationType(createStationRequest.stationType())
        .build();
  }

  public StationCompactResponse stationToStationCompactResponse(Station station) {
    return StationCompactResponse.builder()
        .id(station.getId())
        .code(station.getCode())
        .name(station.getName())
        .enable(station.isEnabled())
        .type(station.getStationType())
        .build();
  }

  public StationResponse stationToStationResponse(Station station) {
    return StationResponse.builder()
        .id(station.getId())
        .code(station.getCode())
        .name(station.getName())
        .enable(station.isEnabled())
        .type(station.getStationType())
        .departureTrainList(station.getDepartureTrainList())
        .arrivalTrainList(station.getArrivalTrainList())
        .build();
  }

  public SearchStationDto searchStationRequestToSearchStationDto(
      SearchStationRequest searchStationRequest) {
    return SearchStationDto.builder()
        .id(searchStationRequest.id())
        .code(searchStationRequest.code())
        .name(searchStationRequest.name())
        .enable(searchStationRequest.enable())
        .stationType(searchStationRequest.stationType())
        .build();
  }
}
