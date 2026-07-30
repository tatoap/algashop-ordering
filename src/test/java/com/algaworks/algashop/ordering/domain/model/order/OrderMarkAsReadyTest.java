package com.algaworks.algashop.ordering.domain.model.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderMarkAsReadyTest {

    @Test
    void givenAnPaidOrder_whenTryToChangeStatusToReady_shouldAllow() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.PAID)
                .build();

        order.markAsReady();

        Assertions.assertThat(order.readyAt()).isNotNull();
    }

    @Test
    void givenAnOrderNotPaid_whenTryToChangeStatusToReady_shouldNotAllow() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.PLACED)
                .build();

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> order.markAsReady());

        Assertions.assertWith(order,
                o -> Assertions.assertThat(o.status()).isEqualTo(order.status()),
                o -> Assertions.assertThat(o.readyAt()).isNull()
        );
    }
}
