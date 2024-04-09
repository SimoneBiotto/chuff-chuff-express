package it.zuppa.chuff.trainService;

import it.zuppa.chuff.domain.train.Train;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.trainService.dto.train.*;
import it.zuppa.chuff.trainService.mapper.TrainDataMapper;
import it.zuppa.chuff.trainService.ports.input.service.TrainService;
import it.zuppa.chuff.trainService.ports.output.repository.TrainRepository;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainServiceImpl implements TrainService {
  private final TrainRepository repository;
  private final TrainDataMapper mapper;
  private final TrainServiceHelper helper;

  public TrainServiceImpl(
      TrainRepository repository, TrainDataMapper mapper, TrainServiceHelper helper) {
    this.repository = repository;
    this.mapper = mapper;
    this.helper = helper;
  }

  @Override
  public TrainCompactResponse createTrain(CreateTrainRequest createTrainRequest)
      throws DomainException {
    log.info("creating train {}", createTrainRequest);
    Train trainCreated = repository.save(helper.createTrainEntity(createTrainRequest));
    return mapper.trainToTrainCompactResponse(trainCreated);
  }

  @Override
  public TrainCompactResponse updateTrainSchedule(
      UpdateTrainScheduleRequest updateTrainScheduleRequest) throws DomainException {
    log.info("updating train {}", updateTrainScheduleRequest);
    Train train =
        repository
            .findById(updateTrainScheduleRequest.trainId())
            .orElseThrow(
                () ->
                    new DomainException(
                        "Train not found", DomainException.Reason.RESOURCE_NOT_FOUND, Train.class));
    Train trainUpdated =
        repository.save(helper.updateTrainSchedule(updateTrainScheduleRequest, train));
    return mapper.trainToTrainCompactResponse(trainUpdated);
  }

  @Override
  public TrainResponse updateTrainStations(UpdateTrainStationsRequest updateTrainStationsRequest)
      throws DomainException {
    log.info("updating train {}", updateTrainStationsRequest);
    Train train =
        repository
            .findById(updateTrainStationsRequest.trainId())
            .orElseThrow(
                () ->
                    new DomainException(
                        "Train not found", DomainException.Reason.RESOURCE_NOT_FOUND, Train.class));
    Train trainUpdated =
        repository.save(helper.updateTrainStations(updateTrainStationsRequest, train));
    return mapper.trainToTrainResponse(trainUpdated);
  }

  @Override
  public void deleteTrain(UUID id) throws DomainException {
    log.info("delete train with id {}", id);
    Train train =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new DomainException(
                        "Train not found", DomainException.Reason.RESOURCE_NOT_FOUND, Train.class));
    repository.delete(train);
  }

  @Override
  public TrainCompactResponse toggleTrain(UUID id, boolean enable) throws DomainException {
    log.info("toggle train with id {}", id);
    Train train =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new DomainException(
                        "Train not found", DomainException.Reason.RESOURCE_NOT_FOUND, Train.class));
    if (train.isEnabled() == enable) {
      log.info("train with id {} already in the requested state {}", id, enable);
      return mapper.trainToTrainCompactResponse(train);
    }
    train.setEnabled(enable);
    Train trainUpdated = repository.save(train);
    return mapper.trainToTrainCompactResponse(trainUpdated);
  }

  @Override
  public Train getTrain(UUID id) {
    log.info("get train with id {}", id);
    return repository.findById(id).orElse(null);
  }
}
