package com.tenantflow.controller;

import com.tenantflow.dto.PlanRequestDto;
import com.tenantflow.model.Plan;
import com.tenantflow.service.PlanService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/plans")
@Slf4j
public class PlanController {

    private final PlanService planService;

    public PlanController(PlanService planService) {

        this.planService = planService;
    }

    /*
     * Create a new Plan (Restricted to ADMIN or OWNER)
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PostMapping
    public ResponseEntity<Plan> createPlan(
            @Valid @RequestBody PlanRequestDto dto) {
        log.info("REST request to create Plan: {}", dto.getName());

        Plan plan = mapToEntity(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(planService.save(plan));
    }

    /*
     * Get all plans
     */
    @GetMapping
    public ResponseEntity<List<Plan>> getAllPlans() {
        log.info("REST request to get all Plans");

        return ResponseEntity.ok(planService.findAll());
    }

    /*
     * Get plan by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<Plan> getPlanById(
            @PathVariable UUID id) {
        log.info("REST request to get Plan : {}", id);

        return ResponseEntity.ok(planService.findById(id));
    }

    /*
     * Update a Plan (Restricted to ADMIN or OWNER)
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @PutMapping("/{id}")
    public ResponseEntity<Plan> updatePlan(
            @PathVariable UUID id,

            @Valid @RequestBody PlanRequestDto dto) {
        log.info("REST request to update Plan : {}", id);

        Plan updatedPlan = mapToEntity(dto);

        return ResponseEntity.ok(planService.update(id, updatedPlan));
    }

    /*
     * Delete a Plan (Restricted to ADMIN or OWNER)
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(
            @PathVariable UUID id) {
        log.info("REST request to delete Plan : {}", id);

        planService.delete(id);

        return ResponseEntity.noContent().build();
    }

    /*
     * DTO -> Entity Mapper
     */
    private Plan mapToEntity(PlanRequestDto dto) {

        Plan plan = new Plan();

        plan.setName(dto.getName());

        plan.setMonthlyPrice(
                dto.getMonthlyPrice());

        plan.setApiCallLimit(
                dto.getApiCallLimit());

        plan.setMaxUsers(
                dto.getMaxUsers());

        return plan;
    }

}