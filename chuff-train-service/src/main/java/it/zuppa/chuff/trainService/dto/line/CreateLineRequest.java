package it.zuppa.chuff.trainService.dto.line;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateLineRequest(@NotNull @NotBlank String type, @NotNull @NotBlank String code) {}
