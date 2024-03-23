package it.zuppa.chuff.domain.trainInstance;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.common.valueObject.DateTime;
import it.zuppa.chuff.domain.station.Station;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrainInstancePosition extends BaseEntity {
  @Embedded private DateTime exceptedArrivalTime;
  @Embedded private DateTime exceptedDepartureTime;
  @Embedded private DateTime realArrivalTime;
  @Embedded private DateTime realDepartureTime;
  @ManyToOne private TrainInstance trainInstance;
  @ManyToOne private Station station;
}
