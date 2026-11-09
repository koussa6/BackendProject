package com.library.com.controller;


import com.library.com.exception.SubscriptionException;
import com.library.com.payload.dto.SubscriptionDto;
import com.library.com.payload.response.ApiResponse;
import com.library.com.service.SubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subs")
@Tag(name = "Subscription controller")

public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(
            @Valid @RequestBody SubscriptionDto subscription
    ) throws Exception {
        SubscriptionDto dto = subscriptionService.subscribe(subscription);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/user/active")
    public ResponseEntity<?> getUsersActiveSubscription(
            @RequestParam(required = false) Long userId
    ) throws Exception {

        SubscriptionDto dto = subscriptionService
                .getUsersActiveSubscription(userId);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/admin")
    public ResponseEntity<?> getAllSubscriptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        org.springframework.data.domain.Pageable pageable = PageRequest.of(page, size);
        List<SubscriptionDto> dto = subscriptionService.getAllSubscriptions(pageable);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/admin/deactivate-expired")
    public ResponseEntity<ApiResponse> deactivateExpiredSubscriptions() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        subscriptionService.deactivateExpiredSubscriptions();
        ApiResponse resp=new ApiResponse("task done!",true);
        return ResponseEntity.ok(resp);
    }
    @PostMapping("/cancel/{subscriptionId}")
    public ResponseEntity<?> cancelSubscription(
            @PathVariable Long subscriptionId,
            @RequestParam(required = false) String reason) throws SubscriptionException {

        SubscriptionDto subscription = subscriptionService
                .cancelSubscription(subscriptionId, reason);
        return ResponseEntity.ok(subscription);
    }
    @PostMapping("/activate")
    public ResponseEntity<?> activateSubscription(
            @RequestParam Long subscriptionId,
            @RequestParam Long paymentId) throws SubscriptionException {
        SubscriptionDto subscription = subscriptionService
                .activateSubscription(subscriptionId, paymentId);
        return ResponseEntity.ok(subscription);
    }
}
