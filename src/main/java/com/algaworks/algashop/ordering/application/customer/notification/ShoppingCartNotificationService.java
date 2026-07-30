package com.algaworks.algashop.ordering.application.customer.notification;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface ShoppingCartNotificationService {

    void notifyCreatedShoppingCart(NotifyShoppingCartInput input);

    void notifyEmptiedShoppingCart(NotifyShoppingCartInput input);

    void notifyAddedItemShoppingCart(NotifyShoppingCartItemInput input);

    void notifyRemovedItemShoppingCart(NotifyShoppingCartItemInput input);

    record NotifyShoppingCartInput(UUID shoppingCartId, UUID customerId, OffsetDateTime date) {}

    record NotifyShoppingCartItemInput(UUID shoppingCartId, UUID customerId, UUID productId, OffsetDateTime date) {}
}
