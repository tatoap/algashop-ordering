package com.algaworks.algashop.ordering.domain.model.customer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class BirthDateTest {

    @Test
    void shouldNotAcceptNullDate() {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> new BirthDate(null));
    }

    @Test
    void shouldNotAcceptBirthDateIsAfterNowDate() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new BirthDate(LocalDate.now().plusDays(1)));
    }
}