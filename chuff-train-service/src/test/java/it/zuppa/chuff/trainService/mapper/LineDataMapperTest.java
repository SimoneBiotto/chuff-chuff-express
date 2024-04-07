package it.zuppa.chuff.trainService.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.zuppa.chuff.domain.train.Line;
import it.zuppa.chuff.trainService.dto.line.CreateLineRequest;
import it.zuppa.chuff.trainService.dto.line.LineResponse;
import it.zuppa.chuff.trainService.dto.line.UpdateLineRequest;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class LineDataMapperTest {
  private final String type = "S";
  private final String code = "11";
  private final UUID id = UUID.randomUUID();

  private final LineDataMapper mapper = new LineDataMapper();

  @Test
  public void itShouldReturnLineFromCreateLineRequest() {
    CreateLineRequest createLineRequest = CreateLineRequest.builder().type(type).code(code).build();
    Line line = mapper.createLineRequestToLine(createLineRequest);
    assertEquals(code, line.getCode());
    assertEquals(type, line.getType());
  }

  @Test
  public void itShouldReturnLineFromUpdateLineRequest() {
    UpdateLineRequest updateLineRequest =
        UpdateLineRequest.builder().type(type).code(code).id(id).build();
    Line line = mapper.updateLineRequestToLine(updateLineRequest);
    assertEquals(code, line.getCode());
    assertEquals(type, line.getType());
    assertEquals(id, line.getId());
  }

  @Test
  public void itShouldReturnLineResponseFromLine() {
    Line line = Line.builder().code(code).type(type).build();
    line.setId(id);
    LineResponse lineResponse = mapper.lineToLineResponse(line);
    assertEquals(code, lineResponse.code());
    assertEquals(type, lineResponse.type());
    assertEquals(id, lineResponse.id());
  }
}
