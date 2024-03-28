package it.zuppa.chuff.trainService.ports.output.repository;

import it.zuppa.chuff.domain.train.Line;
import java.util.Optional;
import java.util.UUID;

public interface LineRepository {
  Line createLine(Line line);

  Optional<Line> findById(UUID uuid);

  Optional<Line> findByTypeAndCode(String type, String code);

  Optional<Line> updateLine(Line line);

  Optional<Line> deleteLine(Line line);
}
