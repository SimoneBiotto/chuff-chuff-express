package it.zuppa.chuff.trainService.ports.input.service;

import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.trainService.dto.line.CreateLineRequest;
import it.zuppa.chuff.trainService.dto.line.DeleteLineRequest;
import it.zuppa.chuff.trainService.dto.line.EditLineRequest;
import it.zuppa.chuff.trainService.dto.line.LineResponse;
import jakarta.validation.Valid;

public interface LineService {
  LineResponse createLine(@Valid CreateLineRequest createLineRequest) throws DomainException;

  void deleteLine(@Valid DeleteLineRequest deleteLineRequest) throws DomainException;

  LineResponse editLine(@Valid EditLineRequest editLineRequest) throws DomainException;
}
