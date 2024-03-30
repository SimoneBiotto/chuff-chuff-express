package it.zuppa.chuff.stationService.dto.station;

import it.zuppa.chuff.domain.station.StationType;
import lombok.Builder;
import java.util.UUID;

@Builder
public record StationResponseCompact(UUID id, String code, String name, boolean enable, StationType type) {}
