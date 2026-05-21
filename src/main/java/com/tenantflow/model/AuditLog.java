package com.tenantflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog extends BaseEntity {

    /*
     * User email
     */
    @Column(nullable = false)
    private String userEmail;

    /*
     * Action performed
     */
    @Column(nullable = false)
    private String action;

    /*
     * Entity affected
     */
    @Column(nullable = false)
    private String entityName;

    /*
     * Extra details
     */
    @Column(length = 1000)
    private String details;

}
