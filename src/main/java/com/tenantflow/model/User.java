package com.tenantflow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")

@Getter
@Setter
public class User extends BaseEntity {

    /*
     * Full name
     */
    @Column(nullable = false)
    private String name;

    /*
     * Login email
     *
     * Must be unique globally.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /*
     * Encrypted password
     *
     * NEVER store plain passwords.
     */
    @Column(nullable = false)
    private String password;

    /*
     * OWNER / ADMIN / MEMBER
     */
    @Enumerated(EnumType.STRING)

    @Column(nullable = false)
    private Role role;

    /*
     * User belongs to tenant/company
     */
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

}