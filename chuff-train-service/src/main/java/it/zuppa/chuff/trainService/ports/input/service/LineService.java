package it.zuppa.chuff.trainService.ports.input.service;

import it.zuppa.chuff.domain.train.Line;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.trainService.dto.line.CreateLineRequest;
import it.zuppa.chuff.trainService.dto.line.LineResponse;
import it.zuppa.chuff.trainService.dto.line.UpdateLineRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface LineService {
  Line createLine(@Valid CreateLineRequest createLineRequest) throws DomainException;

  void deleteLine(@NotBlank @NotNull UUID deleteLineRequest) throws DomainException;

  Line editLine(@Valid UpdateLineRequest updateLineRequest) throws DomainException;

  Optional<Line> getLine(@NotNull UUID id);
}
