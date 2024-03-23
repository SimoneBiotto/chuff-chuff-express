package it.zuppa.chuff.domain.schedule;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.common.valueObject.DateTime;
import it.zuppa.chuff.domain.carriage.Carriage;
import it.zuppa.chuff.domain.train.Train;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Schedule extends BaseEntity {
  @Embedded private DateTime start;
  @Embedded private DateTime end;

  @OneToMany(mappedBy = "schedule")
  private List<Train> trainList;

  @OneToMany(mappedBy = "schedule")
  private List<Schedule> scheduleList;

  @OneToMany(mappedBy = "carriage")
  private List<Carriage> carriageList;
}
