package it.zuppa.chuff.domain.seat;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.domain.trainInstance.InstanceCarriage;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Seat extends BaseEntity {
  private String code;

  @OneToMany(mappedBy = "seat")
  private List<Reservation> reservationList;

  @ManyToOne private InstanceCarriage instanceCarriage;
}
