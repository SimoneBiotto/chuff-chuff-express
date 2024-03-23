package it.zuppa.chuff.domain.order;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.domain.ticket.Ticket;
import it.zuppa.chuff.domain.user.Costumer;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Order extends BaseEntity {
    @OneToMany(mappedBy = "order")
    private List<Ticket> ticketList;
    @ManyToOne
    private Costumer costumer;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
