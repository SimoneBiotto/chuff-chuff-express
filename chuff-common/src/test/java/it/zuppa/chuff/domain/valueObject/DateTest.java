package it.zuppa.chuff.domain.valueObject;

import it.zuppa.chuff.common.valueObject.Date;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateTest {
    @Test
    public void itShouldReturnTrueWhenDateIsGreaterThanOther() {
        Date date = new Date(LocalDate.now());
        Date dateAfter = new Date(LocalDate.now().plusDays(1));
        assertTrue(dateAfter.isAfter(date));
    }

    @Test
    public void itShouldReturnDatePlusDays() {
        Date date = new Date(LocalDate.now());
        Date datePlus = date.plusDays(1);
        assertEquals(date.getDate().plusDays(1), datePlus.getDate());
    }

    @Test
    public void itShouldReturnDateMinusDays() {
        Date date = new Date(LocalDate.now());
        Date datePlus = date.minusDays(1);
        assertEquals(date.getDate().minusDays(1), datePlus.getDate());
    }
}
