package com.tenantflow.dto;

import com.tenantflow.model.TenantStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TenantResponseDto {

    private UUID id;
    private String companyName;
    private String subdomain;
    private TenantStatus status;
    private String planName;

}
