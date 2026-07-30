package com.algaworks.algashop.ordering.domain.model.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderIsCanceledTest {

    @Test
    void givenAnCanceledOrder_whenTryToCheckStatus_shouldBeTrue() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.CANCELED)
                .build();

        Assertions.assertThat(order.isCanceled()).isTrue();
    }

    @Test
    void givenAnOrder_whenTryToCheckStatus_shouldNotBeTrue() {
        Order order = OrderTestDataBuilder.anOrder().build();

        Assertions.assertThat(order.isCanceled()).isFalse();
    }
}
