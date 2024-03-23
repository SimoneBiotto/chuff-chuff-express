package it.zuppa.chuff.domain.trainInstance;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.common.valueObject.DateTime;
import it.zuppa.chuff.common.valueObject.Time;
import it.zuppa.chuff.domain.train.Train;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class TrainInstance extends BaseEntity {
    @OneToMany(mappedBy = "trainInstance")
    private List<TrainInstancePosition> trainInstancePositionList;
    @Embedded
    private DateTime departureTime;
    @Embedded
    private DateTime arrivalTime;
    @Embedded
    private Time delay;
    @ManyToOne
    private Train train;
    @Enumerated(EnumType.STRING)
    private TrainInstanceStatus trainInstanceStatus;
    @OneToMany(mappedBy = "trainInstance")
    private List<InstanceCarriage> instanceCarriageList;

}
