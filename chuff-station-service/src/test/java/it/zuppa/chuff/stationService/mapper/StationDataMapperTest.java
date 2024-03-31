package it.zuppa.chuff.stationService.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.zuppa.chuff.domain.station.Station;
import it.zuppa.chuff.domain.station.StationType;
import it.zuppa.chuff.domain.train.Train;
import it.zuppa.chuff.stationService.dto.station.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class StationDataMapperTest {
  private final String code = "code";
  private final String name = "name";
  private final UUID id = UUID.randomUUID();
  private final StationType stationType = StationType.PASSENGER;

  private final StationDataMapper mapper = new StationDataMapper();

  @Test
  public void itShouldReturnStationFromCreateStationRequest() {
    CreateStationRequest createStationRequest =
        CreateStationRequest.builder().code(code).name(name).stationType(stationType).build();
    Station station = mapper.createStationRequestToStation(createStationRequest);
    assertEquals(code, station.getCode());
    assertEquals(name, station.getName());
    assertEquals(stationType, station.getStationType());
    assertTrue(station.isEnabled());
  }

  @Test
  public void itShouldReturnStationCompactResponseFromStation() {
    Station station = Station.builder().code(code).name(name).stationType(stationType).build();
    station.setId(id);
    StationCompactResponse stationResponseCompact = mapper.stationToStationCompactResponse(station);
    assertEquals(code, stationResponseCompact.code());
    assertEquals(name, stationResponseCompact.name());
    assertEquals(id, stationResponseCompact.id());
    assertTrue(stationResponseCompact.enable());
    assertEquals(stationType, stationResponseCompact.type());
  }

  @Test
  public void itShouldReturnStationResponseFromStation() {
    List<Train> departureTrainList = new ArrayList<>();
    List<Train> arrivalTrainList = new ArrayList<>();
    Train train = new Train();
    departureTrainList.add(train);
    arrivalTrainList.add(train);
    Station station =
        Station.builder()
            .code(code)
            .name(name)
            .stationType(stationType)
            .departureTrainList(departureTrainList)
            .arrivalTrainList(arrivalTrainList)
            .build();
    station.setId(id);
    StationResponse stationResponseCompact = mapper.stationToStationResponse(station);
    assertEquals(code, stationResponseCompact.code());
    assertEquals(name, stationResponseCompact.name());
    assertEquals(id, stationResponseCompact.id());
    assertTrue(stationResponseCompact.enable());
    assertEquals(stationType, stationResponseCompact.type());
    assertEquals(departureTrainList, stationResponseCompact.departureTrainList());
    assertEquals(arrivalTrainList, stationResponseCompact.arrivalTrainList());
  }

  @Test
  public void itShouldReturnSearchStationDtoFromSearchStationRequest() {
    SearchStationRequest searchStationRequest =
        SearchStationRequest.builder()
            .code(code)
            .name(name)
            .enable(false)
            .stationType(stationType)
            .build();
    SearchStationDto searchStationDto =
        mapper.searchStationRequestToSearchStationDto(searchStationRequest);
    assertEquals(code, searchStationDto.code());
    assertEquals(name, searchStationDto.name());
    assertEquals(searchStationRequest.enable(), searchStationDto.enable());
    assertEquals(stationType, searchStationDto.stationType());
  }
}
