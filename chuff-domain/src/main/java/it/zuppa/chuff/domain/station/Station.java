package it.zuppa.chuff.domain.station;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.domain.train.Train;
import it.zuppa.chuff.domain.train.TrainStop;
import it.zuppa.chuff.domain.trainInstance.TrainInstancePosition;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Station extends BaseEntity {
  private String code;
  private String name;

  @OneToMany(mappedBy = "departureStation")
  private List<Train> departureTrainList;

  @OneToMany(mappedBy = "arrivalStation")
  private List<Train> arrivalTrainList;

  @OneToMany(mappedBy = "station")
  private List<TrainStop> trainStopList;

  @OneToMany(mappedBy = "station")
  private List<TrainInstancePosition> trainInstancePositions;
}
