package it.zuppa.chuff.domain.valueObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.zuppa.chuff.common.valueObject.Date;
import it.zuppa.chuff.common.valueObject.DateTime;
import it.zuppa.chuff.common.valueObject.Time;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

public class DateTimeTest {
  private final Date date = new Date();
  private final Time time = new Time();

  @Test
  public void itShouldReturnALocalDateTime() {
    LocalDateTime dateTime = LocalDateTime.of(date.getDate(), time.getTime());
    assertEquals(dateTime, new DateTime(date, time).getLocalDateTime());
  }

  @Test
  public void itShouldReturnTrueWhenDateTimeIsGreaterThanOther() {
    ZonedDateTime zonedDateTime = ZonedDateTime.now();
    DateTime dateTime = new DateTime(zonedDateTime);
    DateTime dateTimeAfter = new DateTime(zonedDateTime.plusHours(1));
    assertTrue(dateTimeAfter.isAfter(dateTime));
  }

  @Test
  public void itShouldReturnDateTimePlusDays() {
    ZonedDateTime zonedDateTime = ZonedDateTime.now();
    DateTime dateTime = new DateTime(zonedDateTime);
    DateTime dateTimeAfter = dateTime.plusDays(1);
    assertEquals(zonedDateTime.plusDays(1), dateTimeAfter.getZonedDateTime());
  }

  @Test
  public void itShouldReturnDateTimeMinusDays() {
    ZonedDateTime zonedDateTime = ZonedDateTime.now();
    DateTime dateTime = new DateTime(zonedDateTime);
    DateTime dateTimeBefore = dateTime.minusDays(1);
    assertEquals(zonedDateTime.minusDays(1), dateTimeBefore.getZonedDateTime());
  }

  @Test
  public void itShouldReturnDateTimePlusHours() {
    ZonedDateTime zonedDateTime = ZonedDateTime.now();
    DateTime dateTime = new DateTime(zonedDateTime);
    DateTime dateTimeAfter = dateTime.plusHours(1);
    assertEquals(zonedDateTime.plusHours(1), dateTimeAfter.getZonedDateTime());
  }

  @Test
  public void itShouldReturnDateTimeMinusHours() {
    ZonedDateTime zonedDateTime = ZonedDateTime.now();
    DateTime dateTime = new DateTime(zonedDateTime);
    DateTime dateTimeBefore = dateTime.minusHours(1);
    assertEquals(zonedDateTime.minusHours(1), dateTimeBefore.getZonedDateTime());
  }

    @Test
    public void itShouldReturnDateTimePlusMinutes() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        DateTime dateTime = new DateTime(zonedDateTime);
        DateTime dateTimeAfter = dateTime.plusMinutes(1);
        assertEquals(zonedDateTime.plusMinutes(1), dateTimeAfter.getZonedDateTime());
    }

    @Test
    public void itShouldReturnDateTimeMinusMinutes() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        DateTime dateTime = new DateTime(zonedDateTime);
        DateTime dateTimeBefore = dateTime.minusMinutes(1);
        assertEquals(zonedDateTime.minusMinutes(1), dateTimeBefore.getZonedDateTime());
    }

    @Test
    public void itShouldReturnDateTimePlusSeconds() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        DateTime dateTime = new DateTime(zonedDateTime);
        DateTime dateTimeAfter = dateTime.plusSeconds(1);
        assertEquals(zonedDateTime.plusSeconds(1), dateTimeAfter.getZonedDateTime());
    }

    @Test
    public void itShouldReturnDateTimeMinusSeconds() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        DateTime dateTime = new DateTime(zonedDateTime);
        DateTime dateTimeBefore = dateTime.minusSeconds(1);
        assertEquals(zonedDateTime.minusSeconds(1), dateTimeBefore.getZonedDateTime());
    }

}
