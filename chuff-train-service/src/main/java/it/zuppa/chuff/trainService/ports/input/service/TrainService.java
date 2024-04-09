package it.zuppa.chuff.trainService.ports.input.service;

import it.zuppa.chuff.domain.train.Train;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.trainService.dto.train.*;
import jakarta.validation.Valid;

import java.util.Optional;
import java.util.UUID;

public interface TrainService {
  Train createTrain(@Valid CreateTrainRequest createTrainRequest)
      throws DomainException;

  Train updateTrainSchedule(
      @Valid UpdateTrainScheduleRequest updateTrainScheduleRequest) throws DomainException;

  Train updateTrainStations(@Valid UpdateTrainStationsRequest updateTrainStationsRequest)
      throws DomainException;

  void deleteTrain(UUID id) throws DomainException;

  Train toggleTrain(UUID id, boolean enable) throws DomainException;

  Optional<Train> getTrain(UUID id);
}
