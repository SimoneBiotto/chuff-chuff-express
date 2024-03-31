package it.zuppa.chuff.stationService.dto.station;

import it.zuppa.chuff.domain.station.StationType;
import java.util.UUID;
import lombok.Builder;

@Builder
public record StationCompactResponse(
    UUID id, String code, String name, boolean enable, StationType type) {}
