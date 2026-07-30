package com.algaworks.algashop.ordering.infrastructure.listener.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.shoppingcart.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShoppingCartEventListener {

    @EventListener
    public void listen(ShoppingCartCreatedEvent event) {
        log.info("ShoppingCartCreatedEvent 1");
    }

    @EventListener
    public void listen(ShoppingCartEmptiedEvent event) {
        log.info("ShoppingCartEmptiedEvent 1");
    }

    @EventListener
    public void listen(ShoppingCartItemAddedEvent event) {
        log.info("ShoppingCartItemAddedEvent 1");
    }

    @EventListener
    public void listen(ShoppingCartItemRemovedEvent event) {
        log.info("ShoppingCartItemRemovedEvent 1");
    }
}
