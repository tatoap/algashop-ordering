package com.algaworks.algashop.ordering.domain.model.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderCancelTest {

    @Test
    void givenAnOrderNotCanceled_whenTryToCancel_shouldAllow() {
        Order order = OrderTestDataBuilder.anOrder().build();

        order.cancel();

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.isCanceled()).isTrue(),
                o -> Assertions.assertThat(o.canceledAt()).isNotNull()
        );
    }

    @Test
    void givenAnOrderCanceled_whenTryToCancel_shouldGenerateException() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.CANCELED)
                .build();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> order.cancel());

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.status()).isEqualTo(OrderStatus.CANCELED),
                o -> Assertions.assertThat(o.canceledAt()).isEqualTo(order.canceledAt()));
    }
}
