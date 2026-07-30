package com.algaworks.algashop.ordering.application.shoppingcart.management;

import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.product.Product;
import com.algaworks.algashop.ordering.domain.model.product.ProductCatalogService;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;
import com.algaworks.algashop.ordering.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShoppingCartManagementApplicationService {

    private ShoppingCarts shoppingCarts;
    private ProductCatalogService productCatalogService;
    private ShoppingService shoppingService;

    public void addItem(ShoppingCartItemInput input) {
        Objects.requireNonNull(input);

        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(input.getShoppingCartId()))
                .orElseThrow(ShoppingCartNotFoundException::new);

        Product product = productCatalogService.ofId(new ProductId(input.getProductId()))
                .orElseThrow(ProductNotFoundException::new);

        shoppingCart.addItem(product, new Quantity(input.getQuantity()));

        shoppingCarts.add(shoppingCart);
    }

    public ShoppingCartId createNew(UUID rawCustomerId) {
        Objects.requireNonNull(rawCustomerId);

        ShoppingCart shoppingCart = shoppingService.startShopping(new CustomerId(rawCustomerId));

        shoppingCarts.add(shoppingCart);

        return shoppingCart.id();
    }

    public void removeItem(UUID rawShoppingCartId, UUID rawShoppingCartItemId) {
        Objects.requireNonNull(rawShoppingCartId);
        Objects.requireNonNull(rawShoppingCartItemId);

        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(rawShoppingCartId))
                .orElseThrow(ShoppingCartNotFoundException::new);

        shoppingCart.removeItem(new ShoppingCartItemId(rawShoppingCartItemId));

        shoppingCarts.add(shoppingCart);
    }

    public void empty(UUID rawShoppingCartId) {
        Objects.requireNonNull(rawShoppingCartId);

        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(rawShoppingCartId))
                .orElseThrow(ShoppingCartNotFoundException::new);

        shoppingCart.empty();

        shoppingCarts.add(shoppingCart);
    }

    public void delete(UUID rawShoppingCartId) {
        Objects.requireNonNull(rawShoppingCartId);

        ShoppingCart shoppingCart = shoppingCarts.ofId(new ShoppingCartId(rawShoppingCartId))
                .orElseThrow(ShoppingCartNotFoundException::new);

        shoppingCarts.remove(shoppingCart);
    }
}
