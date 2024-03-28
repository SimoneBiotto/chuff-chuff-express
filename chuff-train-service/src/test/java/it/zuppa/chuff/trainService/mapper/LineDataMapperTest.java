package it.zuppa.chuff.trainService.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.zuppa.chuff.domain.train.Line;
import it.zuppa.chuff.trainService.dto.line.CreateLineRequest;
import it.zuppa.chuff.trainService.dto.line.DeleteLineRequest;
import it.zuppa.chuff.trainService.dto.line.EditLineRequest;
import it.zuppa.chuff.trainService.dto.line.LineResponse;
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
  public void itShouldReturnLineFromEditLineRequest() {
    EditLineRequest editLineRequest =
        EditLineRequest.builder().type(type).code(code).id(id).build();
    Line line = mapper.editLineRequestToLine(editLineRequest);
    assertEquals(code, line.getCode());
    assertEquals(type, line.getType());
    assertEquals(id, line.getId());
  }

  @Test
  public void itShouldReturnLineFromDeleteLineRequest() {
    DeleteLineRequest deleteLineRequest =
        DeleteLineRequest.builder().type(type).code(code).id(id).build();
    Line line = mapper.deleteLineRequestToLine(deleteLineRequest);
    assertEquals(code, line.getCode());
    assertEquals(type, line.getType());
    assertEquals(id, line.getId());
  }

  @Test
  public void itShouldReturnLineResponseFromLine() {
    Line line = Line.builder().code(code).type(type).build();
    line.setId(id);
    String message = "message";
    LineResponse lineResponse = mapper.lineToLineResponse(line, message);
    assertEquals(code, lineResponse.code());
    assertEquals(type, lineResponse.type());
    assertEquals(id, lineResponse.id());
    assertEquals(message, lineResponse.message());
  }
}
