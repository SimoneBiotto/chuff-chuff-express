package it.zuppa.chuff.trainService.ports.input.service;

import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.trainService.dto.line.CreateLineRequest;
import it.zuppa.chuff.trainService.dto.line.EditLineRequest;
import it.zuppa.chuff.trainService.dto.line.LineResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public interface LineService {
  LineResponse createLine(@Valid CreateLineRequest createLineRequest) throws DomainException;

  void deleteLine(@NotBlank @NotNull UUID deleteLineRequest) throws DomainException;

  LineResponse editLine(@Valid EditLineRequest editLineRequest) throws DomainException;
}
