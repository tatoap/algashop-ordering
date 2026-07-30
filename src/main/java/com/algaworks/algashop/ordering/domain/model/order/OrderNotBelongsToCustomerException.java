package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.DomainException;

public class OrderNotBelongsToCustomerException extends DomainException {

    public OrderNotBelongsToCustomerException() {
    }

    public OrderNotBelongsToCustomerException(String message) {
        super(message);
    }

    public OrderNotBelongsToCustomerException(String message, Throwable cause) {
        super(message, cause);
    }
}
