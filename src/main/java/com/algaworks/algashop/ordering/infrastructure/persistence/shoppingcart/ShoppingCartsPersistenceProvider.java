package com.algaworks.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCarts;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
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
public class ShoppingCartsPersistenceProvider implements ShoppingCarts {

    private final ShoppingCartPersistenceEntityRepository persistenceEntityRepository;
    private final ShoppingCartPersistenceEntityAssembler assembler;
    private final ShoppingCartPersistenceEntityDisassembler disassembler;
    private final EntityManager entityManager;

    @Override
    public void remove(ShoppingCart shoppingCart) {
        ShoppingCartPersistenceEntity entity = assembler.fromDomain(shoppingCart);

        persistenceEntityRepository.delete(entity);
    }

    @Override
    public void remove(ShoppingCartId shoppingCartId) {
        persistenceEntityRepository.findById(shoppingCartId.value()).ifPresentOrElse(
                (persistenceEntityRepository) -> remove(disassembler.toItemsDomainEntities(persistenceEntityRepository)),
                IllegalArgumentException::new
        );
    }

    @Override
    public Optional<ShoppingCart> ofId(ShoppingCartId shoppingCartId) {
        Optional<ShoppingCartPersistenceEntity> possibleEntity = persistenceEntityRepository.findById(shoppingCartId.value());

        return possibleEntity.map(disassembler::toItemsDomainEntities);
    }

    @Override
    public boolean exists(ShoppingCartId shoppingCartId) {
        return persistenceEntityRepository.existsById(shoppingCartId.value());
    }

    @Override
    @Transactional(readOnly = false)
    public void add(ShoppingCart aggregateRoot) {
        UUID shoppingCartId = aggregateRoot.id().value();

        persistenceEntityRepository.findById(shoppingCartId).ifPresentOrElse(
                (persistenceEntityRepository) -> update(aggregateRoot, persistenceEntityRepository),
                () -> insert(aggregateRoot)
        );
    }

    private void insert(ShoppingCart aggregateRoot) {
        ShoppingCartPersistenceEntity persistenceEntity = assembler.fromDomain(aggregateRoot);
        persistenceEntityRepository.saveAndFlush(persistenceEntity);
        updateVersion(aggregateRoot, persistenceEntity);
    }

    private void update(ShoppingCart aggregateRoot, ShoppingCartPersistenceEntity persistenceEntity) {
        persistenceEntity = assembler.merge(persistenceEntity, aggregateRoot);
        entityManager.detach(persistenceEntity);
        persistenceEntity = persistenceEntityRepository.saveAndFlush(persistenceEntity);
        updateVersion(aggregateRoot, persistenceEntity);
    }

    @Override
    public long count() {
        return persistenceEntityRepository.count();
    }

    @Override
    public Optional<ShoppingCart> ofCustomer(CustomerId customerId) {
        return persistenceEntityRepository.findByCustomer_id(customerId.value())
                .map(disassembler::toItemsDomainEntities);
    }

    @SneakyThrows
    private void updateVersion(ShoppingCart aggregateRoot, ShoppingCartPersistenceEntity persistenceEntity) {
        Field version = aggregateRoot.getClass().getDeclaredField("version");
        version.setAccessible(true);
        ReflectionUtils.setField(version, aggregateRoot, persistenceEntity.getVersion());
        version.setAccessible(false);
    }
}
