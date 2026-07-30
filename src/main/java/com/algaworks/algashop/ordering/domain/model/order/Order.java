package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.AbstractEventSourceEntity;
import com.algaworks.algashop.ordering.domain.model.AggregateRoot;
import com.algaworks.algashop.ordering.domain.model.commons.Money;
import com.algaworks.algashop.ordering.domain.model.commons.Quantity;
import com.algaworks.algashop.ordering.domain.model.product.Product;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Order
        extends AbstractEventSourceEntity
        implements AggregateRoot<OrderId> {

    private OrderId id;
    private CustomerId customerId;
    private Money totalAmount;
    private Quantity totalItems;
    private OffsetDateTime placedAt;
    private OffsetDateTime paidAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime readyAt;
    private Shipping shipping;
    private Billing billing;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private Set<OrderItem> items;
    private Long version;

    @Builder(builderClassName = "ExistingOrderBuilder", builderMethodName = "existing")
    public Order(OrderId id, CustomerId customerId,
                 Money totalAmount, Quantity totalItems,
                 OffsetDateTime placedAt, OffsetDateTime paidAt,
                 OffsetDateTime canceledAt, OffsetDateTime readyAt,
                 Shipping shipping, Billing billing,
                 OrderStatus status, PaymentMethod paymentMethod,
                 Set<OrderItem> items, Long version) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setTotalAmount(totalAmount);
        this.setTotalItems(totalItems);
        this.setPlacedAt(placedAt);
        this.setPaidAt(paidAt);
        this.setCanceledAt(canceledAt);
        this.setReadyAt(readyAt);
        this.setShipping(shipping);
        this.setBilling(billing);
        this.setStatus(status);
        this.setPaymentMethod(paymentMethod);
        this.setItems(items);
        this.setVersion(version);
    }

    public static Order draft(CustomerId customerId) {
        return new Order(
                new OrderId(),
                customerId,
                Money.ZERO,
                Quantity.ZERO,
                null,
                null,
                null,
                null,
                null,
                null,
                OrderStatus.DRAFT,
                null,
                new HashSet<>(),
                null
        );
    }

    public void addItem(Product product, Quantity quantity) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(quantity);

        this.verifyIfChangeable();

        product.checkOutOfStock();

        OrderItem orderItem = OrderItem.brandNew()
            .orderId(this.id())
            .product(product)
            .quantity(quantity)
            .build();

        if (this.items == null) {
            this.items = new HashSet<>();
        }

        this.items.add(orderItem);

        this.recalculateTotals();
    }

    public void removeItem(OrderItemId orderItemId) {
        Objects.requireNonNull(orderItemId);

        this.verifyIfChangeable();

        OrderItem orderItem = this.findOrderItem(orderItemId);

        if (orderItem == null) {
            throw new OrderDoesNotContainOrderItemException(this.id(), orderItemId);
        }

        this.items.remove(orderItem);
        this.recalculateTotals();
    }

    public void place() {
        this.verifyIfCanChangeToPlaced();
        this.changeStatus(OrderStatus.PLACED);
        this.setPlacedAt(OffsetDateTime.now());
        this.publishDomainEvent(new OrderPlacedEvent(this.id(), this.customerId(), this.placedAt()));
    }

    public boolean isPlaced() {
        return OrderStatus.PLACED.equals(this.status());
    }

    public boolean isPaid() {
        return OrderStatus.PAID.equals(this.status());
    }

    public boolean isDraft() {
        return OrderStatus.DRAFT.equals(this.status());
    }

    public boolean isCanceled() {
        return OrderStatus.CANCELED.equals(this.status());
    }

    public boolean isReady() { return OrderStatus.READY.equals(this.status()); }

    public void changePaymentMethod(PaymentMethod paymentMethod) {
        Objects.requireNonNull(paymentMethod);
        this.verifyIfChangeable();

        this.setPaymentMethod(paymentMethod);
    }

    public void changeBilling(Billing billingInfo) {
        Objects.requireNonNull(billingInfo);
        this.verifyIfChangeable();

        this.setBilling(billingInfo);
    }

    public void changeShipping(Shipping newShipping) {
        Objects.requireNonNull(newShipping);
        this.verifyIfChangeable();

        if (newShipping.expectedDate().isBefore(LocalDate.now())) {
            throw new OrderInvalidShippingDeliveryDateException(this.id());
        }

        this.setShipping(newShipping);
        this.recalculateTotals();
    }

    public OrderId id() {
        return id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public Quantity totalItems() {
        return totalItems;
    }

    public OffsetDateTime placedAt() {
        return placedAt;
    }

    public OffsetDateTime paidAt() {
        return paidAt;
    }

    public OffsetDateTime canceledAt() {
        return canceledAt;
    }

    public OffsetDateTime readyAt() {
        return readyAt;
    }

    public Shipping shipping() {
        return shipping;
    }

    public Billing billing() {
        return billing;
    }

    public OrderStatus status() {
        return status;
    }

    public PaymentMethod paymentMethod() {
        return paymentMethod;
    }

    public Set<OrderItem> items() {
        return Collections.unmodifiableSet(items);
    }

    public Long version() {
        return version;
    }

    public void markAsPaid() {
        this.setPaidAt(OffsetDateTime.now());
        this.changeStatus(OrderStatus.PAID);
        this.publishDomainEvent(new OrderPaidEvent(this.id(), this.customerId(), this.paidAt()));
    }

    public void markAsReady() {
        this.changeStatus(OrderStatus.READY);
        this.setReadyAt(OffsetDateTime.now());
        this.publishDomainEvent(new OrderReadyEvent(this.id(), this.customerId(), this.readyAt()));
    }

    public void cancel() {
        this.changeStatus(OrderStatus.CANCELED);
        this.setCanceledAt(OffsetDateTime.now());
        this.publishDomainEvent(new OrderCanceledEvent(this.id(), this.customerId(), this.canceledAt()));
    }

    public void changeItemQuantity(OrderItemId orderItemId, Quantity quantity) {
        Objects.requireNonNull(orderItemId);
        Objects.requireNonNull(quantity);

        this.verifyIfChangeable();

        OrderItem orderItem = this.findOrderItem(orderItemId);
        orderItem.changeQuantity(quantity);

        this.recalculateTotals();
    }

    private void verifyIfChangeable() {
        if (!this.isDraft()) {
            throw new OrderCannotBeEditedException(this.id(), this.status());
        }
    }

    private OrderItem findOrderItem(OrderItemId orderItemId) {
        Objects.requireNonNull(orderItemId);

        return this.items().stream()
                .filter(i -> i.id().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new OrderDoesNotContainOrderItemException(this.id(), orderItemId));
    }

    private void recalculateTotals() {
        BigDecimal totalItemsAmount = this.items().stream().map(i -> i.totalAmount().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalItemsQuantity = this.items.stream().map(i -> i.quantity().value()).reduce(0, Integer::sum);

        BigDecimal shippingCost;

        if (this.shipping() == null) {
            shippingCost = BigDecimal.ZERO;
        } else {
            shippingCost = this.shipping.cost().value();
        }

        BigDecimal totalAmount = totalItemsAmount.add(shippingCost);

        this.setTotalAmount(new Money(totalAmount));
        this.setTotalItems(new Quantity(totalItemsQuantity));
    }

    private void verifyIfCanChangeToPlaced() {
        if (this.shipping() == null) {
            throw OrderCannotBePlacedException.noShippingInfo(this.id());
        }

        if (this.billing() == null) {
            throw OrderCannotBePlacedException.noBillingInfo(this.id());
        }

        if (this.paymentMethod() == null) {
            throw OrderCannotBePlacedException.noPaymentMethod(this.id());
        }

        if (this.items() == null || this.items().isEmpty()) {
            throw OrderCannotBePlacedException.noItems(this.id());
        }
    }

    private void changeStatus(OrderStatus newStatus) {
        Objects.requireNonNull(newStatus);

        if (this.status().canNotChangeTo(newStatus)) {
            throw new OrderStatusCannotBeChangedException(this.id(), this.status(), newStatus);
        }

        this.setStatus(newStatus);
    }

    private void setId(OrderId id) {
        this.id = id;
    }

    private void setCustomerId(CustomerId customerId) {
        Objects.requireNonNull(customerId);
        this.customerId = customerId;
    }

    private void setTotalAmount(Money totalAmount) {
        Objects.requireNonNull(totalAmount);
        this.totalAmount = totalAmount;
    }

    private void setTotalItems(Quantity totalItems) {
        Objects.requireNonNull(totalItems);
        this.totalItems = totalItems;
    }

    private void setPlacedAt(OffsetDateTime placedAt) {
        this.placedAt = placedAt;
    }

    private void setPaidAt(OffsetDateTime paidAt) {
        this.paidAt = paidAt;
    }

    private void setCanceledAt(OffsetDateTime canceledAt) {
        this.canceledAt = canceledAt;
    }

    private void setReadyAt(OffsetDateTime readyAt) {
        this.readyAt = readyAt;
    }

    private void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    private void setBilling(Billing billing) {
        this.billing = billing;
    }

    private void setStatus(OrderStatus status) {
        Objects.requireNonNull(status);
        this.status = status;
    }

    private void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    private void setItems(Set<OrderItem> items) {
        Objects.requireNonNull(items);
        this.items = items;
    }

    private void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
