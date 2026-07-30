package com.algaworks.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.algaworks.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartItemPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.shoppingcart.ShoppingCartPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShoppingCartPersistenceEntityAssembler {

    private final CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    public ShoppingCartPersistenceEntity fromDomain(ShoppingCart shoppingCart) {
        return merge(new ShoppingCartPersistenceEntity(), shoppingCart);
    }

    public ShoppingCartPersistenceEntity merge(ShoppingCartPersistenceEntity entity, ShoppingCart shoppingCart) {
        entity.setId(shoppingCart.id().value());
        entity.setCustomer(customerPersistenceEntityRepository.getReferenceById(shoppingCart.customerId().value()));
        entity.setTotalItems(shoppingCart.totalItems().value());
        entity.setTotalAmount(shoppingCart.totalAmount().value());
        entity.setCreatedAt(shoppingCart.createdAt());
        entity.setItems(toOrderItemsEntities(shoppingCart.items()));
        entity.addEvents(shoppingCart.domainEvents());

        return entity;
    }

    private Set<ShoppingCartItemPersistenceEntity> toOrderItemsEntities(Set<ShoppingCartItem> source) {
        return source.stream().map(i -> this.mergeItem(new ShoppingCartItemPersistenceEntity(), i)).collect(Collectors.toSet());
    }

    private ShoppingCartItemPersistenceEntity mergeItem(ShoppingCartItemPersistenceEntity persistenceEntity, ShoppingCartItem shoppingCartItem) {
        persistenceEntity.setId(shoppingCartItem.id().value());
        persistenceEntity.setProductId(shoppingCartItem.productId().value());
        persistenceEntity.setProductName(shoppingCartItem.productName().value());
        persistenceEntity.setPrice(shoppingCartItem.price().value());
        persistenceEntity.setQuantity(shoppingCartItem.quantity().value());
        persistenceEntity.setAvailable(shoppingCartItem.isAvailable());
        persistenceEntity.setTotalAmount(shoppingCartItem.totalAmount().value());

        return persistenceEntity;
    }

    private ShoppingCartItemPersistenceEntity toOrderItemsEntities(ShoppingCartItem source) {
        return ShoppingCartItemPersistenceEntity.builder()
                .id(source.id().value())
                .shoppingCart(ShoppingCartPersistenceEntity.builder().id(source.shoppingCartId().value()).build())
                .productId(source.productId().value())
                .productName(source.productName().value())
                .price(source.price().value())
                .quantity(source.quantity().value())
                .available(source.isAvailable())
                .totalAmount(source.totalAmount().value())
                .build();
    }
}
