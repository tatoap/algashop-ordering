package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderRemoveItemTest {

    @Test
    void givenAnExistingOrderItem_whenTryToRemove_shouldAllow() {
        Order order = OrderTestDataBuilder.anOrder().build();

        OrderItem orderItem = order.items().iterator().next();
        Money total = order.totalAmount();
        Quantity quantity = order.totalItems();

        order.removeItem(orderItem.id());

        Assertions.assertThat(order.items()).doesNotContain(orderItem);
        Assertions.assertThat(order.totalItems()).isNotEqualTo(quantity);
        Assertions.assertThat(order.totalAmount()).isNotEqualTo(total);
    }

    @Test
    void givenAnInexistingOrderItem_whenTryToRemove_shouldAnException() {
        Order order = OrderTestDataBuilder.anOrder().build();

        OrderItem orderItem = OrderItem.brandNew()
                .orderId(order.id())
                .product(ProductTestDataBuilder.aProductAltRamMemory().build())
                .quantity(new Quantity(1))
                .build();

        Assertions.assertThatExceptionOfType(OrderDoesNotContainOrderItemException.class)
                .isThrownBy(() -> order.removeItem(orderItem.id()));
    }

    @Test
    void givenAnPlacedOrder_whenTryToRemoveOrderItem_shouldNotAllow() {
        Order order = OrderTestDataBuilder.anOrder().build();

        order.place();

        OrderItem orderItem = order.items().iterator().next();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.removeItem(orderItem.id()));
    }
}
