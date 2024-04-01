package it.zuppa.chuff.scheduleService.dto.schedule;

import it.zuppa.chuff.domain.schedule.ScheduleType;
import jakarta.validation.constraints.AssertTrue;
import java.time.DayOfWeek;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record CreateScheduleRequest(
    @NonNull ScheduleType type, int priceIncreasedPercentageOnFestive, DayOfWeek weekDay) {
  @AssertTrue
  private boolean isValid() {
    List<ScheduleType> listDontRequireOtherInfo =
        List.of(ScheduleType.DAILY, ScheduleType.WEEKEND, ScheduleType.WORKDAY);
    return listDontRequireOtherInfo.contains(this.type)
        || (this.type == ScheduleType.WEEKLY && weekDay != null);
  }

  @Override
  public String toString() {
    return "[type: "
        + type
        + ", priceIncreasedPercentageOnFestive: "
        + priceIncreasedPercentageOnFestive
        + ", weekDay: "
        + weekDay
        + "]";
  }
}
