package it.zuppa.chuff.stationService.dto.station;

import it.zuppa.chuff.valueObject.StationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;

@Builder
public record EditStationRequest(
    @NotNull UUID id,
    @NotNull @NotBlank String code,
    @NotNull @NotBlank String name,
    boolean enable,
    @NotNull StationType stationType) {}
