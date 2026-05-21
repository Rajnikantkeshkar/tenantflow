package com.tenantflow.service;

import com.tenantflow.model.AuditLog;
import com.tenantflow.repository.AuditLogRepository;

import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    /*
     * Save audit log
     */
    public void logAction(String userEmail, String action, String entityName, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUserEmail(userEmail);
        auditLog.setAction(action);
        auditLog.setEntityName(entityName);
        auditLog.setDetails(details);
        auditLogRepository.save(auditLog);
    }

}
