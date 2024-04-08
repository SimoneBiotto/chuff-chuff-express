package it.zuppa.chuff.trainService.mapper;

import it.zuppa.chuff.domain.train.TrainStop;
import it.zuppa.chuff.trainService.dto.trainStop.TrainStopResponse;
import org.springframework.stereotype.Component;

@Component
public class TrainStopDataMapper {
  public TrainStopResponse trainStopToTrainStopResponse(TrainStop trainStop) {
    return TrainStopResponse.builder()
        .id(trainStop.getId())
        .departureTime(trainStop.getDepartureTime())
        .arrivalTime(trainStop.getArrivalTime())
        .build();
  }
}
