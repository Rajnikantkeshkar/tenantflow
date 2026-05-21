package com.tenantflow.dto;

import com.tenantflow.model.TenantStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TenantRequestDto {

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Subdomain is required")
    private String subdomain;

    @NotNull(message = "Status is required")
    private TenantStatus status;

    @NotNull(message = "Plan ID is required")
    private UUID planId;

}