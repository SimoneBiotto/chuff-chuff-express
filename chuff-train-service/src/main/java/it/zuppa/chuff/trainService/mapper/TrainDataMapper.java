package it.zuppa.chuff.trainService.mapper;

import it.zuppa.chuff.domain.train.Train;
import it.zuppa.chuff.stationService.mapper.StationDataMapper;
import it.zuppa.chuff.trainService.dto.train.TrainCompactResponse;
import it.zuppa.chuff.trainService.dto.train.TrainResponse;
import org.springframework.stereotype.Component;

@Component
public class TrainDataMapper {
  private final LineDataMapper lineDataMapper;
  private final StationDataMapper stationDataMapper;
  private final TrainStopDataMapper trainStopDataMapper;

  public TrainDataMapper(
      LineDataMapper lineDataMapper,
      StationDataMapper stationDataMapper,
      TrainStopDataMapper trainStopDataMapper) {
    this.lineDataMapper = lineDataMapper;
    this.stationDataMapper = stationDataMapper;
    this.trainStopDataMapper = trainStopDataMapper;
  }

  public TrainCompactResponse trainToTrainCompactResponse(Train train) {
    return TrainCompactResponse.builder()
        .id(train.getId())
        .arrivalStation(
            stationDataMapper.stationToStationCompactResponse(train.getArrivalStation()))
        .departureStation(
            stationDataMapper.stationToStationCompactResponse(train.getDepartureStation()))
        .line(lineDataMapper.lineToLineResponse(train.getLine()))
        .schedule(train.getTrainSchedule())
        .enabled(train.isEnabled())
        .build();
  }

  public TrainResponse trainToTrainResponse(Train train) {
    return TrainResponse.builder()
        .id(train.getId())
        .arrivalStation(
            stationDataMapper.stationToStationCompactResponse(train.getArrivalStation()))
        .departureStation(
            stationDataMapper.stationToStationCompactResponse(train.getDepartureStation()))
        .line(lineDataMapper.lineToLineResponse(train.getLine()))
        .schedule(train.getTrainSchedule())
        .enabled(train.isEnabled())
        .trainStopList(
            train.getTrainStopList().stream()
                .map(trainStopDataMapper::trainStopToTrainStopResponse)
                .toList())
        .build();
  }
}
