package com.tenantflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "plans")

@Getter
@Setter
@ToString
public class Plan extends BaseEntity {

    /*
     * FREE / PRO / ENTERPRISE
     */
    @Enumerated(EnumType.STRING)

    @Column(nullable = false, unique = true)
    private PlanName name;

    /*
     * Monthly subscription price
     */
    @Column(name = "monthly_price", nullable = false)
    private BigDecimal monthlyPrice;

    /*
     * API call limit per month/day
     */
    @Column(name = "api_call_limit")
    private Long apiCallLimit;

    /*
     * Maximum users allowed
     */
    @Column(name = "max_users")
    private Integer maxUsers;

}