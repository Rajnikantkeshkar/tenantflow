package com.tenantflow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tenants")

@Getter
@Setter
public class Tenant extends BaseEntity {

    /*
     * Company name
     *
     * Example:
     * Zomato
     * Swiggy
     */
    @Column(name = "company_name", nullable = false)
    private String companyName;

    /*
     * Unique subdomain
     *
     * Example:
     * zomato
     * swiggy
     */
    @Column(nullable = false, unique = true)
    private String subdomain;

    /*
     * ACTIVE / SUSPENDED / DELETED
     */
    @Enumerated(EnumType.STRING)

    @Column(nullable = false)
    private TenantStatus status;

    /*
     * Relationship:
     *
     * Many tenants can theoretically use same plan.
     *
     * Example:
     * 1000 companies on FREE plan.
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "plan_id")
    private Plan plan;

}