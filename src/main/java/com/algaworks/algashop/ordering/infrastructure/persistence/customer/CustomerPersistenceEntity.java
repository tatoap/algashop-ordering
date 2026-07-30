package com.algaworks.algashop.ordering.infrastructure.persistence.customer;

import com.algaworks.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "id")
@Table(name = "\"customer\"")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class CustomerPersistenceEntity extends AbstractAggregateRoot<CustomerPersistenceEntity> {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String document;
    private Boolean promotionNotificationsAllowed;
    private Boolean archived;
    private OffsetDateTime registeredAt;
    private OffsetDateTime archivedAt;
    private Integer loyaltyPoints;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address.street", column = @Column(name = "address_street")),
            @AttributeOverride(name = "address.number", column = @Column(name = "address_number")),
            @AttributeOverride(name = "address.complement", column = @Column(name = "address_complement")),
            @AttributeOverride(name = "address.neighborhood", column = @Column(name = "address_neighborhood")),
            @AttributeOverride(name = "address.city", column = @Column(name = "address_city")),
            @AttributeOverride(name = "address.state", column = @Column(name = "address_state")),
            @AttributeOverride(name = "address.zipCode", column = @Column(name = "address_zipCode"))
    })
    private AddressEmbeddable address;

    @CreatedBy
    private UUID createdByUserID;

    @LastModifiedDate
    private OffsetDateTime lastModifiedAt;

    @LastModifiedBy
    private UUID lastModifiedByUserId;

    @Version
    private Long version;

    public Collection<Object> getEvents() {
        return super.domainEvents();
    }

    public void addEvents(Collection<Object> events) {
        if (events != null) {
            for (Object event : events) {
                this.registerEvent(event);
            }
        }
    }

}
