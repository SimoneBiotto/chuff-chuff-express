package it.zuppa.chuff.stationService.dto.station;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateStationRequest(@NotNull @NotBlank String code, @NotNull @NotBlank String name) {}
