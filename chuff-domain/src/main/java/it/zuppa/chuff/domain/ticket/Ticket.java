package it.zuppa.chuff.domain.ticket;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.common.valueObject.Money;
import it.zuppa.chuff.domain.costumer.Costumer;
import it.zuppa.chuff.domain.order.Order;
import it.zuppa.chuff.domain.seat.Reservation;
import it.zuppa.chuff.valueObject.TicketStatus;
import jakarta.persistence.*;

@Entity
public class Ticket extends BaseEntity {
  @Embedded private Money price;
  @OneToOne private Reservation reservation;

  @Enumerated(EnumType.STRING)
  private TicketStatus status;

  @ManyToOne private Costumer costumer;
  @ManyToOne private Order order;
}
