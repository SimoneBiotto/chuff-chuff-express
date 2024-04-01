package it.zuppa.chuff.domain.schedule;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.domain.carriage.Carriage;
import it.zuppa.chuff.domain.train.Train;
import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.util.List;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule extends BaseEntity {
  private int priceIncreasedPercentageOnFestive;

  @Enumerated(EnumType.STRING)
  @NonNull
  private ScheduleType type;

  private DayOfWeek weekDay;
  @Builder.Default private boolean enabled = true;

  @OneToMany(mappedBy = "schedule")
  private List<Train> trainList;

  @OneToMany(mappedBy = "carriage")
  private List<Carriage> carriageList;

  public boolean isValid() {
    List<ScheduleType> listDontRequireOtherInfo =
        List.of(ScheduleType.DAILY, ScheduleType.WEEKEND, ScheduleType.WORKDAY);
    return listDontRequireOtherInfo.contains(this.type)
        || (this.type == ScheduleType.WEEKLY && weekDay != null);
  }
}
