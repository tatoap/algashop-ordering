package com.algaworks.algashop.ordering.domain.model.commons;

import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MoneyTest {

    private static final Money VALUE = new Money("10");

    @Test
    void shouldNotAddNegativeValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Money("-1"));
    }

    @Test
    void shouldMultiplyValue() {
        Assertions.assertThat(VALUE.multiply(new Quantity(2))).isEqualTo(new Money("20.00"));
    }

    @Test
    void shouldAddValue() {
        Assertions.assertThat(VALUE.add(new Money("2"))).isEqualTo(new Money("12.00"));
    }

    @Test
    void shouldDivideValue() {
        Assertions.assertThat(VALUE.divide(new Money("2"))).isEqualTo(new Money("5.00"));
    }

    @Test
    void shouldNotMultiplyZeroValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> VALUE.multiply(new Quantity(0)));
    }

    @Test
    void shouldCompareValue() {
        Assertions.assertThat(VALUE.compareTo(new Money("10"))).isEqualTo(0);
    }
}