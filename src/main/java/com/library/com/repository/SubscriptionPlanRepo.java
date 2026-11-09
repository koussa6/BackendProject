package com.library.com.repository;

import com.library.com.modal.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepo extends JpaRepository<SubscriptionPlan,Long> {
    Boolean existsByPlanCode(String planCode);
    SubscriptionPlan findByPlanCode(String subscriptionPlanCode);
}
