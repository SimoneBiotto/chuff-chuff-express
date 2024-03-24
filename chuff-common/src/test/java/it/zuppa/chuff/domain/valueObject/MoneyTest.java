package it.zuppa.chuff.domain.valueObject;

import static org.junit.jupiter.api.Assertions.*;

import it.zuppa.chuff.common.valueObject.Money;
import org.junit.jupiter.api.Test;

public class MoneyTest {

  private final int moneyAmount1 = 10;
  private final int moneyAmount2 = 15;

  @Test
  public void itShouldReturnAsString() {
    assertEquals(Integer.toString(moneyAmount1), new Money(moneyAmount1).toString());
  }

  @Test
  public void itShouldCompare() {
    Money money1 = new Money(moneyAmount1);
    Money money2 = new Money(moneyAmount2);
    assertTrue(money2.isGreaterThan(money1));
    assertTrue(money2.isGreaterThanOrEqual(money1));
    assertEquals(money1, new Money(moneyAmount1));
    assertNotEquals(money1, money2);
    assertFalse(money1.isGreaterThanOrEqual(money2));
  }

  @Test
  public void itShouldAdd() {
    Money moneyResult = new Money(moneyAmount1 + moneyAmount2);
    Money money = new Money(moneyAmount1);
    Money delta = new Money(moneyAmount2);
    assertEquals(moneyResult, money.add(delta));
  }

  @Test
  public void itShouldMultiply() {
    int multiplier = 12;
    Money money = new Money(moneyAmount2);
    Money moneyResult = new Money(moneyAmount2 * multiplier);
    assertEquals(moneyResult, money.multiply(multiplier));
  }

  @Test
  public void itShouldSubtract() {
    Money moneyResult = new Money(moneyAmount2 - moneyAmount1);
    Money money = new Money(moneyAmount2);
    Money delta = new Money(moneyAmount1);
    assertEquals(moneyResult, money.subtract(delta));
  }

  @Test
  public void itShouldDivide() {
    int divisor = 2;
    Money money1 = new Money(moneyAmount1);
    Money money2 = new Money(moneyAmount2);
    Money moneyResult1 = new Money(moneyAmount1 / divisor);
    Money moneyResult2 = new Money((moneyAmount2 + 1) / divisor);
    assertEquals(moneyResult1, money1.divide(divisor));
    assertEquals(moneyResult2, money2.divide(divisor));
  }
}
