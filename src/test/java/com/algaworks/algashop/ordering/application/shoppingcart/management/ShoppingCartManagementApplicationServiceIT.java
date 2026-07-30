package com.algaworks.algashop.ordering.application.shoppingcart.management;

import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartCreatedEvent;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingService;
import com.algaworks.algashop.ordering.infrastructure.listener.shoppingcart.ShoppingCartEventListener;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ShoppingCartManagementApplicationServiceIT {

    @Autowired
    private ShoppingService shoppingService;

    @MockitoSpyBean
    private ShoppingCartEventListener shoppingCartEventListener;

    @Test
    void shouldEmitShoppingCartCreatedEventWhenShoppingCartIsCreated() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().build();

        ShoppingCart.startShopping(shoppingCart.customerId());

        Mockito.verify(shoppingCartEventListener, Mockito.times(1))
                .listen(Mockito.any(ShoppingCartCreatedEvent.class));
    }

}