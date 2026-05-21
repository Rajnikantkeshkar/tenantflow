package com.tenantflow.service;

import com.tenantflow.exception.ResourceNotFoundException;

import com.tenantflow.model.Plan;
import com.tenantflow.model.Tenant;
import com.tenantflow.model.TenantUsage;

import com.tenantflow.repository.PlanRepository;
import com.tenantflow.repository.TenantRepository;
import com.tenantflow.repository.TenantUsageRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TenantService {

    private static final Logger logger =
            LoggerFactory.getLogger(TenantService.class);

    private final TenantRepository tenantRepository;

    private final PlanRepository planRepository;

    private final TenantUsageRepository tenantUsageRepository;

    public TenantService(
            TenantRepository tenantRepository,
            PlanRepository planRepository,
            TenantUsageRepository tenantUsageRepository) {

        this.tenantRepository = tenantRepository;
        this.planRepository = planRepository;
        this.tenantUsageRepository = tenantUsageRepository;
    }

    /*
     * Create Tenant
     */
    public Tenant createTenant(
            Tenant tenant,
            UUID planId) {

        logger.info(
                "Creating tenant with company name: {}",
                tenant.getCompanyName());

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Plan not found"));

        tenant.setPlan(plan);

        Tenant savedTenant =
                tenantRepository.save(tenant);

        // Initialize Usage
        TenantUsage usage = new TenantUsage();
        usage.setTenant(savedTenant);
        usage.setApiCallsUsed(0L);
        tenantUsageRepository.save(usage);

        logger.info(
                "Tenant created successfully with id: {}",
                savedTenant.getId());

        return savedTenant;
    }

    /*
     * Get all tenants
     */
    public List<Tenant> findAll() {

        logger.info("Fetching all tenants");

        return tenantRepository.findAll();
    }

    /*
     * Get tenant by id
     */
    public Tenant findById(UUID id) {

        logger.info(
                "Fetching tenant with id: {}",
                id);

        return tenantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tenant not found"));
    }

    /*
     * Update tenant
     */
    public Tenant update(
            UUID id,
            Tenant updatedTenant,
            UUID planId) {

        logger.info(
                "Updating tenant with id: {}",
                id);

        Tenant existingTenant =
                tenantRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Tenant not found"));

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Plan not found"));

        existingTenant.setCompanyName(
                updatedTenant.getCompanyName());

        existingTenant.setSubdomain(
                updatedTenant.getSubdomain());

        existingTenant.setStatus(
                updatedTenant.getStatus());

        existingTenant.setPlan(plan);

        Tenant savedTenant =
                tenantRepository.save(existingTenant);

        logger.info(
                "Tenant updated successfully");

        return savedTenant;
    }

    /*
     * Delete tenant
     */
    public void delete(UUID id) {

        logger.info(
                "Deleting tenant with id: {}",
                id);

        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Tenant not found"));

        tenantRepository.delete(tenant);

        logger.info(
                "Tenant deleted successfully");
    }

}