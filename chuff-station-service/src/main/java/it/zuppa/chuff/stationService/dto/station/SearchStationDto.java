package it.zuppa.chuff.stationService.dto.station;

import it.zuppa.chuff.valueObject.StationType;
import java.util.UUID;
import lombok.Builder;

@Builder
public record SearchStationDto(
    UUID id, String code, String name, boolean enable, StationType stationType) {}
