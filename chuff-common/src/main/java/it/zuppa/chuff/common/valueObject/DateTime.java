package it.zuppa.chuff.common.valueObject;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class DateTime {
  private ZonedDateTime zonedDateTime;

  public DateTime() {
    this.zonedDateTime = ZonedDateTime.now();
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
}
