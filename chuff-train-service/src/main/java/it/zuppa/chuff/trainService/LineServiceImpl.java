package it.zuppa.chuff.trainService;

import it.zuppa.chuff.domain.train.Line;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.trainService.dto.line.CreateLineRequest;
import it.zuppa.chuff.trainService.dto.line.DeleteLineRequest;
import it.zuppa.chuff.trainService.dto.line.EditLineRequest;
import it.zuppa.chuff.trainService.dto.line.LineResponse;
import it.zuppa.chuff.trainService.mapper.LineDataMapper;
import it.zuppa.chuff.trainService.ports.input.service.LineService;
import it.zuppa.chuff.trainService.ports.output.repository.LineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LineServiceImpl implements LineService {
  private final LineRepository repository;

  public LineServiceImpl(LineRepository repository) {
    this.repository = repository;
  }

  @Override
  public LineResponse createLine(CreateLineRequest createLineRequest) throws DomainException {
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
    Line line = repository.createLine(LineDataMapper.createLineRequestToLine(createLineRequest));
    if (line == null) {
      String message =
          String.format(
              "Could not save Line with type and code: %s %s",
              createLineRequest.type(), createLineRequest.code());
      log.error(message);
      throw new DomainException(message, DomainException.Reason.GENERIC_ERROR, Line.class);
    }
    log.info("Line created successfully with id {}", line.getId());
    return LineDataMapper.lineToLineResponse(
        line, "Line created successfully with id " + line.getId());
  }

  @Override
  public void deleteLine(DeleteLineRequest deleteLineRequest) throws DomainException {
    Line line =
        repository
            .findByTypeAndCode(deleteLineRequest.type(), deleteLineRequest.code())
            .orElseThrow(
                () ->
                    new DomainException(
                        "Line not found", DomainException.Reason.RESOURCE_NOT_FOUND, Line.class));
    repository.deleteLine(line);
    log.info("Line deleted successfully with id {}", line.getId());
  }

  @Override
  public LineResponse editLine(EditLineRequest editLineRequest) throws DomainException {
    Line line =
        repository
            .findById(editLineRequest.id())
            .orElseThrow(
                () ->
                    new DomainException(
                        "Line not found with id: " + editLineRequest.id(),
                        DomainException.Reason.RESOURCE_NOT_FOUND,
                        Line.class));
    line.setCode(editLineRequest.code());
    line.setType(editLineRequest.type());
    Line updatedLine = repository.createLine(line);
    if (updatedLine == null) {
      String message = String.format("Could not update Line with id: %s", editLineRequest.id());
      log.error(message);
      throw new DomainException(message, DomainException.Reason.GENERIC_ERROR, Line.class);
    }
    log.info("Line updated successfully with id {}", updatedLine.getId());
    return LineDataMapper.lineToLineResponse(
        updatedLine, "Line updated successfully with id " + updatedLine.getId());
  }
}
