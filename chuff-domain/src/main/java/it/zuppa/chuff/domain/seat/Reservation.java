package it.zuppa.chuff.domain.seat;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.domain.station.Station;
import it.zuppa.chuff.domain.ticket.Ticket;
import it.zuppa.chuff.valueObject.ReservationStatus;
import jakarta.persistence.*;

@Entity
public class Reservation extends BaseEntity {
  @ManyToOne private Seat seat;
  @ManyToOne private Station departureStation;
  @ManyToOne private Station arrivalStation;
  @OneToOne private Ticket ticket;

  @Enumerated(EnumType.STRING)
  private ReservationStatus status;
}
