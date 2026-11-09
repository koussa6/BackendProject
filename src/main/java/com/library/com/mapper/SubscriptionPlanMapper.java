package com.library.com.mapper;

import com.library.com.modal.SubscriptionPlan;
import com.library.com.payload.dto.SubscriptionPlanDto;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionPlanMapper {

    public SubscriptionPlanDto toDto(SubscriptionPlan plan){
        if(plan==null){
            return null;
        }
        SubscriptionPlanDto dto = new SubscriptionPlanDto();
        dto.setId(plan.getId());
        dto.setPlanCode(plan.getPlanCode());
        dto.setName(plan.getName());
        dto.setDescription(plan.getDescription());
        dto.setDurationDays(plan.getDurationDays());
        dto.setPrice(plan.getPrice());
        dto.setCurrency(plan.getCurrency());
        dto.setMaxBooksAllowed(plan.getMaxBooksAllowed());
        dto.setMaxDaysPerBook(plan.getMaxDaysPerBook());
        dto.setDisplayOrder(plan.getDisplayOrder());
        dto.setIsActive(plan.getIsActive());
        dto.setIsFeatured(plan.getIsFeatured());
        dto.setBadge(plan.getBadgeText());
        dto.setAdminNotes(plan.getAdminNotes());
        dto.setCreatedAt(plan.getCreatedAt());
        dto.setUpdatedAt(plan.getUpdatedAt());
        dto.setCreatedBy(plan.getCreatedBy());
        dto.setUpdatedBy(plan.getUpdatedBy());
        return dto;
    };
    public SubscriptionPlan toEntity(SubscriptionPlanDto dto){
        if(dto==null){
            return null;
        }
        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setId(dto.getId());
        plan.setPlanCode(dto.getPlanCode());
        plan.setName(dto.getName());
        plan.setDescription(dto.getDescription());
        plan.setDurationDays(dto.getDurationDays());
        plan.setPrice(dto.getPrice());
        plan.setMaxBooksAllowed(dto.getMaxBooksAllowed());
        plan.setMaxDaysPerBook(dto.getMaxDaysPerBook());
        plan.setDisplayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0);
        plan.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        plan.setIsFeatured(dto.getIsFeatured() != null ? dto.getIsFeatured() : false);
        plan.setBadge(dto.getBadgeText());
        plan.setAdminNotes(dto.getAdminNotes());
        plan.setCreatedBy(dto.getCreatedBy());
        plan.setUpdatedBy(dto.getUpdatedBy());
        return plan;
    }

    public void updateEntity(SubscriptionPlan plan,SubscriptionPlanDto dto){
        if(plan==null||dto==null){
            return ;
        }
        // Don't update ID or planCode (immutable after creation)
        if (dto.getName() != null) {
            plan.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            plan.setDescription(dto.getDescription());
        }

        if (dto.getDurationDays() != null) {
            plan.setDurationDays(dto.getDurationDays());
        }

        if (dto.getPrice() != null) {
            plan.setPrice(dto.getPrice());
        }

        if (dto.getCurrency() != null) {
            plan.setCurrency(dto.getCurrency());
        }

        if (dto.getMaxBooksAllowed() != null) {
            plan.setMaxBooksAllowed(dto.getMaxBooksAllowed());
        }
        if (dto.getMaxDaysPerBook() != null) {
            plan.setMaxDaysPerBook(dto.getMaxDaysPerBook());
        }

        if (dto.getDisplayOrder() != null) {
            plan.setDisplayOrder(dto.getDisplayOrder());
        }

        if (dto.getIsActive() != null) {
            plan.setIsActive(dto.getIsActive());
        }

        if (dto.getIsFeatured() != null) {
            plan.setIsFeatured(dto.getIsFeatured());
        }
        if (dto.getBadgeText() != null) {
            plan.setBadgeText(dto.getBadgeText());
        }

        if (dto.getAdminNotes() != null) {
            plan.setAdminNotes(dto.getAdminNotes());
        }

        if (dto.getUpdatedBy() != null) {
            plan.setUpdatedBy(dto.getUpdatedBy());
        }
    }
}
