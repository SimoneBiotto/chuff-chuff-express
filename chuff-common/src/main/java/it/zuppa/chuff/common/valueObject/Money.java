package it.zuppa.chuff.common.valueObject;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Money {
  private BigDecimal amount;

  protected Money() {}

  public Money(BigDecimal amount) {
    this.amount = amount;
  }

  public Money(int amountInteger) {
    this.amount = new BigDecimal(amountInteger);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Money money = (Money) o;
    return Objects.equals(amount, money.amount);
  }

  public boolean isGreaterThan(Money other) {
    return this.amount.compareTo(other.amount) > 0;
  }

  public boolean isGreaterThanOrEqual(Money other) {
    return this.amount.compareTo(other.amount) >= 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount);
  }

  @Override
  public String toString() {
    return amount.toPlainString();
  }

  public Money add(Money delta) {
    return new Money(this.amount.add(delta.amount));
  }

  public Money subtract(Money delta) {
    return new Money(this.amount.subtract(delta.amount));
  }

  public Money multiply(Integer multiplier) {
    return new Money(this.amount.multiply(new BigDecimal(multiplier)));
  }

  public Money divide(Integer divisor) {
    return new Money(this.amount.divide(new BigDecimal(divisor), RoundingMode.HALF_UP));
  }
}
