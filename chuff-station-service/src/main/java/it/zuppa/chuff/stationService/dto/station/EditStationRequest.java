package it.zuppa.chuff.stationService.dto.station;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record EditStationRequest(@NotNull UUID id, @NotNull @NotBlank String code, @NotNull @NotBlank String name) {}
