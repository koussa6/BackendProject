package com.library.com.service.impl;


import com.library.com.exception.SubscriptionException;
import com.library.com.mapper.SubscriptionMapper;
import com.library.com.modal.Subscription;
import com.library.com.modal.SubscriptionPlan;
import com.library.com.modal.User;
import com.library.com.payload.dto.SubscriptionDto;
import com.library.com.repository.SubscriptionPlanRepo;
import com.library.com.repository.SubscriptionRepository;
import com.library.com.service.SubscriptionService;
import com.library.com.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepo subscriptionPlanRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserService userService;
    @Override
    public SubscriptionDto subscribe(SubscriptionDto subscriptionDTO) throws Exception {
        User user = userService.getCurrentUser();

        SubscriptionPlan plan = subscriptionPlanRepository
                .findById(subscriptionDTO.getPlanId()).orElseThrow(
                        () -> new Exception("Plan not found!")
                );
        Subscription subscription=subscriptionMapper.toEntity(subscriptionDTO);
        subscription.initializeFromPlan();
        subscription.setIsActive(false);
        Subscription saved=subscriptionRepository.save(subscription);
        return subscriptionMapper.toDTO(saved);
    }

    @Override
    public SubscriptionDto getUsersActiveSubscription(Long userId) throws Exception {
        User user=userService.getCurrentUser();
        Subscription subscription=subscriptionRepository
                .findActiveSubscriptionByUserId(user.getId(), LocalDate.now())
                .orElseThrow(()->new Exception("Subscription not found"));
        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public SubscriptionDto cancelSubscription(Long subscriptionId, String reason) throws SubscriptionException {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionException(
                        "Subscription not found with ID: " + subscriptionId));

        if (!subscription.getIsActive()) {
            throw new SubscriptionException("Subscription is already inactive");
        }

        subscription.setIsActive(false);
        subscription.setCancelledAt(LocalDateTime.now());
        subscription.setCancellationReason(reason != null ? reason : "Cancelled by user");

        subscription = subscriptionRepository.save(subscription);

       log.info("Subscription cancelled successfully: {}", subscriptionId);
        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public SubscriptionDto activateSubscription(Long subscriptionId, Long paymentId) throws SubscriptionException {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionException(
                        "Subscription not found with ID: " + subscriptionId));
        subscription.setIsActive(true);
       Subscription subscribe=subscriptionRepository.save(subscription);
       return subscriptionMapper.toDTO(subscribe);
    }

    @Override
    public List<SubscriptionDto> getAllSubscriptions(Pageable pageable) {
        List<Subscription>subs=subscriptionRepository.findAll();
        return subscriptionMapper.toDTOList(subs);
    }

    @Override
    public void deactivateExpiredSubscriptions() {
        List<Subscription>subs=subscriptionRepository.findExpiredActiveSubscriptions(LocalDate.now());
        for(Subscription sub:subs){
            sub.setIsActive(false);
            subscriptionRepository.save(sub);
        }
    }
}
