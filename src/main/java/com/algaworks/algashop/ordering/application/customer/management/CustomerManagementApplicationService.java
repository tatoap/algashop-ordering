package com.algaworks.algashop.ordering.application.customer.management;

import com.algaworks.algashop.ordering.application.commons.AddressData;
import com.algaworks.algashop.ordering.domain.model.commons.*;
import com.algaworks.algashop.ordering.domain.model.customer.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerManagementApplicationService {

    private final CustomerRegistrationService customerRegistration;
    private final Customers customers;

    @Transactional
    public UUID create(CustomerInput input) {
        Objects.requireNonNull(input);
        AddressData address = input.getAddress();

        Customer customer = customerRegistration.register(
                new FullName(input.getFirstName(), input.getLastName()),
                new BirthDate(input.getBirthDate()),
                new Email(input.getEmail()),
                new Phone(input.getPhone()),
                new Document(input.getDocument()),
                input.getPromotionNotificationsAllowed(),
                Address.builder()
                        .street(address.getStreet())
                        .number(address.getNumber())
                        .neighborhood(address.getNeighborhood())
                        .city(address.getCity())
                        .state(address.getState())
                        .complement(address.getComplement())
                        .zipCode(new ZipCode(address.getZipCode()))
                        .build()
        );

        customers.add(customer);

        return customer.id().value();
    }

    @Transactional
    public void update(UUID rawCustomerId, CustomerUpdateInput input) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(rawCustomerId);

        Customer customer = customers.ofId(new CustomerId(rawCustomerId))
                .orElseThrow(() -> new CustomerNotFoundException());

        customer.changeName(new FullName(input.getFirstName(), input.getLastName()));
        customer.changePhone(new Phone(input.getPhone()));

        if (Boolean.TRUE.equals(input.getPromotionNotificationsAllowed())) {
            customer.enablePromotionNotifications();
        } else {
            customer.disablePromotionNotifications();
        }

        AddressData address = input.getAddress();

        customer.changAddress(Address.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .neighborhood(address.getNeighborhood())
                .city(address.getCity())
                .state(address.getState())
                .complement(address.getComplement())
                .zipCode(new ZipCode(address.getZipCode()))
                .build());

        customers.add(customer);
    }

    @Transactional
    public void archive(UUID customerId) {
        Objects.requireNonNull(customerId);

        Customer customer = customers.ofId(new CustomerId(customerId))
                .orElseThrow(() -> new CustomerNotFoundException());

        customer.archive();
        customers.add(customer);
    }

    @Transactional
    public void changeEmail(UUID customerId, String newEmail) {
        Objects.requireNonNull(customerId);

        Customer customer = customers.ofId(new CustomerId(customerId))
                .orElseThrow(() -> new CustomerNotFoundException());

        customerRegistration.changeEmail(customer, new Email(newEmail));
        customers.add(customer);
    }
}
