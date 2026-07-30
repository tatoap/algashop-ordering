package com.algaworks.algashop.ordering.application.customer.notification;

import io.hypersistence.tsid.TSID;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface OrderNotificationService {

    void notifyPlaceOrder(NotifyOrderServiceInput input);

    void notifyPaidOrder(NotifyOrderServiceInput input);

    void notifyReadyOrder(NotifyOrderServiceInput input);

    void notifyCancelOrder(NotifyOrderServiceInput input);

    record NotifyOrderServiceInput(TSID orderId, UUID customerId, OffsetDateTime date) {}

}
