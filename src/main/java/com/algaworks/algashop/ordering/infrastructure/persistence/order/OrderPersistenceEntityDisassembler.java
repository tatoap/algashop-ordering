package com.algaworks.algashop.ordering.infrastructure.persistence.order;

import com.algaworks.algashop.ordering.domain.model.commons.*;
import com.algaworks.algashop.ordering.domain.model.order.*;
import com.algaworks.algashop.ordering.domain.model.product.ProductName;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.order.OrderId;
import com.algaworks.algashop.ordering.domain.model.order.OrderItemId;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;
import com.algaworks.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderPersistenceEntityDisassembler {

    public Order toDomainEntity(OrderPersistenceEntity entity) {
        return Order.existing()
                .id(new OrderId(entity.getId()))
                .customerId(new CustomerId(entity.getCustomerId()))
                .totalAmount(new Money(entity.getTotalAmount()))
                .totalItems(new Quantity(entity.getTotalItems()))
                .status(OrderStatus.valueOf(entity.getStatus()))
                .paymentMethod(PaymentMethod.valueOf(entity.getPaymentMethod()))
                .placedAt(entity.getPlacedAt())
                .paidAt(entity.getPaidAt())
                .readyAt(entity.getReadyAt())
                .canceledAt(entity.getCanceledAt())
                .items(toDomainEntity(entity.getItems()))
                .version(entity.getVersion())
                .build();
    }

    private Set<OrderItem> toDomainEntity(Set<OrderItemPersistenceEntity> items) {
        return items.stream().map(i -> toDomainEntity(i)).collect(Collectors.toSet());
    }

    private OrderItem toDomainEntity(OrderItemPersistenceEntity item) {
        return OrderItem.existing()
                .id(new OrderItemId(item.getId()))
                .orderId(new OrderId(item.getOrderId()))
                .productId(new ProductId(item.getProductId()))
                .productName(new ProductName(item.getProductName()))
                .price(new Money(item.getPrice()))
                .quantity(new Quantity(item.getQuantity()))
                .totalAmount(new Money(item.getTotalAmount()))
                .build();
    }

    private Shipping toShippingValueObject(ShippingEmbeddable shipping) {
        return Shipping.builder()
                .address(toAddressValueObject(shipping.getAddress()))
                .cost(new Money(shipping.getCost()))
                .recipient(toRecipientValueObject(shipping.getRecipient()))
                .expectedDate(shipping.getExpectedDate())
                .build();
    }

    private Billing toBillingValueObject(BillingEmbeddable billing) {
        return Billing.builder()
                .fullName(new FullName(billing.getFirstName(), billing.getLastName()))
                .address(toAddressValueObject(billing.getAddress()))
                .document(new Document(billing.getDocument()))
                .phone(new Phone(billing.getPhone()))
                .build();
    }

    private Recipient toRecipientValueObject(RecipientEmbeddable recipient) {
        return Recipient.builder()
                .fullName(new FullName(recipient.getFirstName(), recipient.getLastName()))
                .document(new Document(recipient.getDocument()))
                .phone(new Phone(recipient.getPhone()))
                .build();
    }

    private Address toAddressValueObject(AddressEmbeddable address) {
        return Address.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .neighborhood(address.getNeighborhood())
                .complement(address.getComplement())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(new ZipCode(address.getZipCode()))
                .build();
    }
}
