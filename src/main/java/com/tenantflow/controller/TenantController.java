package com.tenantflow.controller;

import com.tenantflow.dto.TenantRequestDto;
import com.tenantflow.dto.TenantResponseDto;

import com.tenantflow.model.Tenant;

import com.tenantflow.service.TenantService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tenants")

public class TenantController {

    private final TenantService tenantService;

    public TenantController(
            TenantService tenantService) {

        this.tenantService = tenantService;
    }

    /*
     * Create Tenant API
     */
    @PostMapping
    public ResponseEntity<TenantResponseDto>
    createTenant(
            @Valid @RequestBody TenantRequestDto dto) {

        Tenant tenant = mapToEntity(dto);

        Tenant savedTenant =
                tenantService.createTenant(
                        tenant,
                        dto.getPlanId()
                );

        return new ResponseEntity<>(
                mapToResponse(savedTenant),
                HttpStatus.CREATED
        );
    }

    /*
     * Get all tenants
     */
    @GetMapping
    public ResponseEntity<List<TenantResponseDto>>
    getAllTenants() {

        List<TenantResponseDto> response =
                tenantService.findAll()
                        .stream()
                        .map(this::mapToResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /*
     * Get tenant by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<TenantResponseDto>
    getTenantById(
            @PathVariable UUID id) {

        Tenant tenant =
                tenantService.findById(id);

        return ResponseEntity.ok(
                mapToResponse(tenant)
        );
    }

    /*
     * Update tenant
     */
    @PutMapping("/{id}")
    public ResponseEntity<TenantResponseDto>
    updateTenant(
            @PathVariable UUID id,

            @Valid @RequestBody TenantRequestDto dto) {

        Tenant updatedTenant =
                mapToEntity(dto);

        Tenant savedTenant =
                tenantService.update(
                        id,
                        updatedTenant,
                        dto.getPlanId()
                );

        return ResponseEntity.ok(
                mapToResponse(savedTenant)
        );
    }

    /*
     * Delete tenant
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteTenant(
            @PathVariable UUID id) {

        tenantService.delete(id);

        return ResponseEntity.ok(
                "Tenant deleted successfully"
        );
    }

    /*
     * DTO -> Entity
     */
    private Tenant mapToEntity(
            TenantRequestDto dto) {

        Tenant tenant = new Tenant();

        tenant.setCompanyName(
                dto.getCompanyName());

        tenant.setSubdomain(
                dto.getSubdomain());

        tenant.setStatus(
                dto.getStatus());

        return tenant;
    }

    /*
     * Entity -> Response DTO
     */
    private TenantResponseDto
    mapToResponse(Tenant tenant) {

        TenantResponseDto dto =
                new TenantResponseDto();

        dto.setId(tenant.getId());

        dto.setCompanyName(
                tenant.getCompanyName());

        dto.setSubdomain(
                tenant.getSubdomain());

        dto.setStatus(
                tenant.getStatus());

        dto.setPlanName(
                tenant.getPlan().getName().name());

        return dto;
    }

}