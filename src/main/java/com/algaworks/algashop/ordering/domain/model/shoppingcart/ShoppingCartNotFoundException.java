package com.algaworks.algashop.ordering.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.DomainException;
import com.algaworks.algashop.ordering.domain.model.ErrorMessages;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;

import java.util.UUID;

public class ShoppingCartNotFoundException extends DomainException {

    public ShoppingCartNotFoundException() {
        super(String.format(ErrorMessages.ERROR_SHOPPING_CART_NOT_FOUND_EXCEPTION));
    }
}
