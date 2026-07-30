package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.commons.Email;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomersPersistenceProvider;
import com.algaworks.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

@DataJpaTest
@Import({
        CustomersPersistenceProvider.class,
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
public class CustomersPersistenceProviderIT {

    private CustomersPersistenceProvider customerProvider;
    private CustomerPersistenceEntityRepository customerRepository;

    @Autowired
    public CustomersPersistenceProviderIT(
            CustomersPersistenceProvider customerProvider,
            CustomerPersistenceEntityRepository customerRepository) {
        this.customerProvider = customerProvider;
        this.customerRepository = customerRepository;
    }

    @Test
    void shouldUpdateAndKeepPersistenceEntityState() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer().build();
        UUID customerId = customer.id().value();

        customerProvider.add(customer);

        var persistenceEntity = customerRepository.findById(customerId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getId()).isEqualTo(customerId);

        Assertions.assertThat(persistenceEntity.getCreatedByUserID()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenceEntity.getLastModifiedByUserId()).isNotNull();

        long version = customer.version();
        customer = customerProvider.ofId(customer.id()).orElseThrow();
        customer.changeEmail(new Email("teste@teste.com"));
        customerProvider.add(customer);

        Assertions.assertThat(customer.version()).isNotEqualTo(version);
    }
}
