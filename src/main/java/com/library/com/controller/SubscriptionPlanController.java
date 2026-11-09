package com.library.com.controller;


import com.library.com.payload.dto.SubscriptionPlanDto;
import com.library.com.payload.response.ApiResponse;
import com.library.com.service.SubscriptionPlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")
@Tag(name = "Subscription Plan controller")
public class SubscriptionPlanController {
    private final SubscriptionPlanService subscriptionPlanService;

    @GetMapping
    @PreFilter("isAuthenticated()")
    public ResponseEntity<?> getAllSubscriptionPlans() {
        List<SubscriptionPlanDto> plans = subscriptionPlanService.getAllSubscriptionPlan();
        return ResponseEntity.ok(plans);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> createSubscriptionPlan(
            @Valid @RequestBody SubscriptionPlanDto subscriptionPlanDTO
    ) throws Exception {
        SubscriptionPlanDto plans = subscriptionPlanService.createSubscriptionPlan(
                subscriptionPlanDTO
        );
        return ResponseEntity.ok(plans);
    }
    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateSubscriptionPlan(
            @Valid @RequestBody SubscriptionPlanDto subscriptionPlanDTO,
            @PathVariable long id
    ) throws Exception {
        SubscriptionPlanDto plans = subscriptionPlanService.updateSubscriptionPlan(
                id, subscriptionPlanDTO
        );
        return ResponseEntity.ok(plans);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<ApiResponse> deleteSubscriptionPlan(

            @PathVariable long id
    ) throws Exception {
        subscriptionPlanService.deleteSubscriptionPlan(id);
        ApiResponse resp=new ApiResponse("subscription delete successfully",true);
        return ResponseEntity.ok(resp);
    }
}




