package com.food.ordering.system.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {

    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    /*********************************************************
     *   Método para verificar que el valor es mayor que "0"
     *********************************************************/
    public boolean isGreaterThanZero() {
        return  this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /*********************************************************
     *   Método para verificar que el valor es menor que "0"
     *********************************************************/
    public boolean isGreaterThan(Money money) {
        return this.amount != null && this.amount.compareTo(money.getAmount()) < 0;
    }

    /*********************************************************
     *   Método para hacer operaciones de suma
     *********************************************************/
    public Money add(Money money) {
        return new Money(this.setScale(this.amount.add(money.getAmount())));
    }

    /*********************************************************
     *   Método para hacer operaciones de resta
     *********************************************************/
    public Money subtract(Money money) {
        return new Money(this.setScale(this.amount.subtract(money.getAmount())));
    }

    /*********************************************************
     *   Método para hacer operaciones de multiplicación
     *********************************************************/
    public Money multiply(int multiplier) {
        return  new Money(this.setScale(this.amount.multiply(new BigDecimal(multiplier))));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    /*********************************************************
     *   Método para redondear los decimales
     * 2.5 -> 2
     * 1.6 -> 2
     * 1.1 -> 1
     * 1.0 -> 1
     * -1.0 -> -1
     * -1.1 -> -1
     * -1.6 -> -2
     * -2.5 -> -2
     * -5.5 -> -6
     *********************************************************/
    public BigDecimal setScale(BigDecimal input) {
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }
}
