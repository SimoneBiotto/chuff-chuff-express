package it.zuppa.chuff.common.valueObject;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Date {
  private LocalDate date;

  public Date() {
    setDate(LocalDate.now());
  }

  public Date(LocalDate date) {
    setDate(date);
  }

  public LocalDate getDate() {
    return date;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Date date1 = (Date) o;
    return Objects.equals(date, date1.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date);
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public boolean isAfter(Date other) {
    return this.getDate().isAfter(other.getDate());
  }

  public Date plusDays(long days) {
    return new Date(date.plusDays(days));
  }

  public Date minusDays(long days) {
    return new Date(date.minusDays(days));
  }
}
