package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.order.OrderStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @Test
    void canChangeTo() {
        Assertions.assertThat(OrderStatus.DRAFT.canChangeTo(OrderStatus.PLACED)).isTrue();
        Assertions.assertThat(OrderStatus.PLACED.canChangeTo(OrderStatus.CANCELED)).isTrue();
        Assertions.assertThat(OrderStatus.READY.canChangeTo(OrderStatus.PLACED)).isFalse();
    }

    @Test
    void canNotChangeTo() {
        Assertions.assertThat(OrderStatus.DRAFT.canNotChangeTo(OrderStatus.READY)).isTrue();
        Assertions.assertThat(OrderStatus.CANCELED.canNotChangeTo(OrderStatus.DRAFT)).isTrue();
    }
}