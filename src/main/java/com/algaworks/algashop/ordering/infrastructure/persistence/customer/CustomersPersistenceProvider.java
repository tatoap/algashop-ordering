package com.algaworks.algashop.ordering.infrastructure.persistence.customer;

import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.customer.Customers;
import com.algaworks.algashop.ordering.domain.model.commons.Email;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomersPersistenceProvider implements Customers {

    private final CustomerPersistenceEntityRepository customerRepository;
    private final CustomerPersistenceEntityAssembler assembler;
    private final CustomerPersistenceEntityDisassembler disassembler;
    private final EntityManager entityManager;


    @Override
    public Optional<Customer> ofId(CustomerId customerId) {
        return customerRepository.findById(customerId.value()).map(disassembler::toDomainEntity);
    }

    @Override
    public boolean exists(CustomerId customerId) {
        return customerRepository.existsById(customerId.value());
    }

    @Override
    @Transactional(readOnly = false)
    public void add(Customer aggregateRoot) {
        UUID customerId = aggregateRoot.id().value();

        customerRepository.findById(customerId)
                .ifPresentOrElse(
                        (persistenceEntity) -> update(aggregateRoot, persistenceEntity),
                        () -> insert(aggregateRoot)
                );

        aggregateRoot.clearDomainEvents();
    }

    @Override
    public Optional<Customer> ofEmail(Email email) {
        return customerRepository.findByEmail(email.value())
                .map(disassembler::toDomainEntity);
    }

    @Override
    public boolean isEmailUnique(Email email, CustomerId exceptCustomerId) {
        return !customerRepository.existsByEmailAndIdNot(email.value(), exceptCustomerId.value());
    }

    @Override
    public long count() {
        return customerRepository.count();
    }

    private void insert(Customer aggregateRoot) {
        CustomerPersistenceEntity persistenceEntity = assembler.fromDomain(aggregateRoot);
        customerRepository.saveAndFlush(persistenceEntity);
        updateVersion(aggregateRoot, persistenceEntity);
    }

    private void update(Customer aggregateRoot, CustomerPersistenceEntity persistenceEntity) {
        persistenceEntity = assembler.merge(persistenceEntity, aggregateRoot);
        entityManager.detach(persistenceEntity);

        System.out.println(persistenceEntity);
        System.out.println(aggregateRoot);

        persistenceEntity = customerRepository.saveAndFlush(persistenceEntity);
        updateVersion(aggregateRoot, persistenceEntity);
    }

    @SneakyThrows
    private void updateVersion(Customer aggregateRoot, CustomerPersistenceEntity persistenceEntity) {
        Field version = aggregateRoot.getClass().getDeclaredField("version");
        version.setAccessible(true);
        ReflectionUtils.setField(version, aggregateRoot, persistenceEntity.getVersion());
        version.setAccessible(false);
    }
}
