package it.zuppa.chuff.trainService.ports.output.repository;

import java.util.UUID;

public interface TrainStopRepository {
  public void delete(UUID id);
}
