package com.algaworks.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.product.ProductName;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartItemId;
import com.algaworks.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartItemPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ShoppingCartPersistenceEntityDisassembler {

    public ShoppingCart toItemsDomainEntities(ShoppingCartPersistenceEntity entity) {
        return ShoppingCart.existing()
                .id(new ShoppingCartId(entity.getId()))
                .customerId(new CustomerId(entity.getCustomerId()))
                .totalAmount(new Money(entity.getTotalAmount()))
                .createdAt(entity.getCreatedAt())
                .totalItems(new Quantity(entity.getTotalItems()))
                .items(toItemsDomainEntities(entity.getItems()))
                .build();
    }

    private Set<ShoppingCartItem> toItemsDomainEntities(Set<ShoppingCartItemPersistenceEntity> items) {
        return items.stream().map(this::toItemEntity).collect(Collectors.toSet());
    }

    private ShoppingCartItem toItemEntity(ShoppingCartItemPersistenceEntity entity) {
        return ShoppingCartItem.existing()
                .id(new ShoppingCartItemId(entity.getId()))
                .shoppingCartId(new ShoppingCartId(entity.getShoppingCartId()))
                .productId(new ProductId(entity.getProductId()))
                .productName(new ProductName(entity.getProductName()))
                .price(new Money(entity.getPrice()))
                .quantity(new Quantity(entity.getQuantity()))
                .totalAmount(new Money(entity.getTotalAmount()))
                .available(entity.getAvailable())
                .build();
    }
}
