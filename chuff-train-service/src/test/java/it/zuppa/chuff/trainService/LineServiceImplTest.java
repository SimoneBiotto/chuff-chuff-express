package it.zuppa.chuff.trainService;

import static org.junit.jupiter.api.Assertions.*;

import it.zuppa.chuff.domain.train.Line;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.trainService.dto.line.CreateLineRequest;
import it.zuppa.chuff.trainService.dto.line.DeleteLineRequest;
import it.zuppa.chuff.trainService.dto.line.EditLineRequest;
import it.zuppa.chuff.trainService.dto.line.LineResponse;
import it.zuppa.chuff.trainService.mapper.LineDataMapper;
import it.zuppa.chuff.trainService.ports.output.repository.LineRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LineServiceImplTest {
  private final String type = "s";
  private final String code = "11";
  @Mock private LineRepository repository;
  @Mock private LineDataMapper lineDataMapper;
  private LineServiceImpl lineService;

  @BeforeAll
  public void setup() {
    repository = Mockito.mock(LineRepository.class);
    lineDataMapper = Mockito.mock(LineDataMapper.class);
    lineService = new LineServiceImpl(repository, lineDataMapper);
  }

  @Test
  public void itShouldCreateLineWhenCreateLineIsValid() throws DomainException {
    CreateLineRequest request = CreateLineRequest.builder().type(type).code(code).build();
    Line line = Line.builder().type(type).code(code).build();
    line.setId(UUID.randomUUID());
    LineResponse response =
        LineResponse.builder()
            .id(line.getId())
            .type(type)
            .code(code)
            .message("Line created successfully with id " + line.getId())
            .build();
    Mockito.when(repository.findByTypeAndCode(type, code)).thenReturn(Optional.empty());
    Mockito.when(repository.createLine(line)).thenReturn(line);
    Mockito.when(lineDataMapper.createLineRequestToLine(request)).thenReturn(line);
    Mockito.when(
            lineDataMapper.lineToLineResponse(
                line, "Line created successfully with id " + line.getId()))
        .thenReturn(response);
    LineResponse result = lineService.createLine(request);
    assertNotNull(result);
    assertEquals(response.id(), result.id());
    assertEquals(response.type(), result.type());
    assertEquals(response.code(), result.code());
    assertEquals(response.message(), result.message());
  }

  @Test
  public void itShouldThrowExceptionWhenCodeAndLineAreDuplicated() {
    CreateLineRequest request = CreateLineRequest.builder().type(type).code(code).build();
    Line line = Line.builder().type(type).code(code).build();
    Mockito.when(repository.findByTypeAndCode(type, code)).thenReturn(Optional.of(line));
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              lineService.createLine(request);
            });
    assertEquals(DomainException.Reason.DUPLICATE_RESOURCE, thrown.getReason());
    assertEquals(Line.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldThrowExceptionWhenLineIsNotSaved() {
    CreateLineRequest request = CreateLineRequest.builder().type(type).code(code).build();
    Line line = Line.builder().type(type).code(code).build();
    Mockito.when(repository.findByTypeAndCode(type, code)).thenReturn(Optional.empty());
    Mockito.when(lineDataMapper.createLineRequestToLine(request)).thenReturn(line);
    Mockito.when(repository.createLine(line)).thenReturn(null);
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              lineService.createLine(request);
            });
    assertEquals(DomainException.Reason.GENERIC_ERROR, thrown.getReason());
    assertEquals(Line.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldDeleteLineWhenDeleteLineIsValid() throws DomainException {
    DeleteLineRequest request =
        DeleteLineRequest.builder().id(UUID.randomUUID()).type(type).code(code).build();
    Line line = Line.builder().type(type).code(code).build();
    line.setId(request.id());
    line.setId(UUID.randomUUID());
    Mockito.when(repository.findByTypeAndCode(type, code)).thenReturn(Optional.of(line));
    Mockito.when(repository.deleteLine(line)).thenReturn(Optional.of(line));
    lineService.deleteLine(request);
    Mockito.verify(repository, Mockito.times(1)).deleteLine(line);
  }

  @Test
  public void itShouldThrowExceptionWhenLineIsNotFound() {
    DeleteLineRequest request =
        DeleteLineRequest.builder().id(UUID.randomUUID()).type(type).code(code).build();
    Mockito.when(repository.findByTypeAndCode(type, code)).thenReturn(Optional.empty());
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              lineService.deleteLine(request);
            });
    assertEquals(DomainException.Reason.RESOURCE_NOT_FOUND, thrown.getReason());
    assertEquals(Line.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldEditLineWhenEditLineIsValid() throws DomainException {
    EditLineRequest request =
        EditLineRequest.builder()
            .id(UUID.randomUUID())
            .type("typeChanged")
            .code("codeChanged")
            .build();
    Line line = Line.builder().type(type).code(code).build();
    line.setId(request.id());
    Line lineChanged = Line.builder().type(request.type()).code(request.code()).build();
    LineResponse response =
        LineResponse.builder()
            .id(request.id())
            .type(request.type())
            .code(request.code())
            .message("Line edited successfully with id " + request.id())
            .build();
    lineChanged.setId(request.id());
    line.setId(request.id());
    Mockito.when(repository.findById(request.id())).thenReturn(Optional.of(line));
    ArgumentCaptor<Line> lineArgumentCaptor = ArgumentCaptor.forClass(Line.class);
    Mockito.when(repository.updateLine(line)).thenReturn(Optional.of(lineChanged));
    Mockito.when(lineDataMapper.lineToLineResponse(Mockito.eq(lineChanged), Mockito.anyString()))
        .thenReturn(response);

    LineResponse lineResponse = lineService.editLine(request);

    Mockito.verify(repository, Mockito.times(1)).updateLine(line);
    Mockito.verify(repository).updateLine(lineArgumentCaptor.capture());
    assertEquals(lineArgumentCaptor.getValue().getType(), request.type());
    assertEquals(lineArgumentCaptor.getValue().getCode(), request.code());
    assertEquals(lineResponse.id(), response.id());
    assertEquals(lineResponse.type(), response.type());
    assertEquals(lineResponse.code(), response.code());
    assertEquals(lineResponse.message(), response.message());
  }

  @Test
  public void itShouldThrowExceptionWhenLineIsNotFoundForEdit() {
    EditLineRequest request =
        EditLineRequest.builder()
            .id(UUID.randomUUID())
            .type("typeChanged")
            .code("codeChanged")
            .build();
    Mockito.when(repository.findById(request.id())).thenReturn(Optional.empty());
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              lineService.editLine(request);
            });
    assertEquals(DomainException.Reason.RESOURCE_NOT_FOUND, thrown.getReason());
    assertEquals(Line.class, thrown.getDomainClass());
  }

  @Test
  public void itShouldThrowExceptionWhenLineIsNotUpdated() {
    EditLineRequest request =
        EditLineRequest.builder()
            .id(UUID.randomUUID())
            .type("typeChanged")
            .code("codeChanged")
            .build();
    Line line = Line.builder().type(type).code(code).build();
    line.setId(request.id());
    Mockito.when(repository.findById(request.id())).thenReturn(Optional.of(line));
    Mockito.when(repository.updateLine(line)).thenReturn(Optional.empty());
    DomainException thrown =
        assertThrows(
            DomainException.class,
            () -> {
              lineService.editLine(request);
            });
    assertEquals(DomainException.Reason.GENERIC_ERROR, thrown.getReason());
    assertEquals(Line.class, thrown.getDomainClass());
  }
}