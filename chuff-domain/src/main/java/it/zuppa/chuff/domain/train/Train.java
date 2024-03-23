package it.zuppa.chuff.domain.train;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.domain.schedule.Schedule;
import it.zuppa.chuff.domain.station.Station;
import it.zuppa.chuff.domain.trainInstance.TrainInstance;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Train extends BaseEntity {
  @ManyToOne private Station departureStation;
  @ManyToOne private Station arrivalStation;
  @ManyToOne private Line line;
  @ManyToOne private Schedule schedule;

  @OneToMany(mappedBy = "train")
  private List<TrainStop> trainStopList;

  @OneToMany(mappedBy = "train")
  private List<TrainInstance> trainInstanceList;
}
