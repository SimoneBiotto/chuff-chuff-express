package it.zuppa.chuff.domain.train;

import it.zuppa.chuff.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Line extends BaseEntity {
  private String type;
  private String code;

  @OneToMany(mappedBy = "line")
  private List<Train> trainList;
}
