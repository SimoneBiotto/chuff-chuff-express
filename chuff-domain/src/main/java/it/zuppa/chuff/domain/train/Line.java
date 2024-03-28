package it.zuppa.chuff.domain.train;

import it.zuppa.chuff.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UniqueTypeAndCode",
          columnNames = {"type", "code"})
    })
public class Line extends BaseEntity {
  private String type;
  private String code;

  @OneToMany(mappedBy = "line")
  private List<Train> trainList;

  public String getLine() {
    return type + code;
  }
}
