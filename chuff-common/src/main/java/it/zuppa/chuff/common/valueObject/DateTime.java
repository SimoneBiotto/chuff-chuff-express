package it.zuppa.chuff.common.valueObject;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import java.time.*;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class DateTime {
  private ZonedDateTime zonedDateTime;

  public DateTime() {
    this.zonedDateTime = ZonedDateTime.now();
  }

  public DateTime(Date date, Time time, ZoneOffset zoneOffset) {
    setZonedDateTime(ZonedDateTime.of(date.getDate(), time.getTime(), zoneOffset));
  }

  public DateTime(Date date, Time time) {
    this(date, time, ZoneOffset.UTC);
  }

  public DateTime(Date date) {
    this.zonedDateTime = date.getDate().atStartOfDay(ZoneOffset.UTC);
  }

  public DateTime(Time time) {
    this(new Date(), time, ZoneOffset.UTC);
  }

  public DateTime(ZonedDateTime zonedDateTime) {
    setZonedDateTime(zonedDateTime);
  }

  public ZonedDateTime getZonedDateTime() {
    return zonedDateTime;
  }

  public void setZonedDateTime(ZonedDateTime zonedDateTime) {
    this.zonedDateTime = zonedDateTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DateTime dateTime = (DateTime) o;
    return Objects.equals(zonedDateTime, dateTime.zonedDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zonedDateTime);
  }

  public LocalDateTime getLocalDateTime() {
    return zonedDateTime.toLocalDateTime();
  }
}
