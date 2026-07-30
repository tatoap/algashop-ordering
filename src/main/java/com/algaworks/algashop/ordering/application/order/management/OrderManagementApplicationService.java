package com.algaworks.algashop.ordering.application.order.management;

import com.algaworks.algashop.ordering.domain.model.order.Order;
import com.algaworks.algashop.ordering.domain.model.order.OrderId;
import com.algaworks.algashop.ordering.domain.model.order.OrderNotFoundException;
import com.algaworks.algashop.ordering.domain.model.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderManagementApplicationService {

    @Autowired
    private Orders orders;

    public void cancel(String rawOrderId) {
        Objects.requireNonNull(rawOrderId);

        Order order = this.findOrder(rawOrderId);
        order.cancel();
        orders.add(order);
    }

    public void markAsPaid(String rawOrderId) {
        Objects.requireNonNull(rawOrderId);

        Order order = this.findOrder(rawOrderId);
        order.markAsPaid();
        orders.add(order);
    }

    public void markAsReady(String rawOrderId) {
        Objects.requireNonNull(rawOrderId);

        Order order = this.findOrder(rawOrderId);
        order.markAsReady();
        orders.add(order);
    }

    public void place(String rawOrderId) {
        Objects.requireNonNull(rawOrderId);

        Order order = this.findOrder(rawOrderId);
        order.place();
        orders.add(order);
    }

    private Order findOrder(String rawOrderId) {
        OrderId orderId = new OrderId(rawOrderId);

        return orders.ofId(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
