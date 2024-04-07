package it.zuppa.chuff.trainService.ports.output.repository;

import it.zuppa.chuff.domain.train.Train;
import java.util.Optional;
import java.util.UUID;

public interface TrainRepository {
  Optional<Train> findById(UUID id);

  void delete(Train train);

  Train save(Train train);
}
