package it.zuppa.chuff.trainService.ports.output.repository;

import it.zuppa.chuff.domain.train.Line;
import java.util.Optional;
import java.util.UUID;

public interface LineRepository {
  public Line createLine(Line line);

  public Optional<Line> findById(UUID uuid);

  public Optional<Line> findByTypeAndCode(String type, String code);

  public Optional<Line> updateLine(Line line);

  public Optional<Line> deleteLine(Line line);
}
