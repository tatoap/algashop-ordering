package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.product.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.product.Product;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderChangingTest {

    @Test
    void givenPlacedOrder_whenTryToChange_shouldNotAllow() {
        Order order = OrderTestDataBuilder.anOrder().build();

        order.place();

        Product product = ProductTestDataBuilder.aProductAltMousePad().build();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.addItem(product, new Quantity(1)));
    }

    @Test
    void givenPlacedOrder_whenTryToChangeShipping_shouldNotAllow() {
        Order order = OrderTestDataBuilder.anOrder().build();

        order.place();

        Shipping shipping = OrderTestDataBuilder.aShipping().toBuilder().build();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changeShipping(shipping));
    }

    @Test
    void givenPlacedOrder_whenTryToChangeBilling_shouldNotAllow() {
        Order order = OrderTestDataBuilder.anOrder().build();

        order.place();

        Billing billing = OrderTestDataBuilder.aBilling();

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changeBilling(billing));
    }

    @Test
    void givenPlacedOrder_whenTryToChangePaymentMethod_shouldNotAllow() {
        Order order = OrderTestDataBuilder.anOrder().build();

        order.place();

        PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;

        Assertions.assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.changePaymentMethod(paymentMethod));
    }
}
