package it.zuppa.chuff.common.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntity {
  @Id private UUID id;

  public UUID getId() {
    return id;
  }

  public void setId(UUID uuid) {
    id = uuid;
  }
}
