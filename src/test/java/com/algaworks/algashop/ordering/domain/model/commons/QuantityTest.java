package com.algaworks.algashop.ordering.domain.model.commons;

import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class QuantityTest {

    private static final Quantity VALUE = new Quantity(10);

    @Test
    void shouldNotCreateQuantityWithNegativeValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Quantity(-1));
    }

    @Test
    void shoultNotAddNullValue() {
        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> new Quantity(null));
    }

    @Test
    void shouldAddValue() {
        Assertions.assertThat(VALUE.add(new Quantity(10))).isEqualTo(new Quantity(20));
    }

}