package it.zuppa.chuff.valueObject;

import it.zuppa.chuff.common.valueObject.Date;
import it.zuppa.chuff.common.valueObject.Time;
import it.zuppa.chuff.domain.train.Train;
import it.zuppa.chuff.exception.DomainException;
import jakarta.persistence.Embeddable;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainSchedule {
  @NonNull private Time startTime;
  @NonNull private Time endTime;
  @NonNull private Time repetition;
  private Date startRecurrence;
  private Date endRecurrence;

  public static TrainSchedule create(Time startTime, Time endTime, Time repetition)
      throws DomainException {
    return create(startTime, endTime, repetition, null, null);
  }

  public static TrainSchedule create(
      Time startTime, Time endTime, Time repetition, Date startRecurrence, Date endRecurrence)
      throws DomainException {
    List<String> errorMessage =
        validate(startTime, endTime, repetition, startRecurrence, endRecurrence);
    if (!errorMessage.isEmpty())
      throw new DomainException(
          String.join("; ", errorMessage),
          DomainException.Reason.DOMAIN_CONSTRAINT_VIOLATION,
          Train.class);
    return TrainSchedule.builder()
        .startTime(startTime)
        .endTime(endTime)
        .repetition(repetition)
        .startRecurrence(startRecurrence)
        .endRecurrence(endRecurrence)
        .build();
  }

  public static List<String> validate(
      Time startTime, Time endTime, Time repetition, Date startRecurrence, Date endRecurrence) {
    List<String> errorMessage = new ArrayList<>();
    if (startTime == null) errorMessage.add("startTime is required");
    if (endTime == null) errorMessage.add("endTime is required");
    if (repetition == null) errorMessage.add("repetition is required");
    if (startTime != null && endTime != null && startTime.isAfter(endTime))
      errorMessage.add("startTime must be before endTime");
    if (endRecurrence != null && startRecurrence == null)
      errorMessage.add("startRecurrence is required when endRecurrence is present");
    if (startRecurrence != null && endRecurrence != null && startRecurrence.isAfter(endRecurrence))
      errorMessage.add("startRecurrence must be before endRecurrence");
    return errorMessage;
  }
}
