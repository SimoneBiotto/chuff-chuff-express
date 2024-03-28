package it.zuppa.chuff.trainService.dto.line;

import java.util.UUID;
import lombok.Builder;

@Builder
public record LineResponse(UUID id, String type, String code) {}
