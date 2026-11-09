package com.library.com.service.impl;

import com.library.com.mapper.SubscriptionPlanMapper;
import com.library.com.modal.SubscriptionPlan;
import com.library.com.modal.User;
import com.library.com.payload.dto.SubscriptionPlanDto;
import com.library.com.repository.SubscriptionPlanRepo;
import com.library.com.service.SubscriptionPlanService;
import com.library.com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {
    private final SubscriptionPlanRepo planRepository;
    private final SubscriptionPlanMapper planMapper;
    private final UserService userService;
    @Override
    public SubscriptionPlanDto createSubscriptionPlan(SubscriptionPlanDto planDTO) throws Exception {
        if (planRepository.existsByPlanCode(planDTO.getPlanCode())) {
            throw new Exception("plan code is already exist");
        }

        SubscriptionPlan plan = planMapper.toEntity(planDTO);

        User currentUser = userService.getCurrentUser();
        plan.setCreatedBy(currentUser.getFullName());
        plan.setUpdatedBy(currentUser.getFullName());

        SubscriptionPlan savedPlan = planRepository.save(plan);

        return planMapper.toDto(savedPlan);
    }

    @Override
    public SubscriptionPlanDto updateSubscriptionPlan(Long planId, SubscriptionPlanDto planDTO) throws Exception {
        SubscriptionPlan existingPlan = planRepository.findById(planId).orElseThrow(
                () -> new Exception("Plan not found!")
        );

        planMapper.updateEntity(existingPlan, planDTO);

        User currentUser = userService.getCurrentUser();
        existingPlan.setUpdatedBy(currentUser.getFullName());

        SubscriptionPlan updatedPlan = planRepository.save(existingPlan);

        return planMapper.toDto(updatedPlan);
    }

    @Override
    public void deleteSubscriptionPlan(Long planId) throws Exception {
        SubscriptionPlan existingPlan = planRepository.findById(planId).
                orElseThrow(
                () -> new Exception("Plan not found!")
        );
        planRepository.delete(existingPlan);
    }

    @Override
    public List<SubscriptionPlanDto> getAllSubscriptionPlan() {
      List<SubscriptionPlan>plans=planRepository.findAll();
      return plans.stream().map(planMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public SubscriptionPlanDto getBySubscriptionPlanCode(String subscriptionPlanCode) throws Exception {
        SubscriptionPlan plan=planRepository.findByPlanCode(subscriptionPlanCode);
        if(plan==null){
            throw new Exception("Plan not found");
        }
        return planMapper.toDto(plan);
    }
}
