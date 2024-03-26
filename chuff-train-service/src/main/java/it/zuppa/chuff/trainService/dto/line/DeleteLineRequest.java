package it.zuppa.chuff.trainService.dto.line;

import jakarta.validation.constraints.AssertTrue;
import java.util.UUID;
import lombok.Builder;

@Builder
public record DeleteLineRequest(UUID id, String type, String code) {
  @AssertTrue
  public boolean isDeleteLineRequestValid() {
    return id != null || (type != null && code != null && !type.isBlank() && !code.isBlank());
  }
}
