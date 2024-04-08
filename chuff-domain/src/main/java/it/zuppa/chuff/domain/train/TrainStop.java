package it.zuppa.chuff.domain.train;

import it.zuppa.chuff.common.entity.BaseEntity;
import it.zuppa.chuff.common.valueObject.Time;
import it.zuppa.chuff.domain.station.Station;
import it.zuppa.chuff.exception.DomainException;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainStop extends BaseEntity {
  @ManyToOne private Train train;
  @NonNull @ManyToOne private Station station;
  @NonNull @Embedded private Time arrivalTime;
  @NonNull @Embedded private Time departureTime;

  public static List<String> validate(Station station, Time arrivalTime, Time departureTime) {
    List<String> errorMessage = new ArrayList<>();
    if (station == null) errorMessage.add("station is required");
    if (arrivalTime == null) errorMessage.add("arrivalTime is required");
    if (departureTime == null) errorMessage.add("departureTime is required");
    if (arrivalTime != null && departureTime != null && arrivalTime.isAfter(departureTime))
      errorMessage.add("arrivalTime must be before departureTime");
    return errorMessage;
  }

  public static TrainStop create(Station station, Time arrivalTime, Time departureTime)
      throws DomainException {
    List<String> messageError = validate(station, arrivalTime, departureTime);
    if (!messageError.isEmpty())
      throw new DomainException(
          "Error creating train stop entity, cause: " + String.join(", ", messageError),
          DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION,
          TrainStop.class);
    return TrainStop.builder()
        .station(station)
        .arrivalTime(arrivalTime)
        .departureTime(departureTime)
        .build();
  }
}
