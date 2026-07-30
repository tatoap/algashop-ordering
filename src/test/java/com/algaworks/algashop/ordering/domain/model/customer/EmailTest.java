package com.algaworks.algashop.ordering.domain.model.customer;

import com.algaworks.algashop.ordering.domain.model.commons.*;
import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

class EmailTest {

    @Test
    void shouldGenerateException_whenGivenInvalidEmail() {
        Customer customer = Customer.existing()
            .id(new CustomerId())
            .fullName(new FullName("Anonymous", "Anonymous"))
            .birthDate(new BirthDate(LocalDate.of(1982, 7, 17)))
            .email(new Email("anonymous@anonymous.com"))
            .phone(new Phone("000-000-0000"))
            .document(new Document("000-00-0000"))
            .promotionNotificationsAllowed(false)
            .archived(true)
            .archivedAt(OffsetDateTime.now())
            .registeredAt(OffsetDateTime.now())
            .loyaltyPoints(new LoyaltyPoints(10))
            .address(Address.builder()
                .street("Bourbon Street")
                .number("1134")
                .neighborhood("North Ville")
                .city("York")
                .state("South California")
                .zipCode(new ZipCode("12345"))
                .complement("Apt. 114")
                .build())
            .build();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> {
                customer.changeEmail(new Email("invalid"));
            });
    }

}