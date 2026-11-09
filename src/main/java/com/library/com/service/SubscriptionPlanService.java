package com.library.com.service;

import com.library.com.modal.SubscriptionPlan;
import com.library.com.payload.dto.SubscriptionPlanDto;

import java.util.List;

public interface SubscriptionPlanService {
    SubscriptionPlanDto createSubscriptionPlan(SubscriptionPlanDto planDTO) throws Exception;

    SubscriptionPlanDto updateSubscriptionPlan(Long planId, SubscriptionPlanDto planDTO) throws Exception;

    void deleteSubscriptionPlan(Long planId) throws Exception;

    List<SubscriptionPlanDto> getAllSubscriptionPlan();

    SubscriptionPlanDto getBySubscriptionPlanCode(String subscriptionPlanCode) throws Exception;
}
