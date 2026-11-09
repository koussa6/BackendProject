package com.library.com.service;

import com.library.com.exception.SubscriptionException;
import com.library.com.payload.dto.SubscriptionDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubscriptionService {

    SubscriptionDto subscribe(SubscriptionDto subscriptionDTO) throws Exception;

    SubscriptionDto getUsersActiveSubscription(Long userId) throws Exception;

    SubscriptionDto cancelSubscription(Long subscriptionId, String reason) throws SubscriptionException;

    SubscriptionDto activateSubscription(Long subscriptionId, Long paymentId) throws SubscriptionException;

    List<SubscriptionDto> getAllSubscriptions(Pageable pageable);
    void deactivateExpiredSubscriptions();
}
