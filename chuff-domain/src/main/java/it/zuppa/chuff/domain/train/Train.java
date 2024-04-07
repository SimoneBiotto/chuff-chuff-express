package it.zuppa.chuff.domain.train;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.domain.schedule.Schedule;
import it.zuppa.chuff.domain.station.Station;
import it.zuppa.chuff.domain.trainInstance.TrainInstance;
import it.zuppa.chuff.exception.DomainException;
import it.zuppa.chuff.valueObject.TrainSchedule;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Train extends BaseEntity {
  @NonNull @ManyToOne private Station departureStation;
  @NonNull @ManyToOne private Station arrivalStation;
  @NonNull @ManyToOne private Line line;
  @ManyToOne private Schedule schedule;
  @NonNull private TrainSchedule trainSchedule;
  @Builder.Default private boolean enabled = true;

  @OneToMany(mappedBy = "train", orphanRemoval = true)
  private List<TrainStop> trainStopList;

  @OneToMany(mappedBy = "train")
  private List<TrainInstance> trainInstanceList;

  public static Train create(
      Station departureStation,
      Station arrivalStation,
      Line line,
      TrainSchedule trainSchedule,
      List<TrainStop> trainStopList,
      Schedule schedule)
      throws DomainException {
    List<String> messageError =
        validate(departureStation, arrivalStation, line, trainSchedule, trainStopList);
    if (!messageError.isEmpty())
      throw new DomainException(
          "Error creating train entity, cause: " + String.join(", ", messageError),
          DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION,
          Train.class);
    return Train.builder()
        .departureStation(departureStation)
        .arrivalStation(arrivalStation)
        .line(line)
        .trainSchedule(trainSchedule)
        .trainStopList(trainStopList)
        .schedule(schedule)
        .build();
  }

  public static Train create(
      Station departureStation,
      Station arrivalStation,
      Line line,
      TrainSchedule trainSchedule,
      List<TrainStop> trainStopList)
      throws DomainException {
    return create(departureStation, arrivalStation, line, trainSchedule, trainStopList, null);
  }

  public static List<String> validate(
      Station departureStation,
      Station arrivalStation,
      Line line,
      TrainSchedule trainSchedule,
      List<TrainStop> trainStopList) {
    List<String> errorMessage = new ArrayList<>();
    if (departureStation == null) errorMessage.add("departureStation is required");
    if (arrivalStation == null) errorMessage.add("arrivalStation is required");
    if (line == null) errorMessage.add("line is required");
    if (trainSchedule == null) errorMessage.add("trainSchedule is required");
    if (trainStopList == null || trainStopList.isEmpty())
      errorMessage.add("trainStopList is required");
    return errorMessage;
  }
}
