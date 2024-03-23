package it.zuppa.chuff.domain.trainInstance;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.common.valueObject.Money;
import it.zuppa.chuff.domain.carriage.Carriage;
import it.zuppa.chuff.domain.seat.Seat;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class InstanceCarriage extends BaseEntity {
  @ManyToOne private Carriage carriage;
  @ManyToOne private TrainInstance trainInstance;
  @Embedded private Money price;
  private String code;

  @OneToMany(mappedBy = "instanceCarriage")
  private List<Seat> seatList;
}
