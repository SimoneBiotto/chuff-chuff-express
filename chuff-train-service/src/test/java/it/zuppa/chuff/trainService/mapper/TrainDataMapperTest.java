package it.zuppa.chuff.trainService.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.zuppa.chuff.common.valueObject.Time;
import it.zuppa.chuff.domain.schedule.Schedule;
import it.zuppa.chuff.domain.station.Station;
import it.zuppa.chuff.domain.train.Line;
import it.zuppa.chuff.domain.train.Train;
import it.zuppa.chuff.domain.train.TrainStop;
import it.zuppa.chuff.stationService.dto.station.StationCompactResponse;
import it.zuppa.chuff.stationService.mapper.StationDataMapper;
import it.zuppa.chuff.trainService.dto.line.LineResponse;
import it.zuppa.chuff.trainService.dto.train.TrainCompactResponse;
import it.zuppa.chuff.trainService.dto.train.TrainResponse;
import it.zuppa.chuff.trainService.dto.trainStop.TrainStopResponse;
import it.zuppa.chuff.valueObject.ScheduleType;
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
public class TrainDataMapperTest {
  private final LineDataMapper lineDataMapper = Mockito.mock(LineDataMapper.class);
  private final StationDataMapper stationDataMapper = Mockito.mock(StationDataMapper.class);
  private final TrainStopDataMapper trainStopDataMapper = Mockito.mock(TrainStopDataMapper.class);

  private TrainDataMapper dataMapper;

  private Train train;

  @BeforeAll
  public void setup() {
    dataMapper = new TrainDataMapper(lineDataMapper, stationDataMapper, trainStopDataMapper);
    train =
        Train.builder()
            .line(Line.builder().type("lineType").code("lineCode").build())
            .departureStation(
                Station.builder().code("departureStationCode").name("departureStationName").build())
            .arrivalStation(
                Station.builder().code("arrivalStationCode").name("arrivalStationName").build())
            .trainSchedule(
                TrainSchedule.builder()
                    .startTime(new Time(LocalTime.MIDNIGHT))
                    .endTime(new Time(LocalTime.MIDNIGHT.plusHours(1)))
                    .repetition(new Time(LocalTime.MIDNIGHT))
                    .build())
            .schedule(Schedule.builder().type(ScheduleType.DAILY).build())
            .trainStopList(
                List.of(
                    TrainStop.builder()
                        .arrivalTime(new Time(LocalTime.MIDNIGHT))
                        .departureTime(new Time(LocalTime.MIDNIGHT))
                        .station(Station.builder().code("stationCode").name("stationName").build())
                        .build()))
            .build();
    train.setId(UUID.randomUUID());
  }

  @AfterEach
  public void reset() {
    Mockito.reset(lineDataMapper, stationDataMapper, trainStopDataMapper);
  }

  @Test
  public void itShouldReturnTrainCompactResponseFromTrain() {
    LineResponse lineResponse = LineResponse.builder().code("lineCode").type("lineType").build();
    StationCompactResponse departureStationResponse =
        StationCompactResponse.builder()
            .code("departureStationCode")
            .name("departureStationName")
            .build();
    StationCompactResponse arrivalStationResponse =
        StationCompactResponse.builder()
            .code("arrivalStationCode")
            .name("arrivalStationName")
            .build();
    Mockito.when(lineDataMapper.lineToLineResponse(train.getLine())).thenReturn(lineResponse);
    Mockito.when(stationDataMapper.stationToStationCompactResponse(train.getDepartureStation()))
        .thenReturn(departureStationResponse);
    Mockito.when(stationDataMapper.stationToStationCompactResponse(train.getArrivalStation()))
        .thenReturn(arrivalStationResponse);
    TrainCompactResponse response = dataMapper.trainToTrainCompactResponse(train);
    assertEquals(train.getId(), response.id());
    assertEquals(lineResponse.code(), response.line().code());
    assertEquals(departureStationResponse.code(), response.departureStation().code());
    assertEquals(arrivalStationResponse.code(), response.arrivalStation().code());
    assertEquals(train.getTrainSchedule(), response.schedule());
    assertEquals(train.isEnabled(), response.enabled());
  }

  @Test
  public void itShouldReturnTrainResponseFromTrain() {
    LineResponse lineResponse =
        LineResponse.builder()
            .code(train.getLine().getCode())
            .type(train.getLine().getType())
            .build();
    StationCompactResponse departureStationResponse =
        StationCompactResponse.builder()
            .code(train.getDepartureStation().getCode())
            .name(train.getDepartureStation().getName())
            .build();
    StationCompactResponse arrivalStationResponse =
        StationCompactResponse.builder()
            .code(train.getArrivalStation().getCode())
            .name(train.getArrivalStation().getName())
            .build();
    TrainStopResponse trainStopResponse =
        TrainStopResponse.builder()
            .arrivalTime(train.getTrainStopList().get(0).getArrivalTime())
            .departureTime(train.getTrainStopList().get(0).getDepartureTime())
            .build();
    Mockito.when(lineDataMapper.lineToLineResponse(train.getLine())).thenReturn(lineResponse);
    Mockito.when(stationDataMapper.stationToStationCompactResponse(train.getDepartureStation()))
        .thenReturn(departureStationResponse);
    Mockito.when(stationDataMapper.stationToStationCompactResponse(train.getArrivalStation()))
        .thenReturn(arrivalStationResponse);
    Mockito.when(trainStopDataMapper.trainStopToTrainStopResponse(train.getTrainStopList().get(0)))
        .thenReturn(trainStopResponse);
    TrainResponse response = dataMapper.trainToTrainResponse(train);
    assertEquals(train.getId(), response.id());
    assertEquals(lineResponse.code(), response.line().code());
    assertEquals(departureStationResponse.code(), response.departureStation().code());
    assertEquals(arrivalStationResponse.code(), response.arrivalStation().code());
    assertEquals(train.getTrainSchedule(), response.schedule());
    assertEquals(train.isEnabled(), response.enabled());
    assertEquals(train.getTrainStopList().size(), response.trainStopList().size());
    assertEquals(
        train.getTrainStopList().get(0).getArrivalTime(),
        response.trainStopList().get(0).arrivalTime());
  }
}
