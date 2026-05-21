package com.tenantflow.security;

import com.tenantflow.exception.UpgradeRequiredException;
import com.tenantflow.model.TenantUsage;
import com.tenantflow.repository.TenantUsageRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class ApiUsageInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ApiUsageInterceptor.class);

    private final TenantUsageRepository tenantUsageRepository;

    public ApiUsageInterceptor(TenantUsageRepository tenantUsageRepository) {
        this.tenantUsageRepository = tenantUsageRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String tenantIdStr = (String) request.getAttribute("tenantId");

        if (tenantIdStr != null) {
            UUID tenantId = UUID.fromString(tenantIdStr);

            TenantUsage usage = tenantUsageRepository.findByTenantId(tenantId)
                    .orElseThrow(() -> new RuntimeException("Tenant usage record not found"));

            Long apiCallLimit = usage.getTenant().getPlan().getApiCallLimit();
            Long currentUsage = usage.getApiCallsUsed();

            if (apiCallLimit != null && currentUsage >= apiCallLimit) {
                logger.warn("Tenant {} exceeded API limit. Used: {}, Limit: {}", tenantId, currentUsage, apiCallLimit);
                throw new UpgradeRequiredException("API limit exceeded. Please upgrade your subscription plan to continue using the service.");
            }

            // Increment usage
            usage.setApiCallsUsed(currentUsage + 1);
            tenantUsageRepository.save(usage);
        }

        return true;
    }
}
