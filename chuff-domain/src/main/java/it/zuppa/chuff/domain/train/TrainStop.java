package it.zuppa.chuff.domain.train;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.common.valueObject.Time;
import it.zuppa.chuff.domain.station.Station;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class TrainStop extends BaseEntity {
    @ManyToOne
    private Train train;
    @ManyToOne
    private Station station;
    @Embedded
    private Time arrivalTime;
    @Embedded
    private Time departureTime;
}
