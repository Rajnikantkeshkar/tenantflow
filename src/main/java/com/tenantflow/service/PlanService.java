package com.tenantflow.service;

import com.tenantflow.exception.ResourceNotFoundException;
import com.tenantflow.model.Plan;
import com.tenantflow.repository.PlanRepository;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PlanService {

    private final PlanRepository planRepository;
    private final AuditService auditService;

    public PlanService(PlanRepository planRepository, AuditService auditService) {

        this.planRepository = planRepository;
        this.auditService = auditService;
    }

    /*
     * Save plan
     */
    public Plan save(Plan plan) {
        log.info("Attempting to save new plan with name: {}", plan.getName());

        if (planRepository.findByName(plan.getName()).isPresent()) {
            throw new com.tenantflow.exception.ResourceAlreadyExistsException(
                    "Plan with name " + plan.getName() + " already exists!");
        }

        Plan savedPlan = planRepository.save(plan);
        log.info("Successfully saved plan with ID: {}", savedPlan.getId());

        org.springframework.security.core.Authentication auth = 
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String email = (auth != null) ? auth.getName() : "SYSTEM";

        auditService.logAction(
                email,
                "CREATE_PLAN",
                "Plan",
                "Created plan: " + savedPlan.getName()
        );

        return savedPlan;
    }

    /*
     * Get all plans
     */
    public List<Plan> findAll() {
        log.info("Fetching all plans from database");
        return planRepository.findAll();
    }

    /*
     * Get plan by id
     */
    public Plan findById(UUID id) {
        log.info("Fetching plan by ID: {}", id);
        return planRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Plan not found"));
    }

    /*
     * Update existing plan
     */
    public Plan update(UUID id, Plan updatedPlan) {
        log.info("Attempting to update plan with ID: {}", id);

        Plan existingPlan = planRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Plan not found"));

        existingPlan.setName(updatedPlan.getName());

        existingPlan.setMonthlyPrice(
                updatedPlan.getMonthlyPrice());

        existingPlan.setApiCallLimit(
                updatedPlan.getApiCallLimit());

        existingPlan.setMaxUsers(
                updatedPlan.getMaxUsers());

        Plan saved = planRepository.save(existingPlan);
        log.info("Successfully updated plan with ID: {}", id);

        org.springframework.security.core.Authentication auth = 
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String email = (auth != null) ? auth.getName() : "SYSTEM";

        auditService.logAction(
                email,
                "UPDATE_PLAN",
                "Plan",
                "Updated plan: " + saved.getName() + " (ID: " + id + ")"
        );

        return saved;
    }

    /*
     * Delete plan
     */
    public void delete(UUID id) {
        log.info("Attempting to delete plan with ID: {}", id);

        Plan existingPlan = planRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Plan not found"));

        planRepository.delete(existingPlan);
        log.info("Successfully deleted plan with ID: {}", id);

        org.springframework.security.core.Authentication auth = 
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String email = (auth != null) ? auth.getName() : "SYSTEM";

        auditService.logAction(
                email,
                "DELETE_PLAN",
                "Plan",
                "Deleted plan name: " + existingPlan.getName() + " (ID: " + id + ")"
        );
    }

}