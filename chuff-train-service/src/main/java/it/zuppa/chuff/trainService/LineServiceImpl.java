package it.zuppa.chuff.trainService;

import it.zuppa.chuff.domain.train.Line;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.trainService.dto.line.CreateLineRequest;
import it.zuppa.chuff.trainService.dto.line.LineResponse;
import it.zuppa.chuff.trainService.dto.line.UpdateLineRequest;
import it.zuppa.chuff.trainService.mapper.LineDataMapper;
import it.zuppa.chuff.trainService.ports.input.service.LineService;
import it.zuppa.chuff.trainService.ports.output.repository.LineRepository;

import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LineServiceImpl implements LineService {
  private final LineRepository repository;
  private final LineDataMapper mapper;

  public LineServiceImpl(LineRepository repository, LineDataMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public Line createLine(CreateLineRequest createLineRequest) throws DomainException {
    if (repository
        .findByTypeAndCode(createLineRequest.type(), createLineRequest.code())
        .isPresent()) {
      throw new DomainException(
          String.format(
              "Find another line with type %s and code %s",
              createLineRequest.type(), createLineRequest.code()),
          DomainException.Reason.DUPLICATE_RESOURCE,
          Line.class);
    }
    log.info(
        "Creating line with type "
            + createLineRequest.type()
            + " and code "
            + createLineRequest.code());
    Line line = repository.createLine(mapper.createLineRequestToLine(createLineRequest));
    if (line == null) {
      String message =
          String.format(
              "Could not save Line with type and code: %s %s",
              createLineRequest.type(), createLineRequest.code());
      log.error(message);
      throw new DomainException(message, DomainException.Reason.ERROR_DURING_SAVING, Line.class);
    }
    log.info("Line created successfully with id {}", line.getId());
    return line;
  }

  @Override
  public void deleteLine(UUID id) throws DomainException {
    Line line =
        repository
            .findById(id)
            .orElseThrow(
                () ->
                    new DomainException(
                        "Line not found", DomainException.Reason.RESOURCE_NOT_FOUND, Line.class));
    repository.deleteLine(line);
    log.info("Line deleted successfully with id {}", line.getId());
  }

  @Override
  public Line editLine(UpdateLineRequest updateLineRequest) throws DomainException {
    Line line =
        repository
            .findById(updateLineRequest.id())
            .orElseThrow(
                () ->
                    new DomainException(
                        "Line not found with id: " + updateLineRequest.id(),
                        DomainException.Reason.RESOURCE_NOT_FOUND,
                        Line.class));
    log.info("Editing line with id {}", updateLineRequest.id());
    line.setCode(updateLineRequest.code());
    line.setType(updateLineRequest.type());
    Line updatedLine =
        repository
            .updateLine(line)
            .orElseThrow(
                () ->
                    new DomainException(
                        "Could not update Line",
                        DomainException.Reason.ERROR_DURING_SAVING,
                        Line.class));
    log.info("Line updated successfully with id {}", updatedLine.getId());
    return updatedLine;
  }

  @Override
  public Optional<Line> getLine(UUID id) {
    if (id == null) return Optional.empty();
    return repository.findById(id);
  }
}
