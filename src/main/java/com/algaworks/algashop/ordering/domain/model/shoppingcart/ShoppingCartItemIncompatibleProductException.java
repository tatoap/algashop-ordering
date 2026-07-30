package com.algaworks.algashop.ordering.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.DomainException;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;

import static com.algaworks.algashop.ordering.domain.model.ErrorMessages.ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT;

public class ShoppingCartItemIncompatibleProductException extends DomainException {

    public ShoppingCartItemIncompatibleProductException(String message) {
        super(message);
    }

    public ShoppingCartItemIncompatibleProductException(ShoppingCartItemId shoppingCartItemId, ProductId productId) {
        super(String.format(ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT, shoppingCartItemId, productId));
    }
}
