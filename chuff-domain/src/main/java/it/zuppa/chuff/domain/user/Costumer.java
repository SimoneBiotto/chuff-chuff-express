package it.zuppa.chuff.domain.user;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.domain.order.Order;
import it.zuppa.chuff.domain.ticket.Ticket;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Costumer extends BaseEntity {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;
    private String zipCode;
    private String fiscalCode;
    @OneToMany(mappedBy = "costumer")
    private List<Ticket> ticketList;
    @OneToMany(mappedBy = "costumer")
    private List<Order> orderList;
}
