package it.zuppa.chuff.trainService.ports.input.service;

import it.zuppa.chuff.domain.train.Train;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.trainService.dto.train.*;
import jakarta.validation.Valid;
import java.util.UUID;

public interface TrainService {
  TrainCompactResponse createTrain(@Valid CreateTrainRequest createTrainRequest)
      throws DomainException;

  TrainCompactResponse updateTrainSchedule(
      @Valid UpdateTrainScheduleRequest updateTrainScheduleRequest) throws DomainException;

  TrainResponse updateTrainStations(@Valid UpdateTrainStationsRequest updateTrainStationsRequest)
      throws DomainException;

  void deleteTrain(UUID id) throws DomainException;

  TrainCompactResponse toggleTrain(UUID id, boolean enable) throws DomainException;

  Train getTrain(UUID id);
}
