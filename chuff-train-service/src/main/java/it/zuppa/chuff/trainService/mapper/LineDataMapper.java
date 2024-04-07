package it.zuppa.chuff.trainService.mapper;

import it.zuppa.chuff.domain.train.Line;
import it.zuppa.chuff.trainService.dto.line.CreateLineRequest;
import it.zuppa.chuff.trainService.dto.line.LineResponse;
import it.zuppa.chuff.trainService.dto.line.UpdateLineRequest;
import org.springframework.stereotype.Component;

@Component
public class LineDataMapper {
  public Line createLineRequestToLine(CreateLineRequest createLineRequest) {
    return Line.builder().code(createLineRequest.code()).type(createLineRequest.type()).build();
  }

  public Line updateLineRequestToLine(UpdateLineRequest updateLineRequest) {
    Line line =
        Line.builder().code(updateLineRequest.code()).type(updateLineRequest.type()).build();
    line.setId(updateLineRequest.id());
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
