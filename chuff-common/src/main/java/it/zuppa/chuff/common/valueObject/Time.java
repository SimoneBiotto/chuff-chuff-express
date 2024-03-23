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

  public Time() {
    setTime(LocalTime.now());
  }
}
