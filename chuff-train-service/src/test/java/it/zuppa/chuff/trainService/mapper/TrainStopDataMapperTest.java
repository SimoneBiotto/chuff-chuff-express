package it.zuppa.chuff.trainService.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.zuppa.chuff.common.valueObject.Time;
import it.zuppa.chuff.domain.station.Station;
import it.zuppa.chuff.domain.train.TrainStop;
import it.zuppa.chuff.trainService.dto.trainStop.TrainStopResponse;
import java.time.LocalTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class TrainStopDataMapperTest {
  private final Station station = new Station();
  private final Time departureTime = new Time(LocalTime.MIDNIGHT);
  private final UUID uuid = UUID.randomUUID();
  private final Time arrivalTime = new Time(LocalTime.MIDNIGHT.plusHours(1));
  private final TrainStopDataMapper mapper = new TrainStopDataMapper();

  @Test
  public void itShouldReturnTrainStopResponseFromTrainStop() {
    TrainStop trainStop =
        TrainStop.builder()
            .station(station)
            .departureTime(departureTime)
            .arrivalTime(arrivalTime)
            .build();
    trainStop.setId(uuid);
    TrainStopResponse trainStopResponse = mapper.trainStopToTrainStopResponse(trainStop);
    assertEquals(uuid, trainStopResponse.id());
    assertEquals(arrivalTime, trainStopResponse.arrivalTime());
    assertEquals(departureTime, trainStopResponse.departureTime());
  }
}
