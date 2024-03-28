package it.zuppa.chuff.trainService.mapper;

import it.zuppa.chuff.domain.train.Line;
import it.zuppa.chuff.trainService.dto.line.CreateLineRequest;
import it.zuppa.chuff.trainService.dto.line.EditLineRequest;
import it.zuppa.chuff.trainService.dto.line.LineResponse;
import org.springframework.stereotype.Component;

@Component
public class LineDataMapper {
  public Line createLineRequestToLine(CreateLineRequest createLineRequest) {
    return Line.builder().code(createLineRequest.code()).type(createLineRequest.type()).build();
  }

  public Line editLineRequestToLine(EditLineRequest editLineRequest) {
    Line line = Line.builder().code(editLineRequest.code()).type(editLineRequest.type()).build();
    line.setId(editLineRequest.id());
    return line;
  }

  public LineResponse lineToLineResponse(Line line) {
    return LineResponse.builder()
        .id(line.getId())
        .code(line.getCode())
        .type(line.getType())
        .build();
  }
}
