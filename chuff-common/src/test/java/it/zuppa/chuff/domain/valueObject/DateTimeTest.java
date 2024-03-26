package it.zuppa.chuff.domain.valueObject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.zuppa.chuff.common.valueObject.Date;
import it.zuppa.chuff.common.valueObject.DateTime;
import it.zuppa.chuff.common.valueObject.Time;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class DateTimeTest {
  private final Date date = new Date();
  private final Time time = new Time();

  @Test
  public void itShouldReturnALocalDateTime() {
    LocalDateTime dateTime = LocalDateTime.of(date.getDate(), time.getTime());
    assertEquals(dateTime, new DateTime(date, time).getLocalDateTime());
  }
}
