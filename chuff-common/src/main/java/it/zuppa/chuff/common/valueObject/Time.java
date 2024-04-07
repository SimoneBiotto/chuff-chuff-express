package it.zuppa.chuff.common.valueObject;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import java.time.LocalTime;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Time {
  private LocalTime time;

  public Time(LocalTime time) {
    setTime(time);
  }

  public Time() {
    setTime(LocalTime.now());
  }

  @Override
  public String toString() {
    return time.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Time time1 = (Time) o;
    return Objects.equals(time, time1.time);
  }

  @Override
  public int hashCode() {
    return Objects.hash(time);
  }

  public LocalTime getTime() {
    return time;
  }

  public void setTime(LocalTime time) {
    this.time = time;
  }

  public boolean isAfter(Time other) {
    return this.getTime().isAfter(other.getTime());
  }

  public Time plusHours(long hours) {
    return new Time(time.plusHours(hours));
  }

public Time minusHours(long hours) {
    return new Time(time.minusHours(hours));
  }

    public Time plusMinutes(long minutes) {
        return new Time(time.plusMinutes(minutes));
    }

    public Time minusMinutes(long minutes) {
        return new Time(time.minusMinutes(minutes));
    }

    public Time plusSeconds(long seconds) {
        return new Time(time.plusSeconds(seconds));
    }

    public Time minusSeconds(long seconds) {
        return new Time(time.minusSeconds(seconds));
    }
}
