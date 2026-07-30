package com.algaworks.algashop.ordering.infrastructure.persistence.customer;

import com.algaworks.algashop.ordering.domain.model.commons.*;
import com.algaworks.algashop.ordering.domain.model.customer.BirthDate;
import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.algaworks.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceEntityDisassembler {

    public Customer toDomainEntity(CustomerPersistenceEntity entity) {
        return Customer.existing()
                .id(new CustomerId(entity.getId()))
                .fullName(new FullName(entity.getFirstName(), entity.getLastName()))
                .birthDate(new BirthDate(entity.getBirthDate()))
                .email(new Email(entity.getEmail()))
                .document(new Document(entity.getDocument()))
                .promotionNotificationsAllowed(entity.getPromotionNotificationsAllowed())
                .archived(entity.getArchived())
                .registeredAt(entity.getRegisteredAt())
                .archived(entity.getArchived())
                .address(toAddressValueObject(entity.getAddress()))
                .loyaltyPoints(new LoyaltyPoints(entity.getLoyaltyPoints()))
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
