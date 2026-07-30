package com.algaworks.algashop.ordering.infrastructure.persistence.customer;

import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.commons.Address;
import com.algaworks.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceEntityAssembler {

    public CustomerPersistenceEntity fromDomain(Customer customer) {
        return merge(new CustomerPersistenceEntity(), customer);
    }

    public CustomerPersistenceEntity merge(CustomerPersistenceEntity entity, Customer customer) {
        entity.setId(customer.id().value());
        entity.setFirstName(customer.fullName().firstName());
        entity.setLastName(customer.fullName().lastName());
        entity.setBirthDate(customer.birthDate() != null ? customer.birthDate().value() : null);
        entity.setEmail(customer.email().value());
        entity.setPhone(customer.phone().value());
        entity.setDocument(customer.document().value());
        entity.setAddress(toAddressEmbeddable(customer.address()));
        entity.setPromotionNotificationsAllowed(customer.isPromotionNotificationsAllowed());
        entity.setArchived(customer.isArchived());
        entity.setRegisteredAt(customer.registeredAt());
        entity.setArchivedAt(customer.archivedAt());
        entity.setLoyaltyPoints(customer.loyaltyPoints().value());
        entity.setVersion(customer.version());
        entity.addEvents(customer.domainEvents());

        return entity;
    }

    private AddressEmbeddable toAddressEmbeddable(Address address) {
        if (address == null) {
            return null;
        }

        return AddressEmbeddable.builder()
                .street(address.street())
                .number(address.number())
                .neighborhood(address.neighborhood())
                .city(address.city())
                .state(address.state())
                .complement(address.complement())
                .zipCode(address.zipCode().value())
                .build();
    }
}
