package it.zuppa.chuff.domain.carriage;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.domain.schedule.Schedule;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CarriageSchedule extends BaseEntity {
    @ManyToOne
    private Schedule schedule;
    @ManyToOne
    private Carriage carriage;
    private double multiplierPrice;
}
