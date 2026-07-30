package com.algaworks.algashop.ordering.domain.model.product;

import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.product.Product;
import com.algaworks.algashop.ordering.domain.model.product.ProductName;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;

public class ProductTestDataBuilder {

    public static final ProductId DEFAULT_PRODUCT_ID = new ProductId();

    private ProductTestDataBuilder() {
    }

    public static Product.ProductBuilder aProduct() {
        return Product.builder()
            .id(DEFAULT_PRODUCT_ID)
            .name(new ProductName("Notebook X11"))
            .price(new Money("3000"))
            .inStock(true);
    }

    public static Product.ProductBuilder aProductUnavailable() {
        return Product.builder()
            .id(new ProductId())
            .name(new ProductName("Desktop X9000"))
            .price(new Money("5000"))
            .inStock(false);
    }

    public static Product.ProductBuilder aProductAltRamMemory() {
        return Product.builder()
            .id(new ProductId())
            .name(new ProductName("4GB RAM"))
            .price(new Money("200"))
            .inStock(true);
    }

    public static Product.ProductBuilder aProductAltMousePad() {
        return Product.builder()
            .id(new ProductId())
            .name(new ProductName("Mouse pad"))
            .price(new Money("100"))
            .inStock(true);
    }
}
