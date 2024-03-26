package it.zuppa.chuff.trainService.dto.line;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@Builder
public record EditLineRequest(
    @NotNull UUID id, @NotNull @NotBlank String type, @NotNull @NotBlank String code) {}
