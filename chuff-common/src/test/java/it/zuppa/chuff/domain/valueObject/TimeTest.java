package it.zuppa.chuff.domain.valueObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.zuppa.chuff.common.valueObject.Time;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

public class TimeTest {
  @Test
  public void itShouldReturnTrueWhenTimeIsGreaterThanOther() {
    Time time = new Time(LocalTime.now());
    Time timeAfter = new Time(LocalTime.now().plusSeconds(1));
    assertTrue(timeAfter.isAfter(time));
  }

  @Test
  public void itShouldReturnTimePlusHours() {
    Time time = new Time(LocalTime.now());
    Time timeAfter = time.plusHours(1);
    assertEquals(time.getTime().plusHours(1), timeAfter.getTime());
  }

  @Test
  public void itShouldReturnTimeMinusHours() {
    Time time = new Time(LocalTime.now());
    Time timeAfter = time.minusHours(1);
    assertEquals(time.getTime().minusHours(1), timeAfter.getTime());
  }

  @Test
  public void itShouldReturnTimePlusMinutes() {
    Time time = new Time(LocalTime.now());
    Time timeAfter = time.plusMinutes(1);
    assertEquals(time.getTime().plusMinutes(1), timeAfter.getTime());
  }

  @Test
  public void itShouldReturnTimeMinusMinutes() {
    Time time = new Time(LocalTime.now());
    Time timeAfter = time.minusMinutes(1);
    assertEquals(time.getTime().minusMinutes(1), timeAfter.getTime());
  }

  @Test
  public void itShouldReturnTimePlusSeconds() {
    Time time = new Time(LocalTime.now());
    Time timeAfter = time.plusSeconds(1);
    assertEquals(time.getTime().plusSeconds(1), timeAfter.getTime());
  }

  @Test
  public void itShouldReturnTimeMinusSeconds() {
    Time time = new Time(LocalTime.now());
    Time timeAfter = time.minusSeconds(1);
    assertEquals(time.getTime().minusSeconds(1), timeAfter.getTime());
  }
}
