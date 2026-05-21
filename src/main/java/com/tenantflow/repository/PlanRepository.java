package com.tenantflow.repository;

import com.tenantflow.model.Plan;
import com.tenantflow.model.PlanName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {

    Optional<Plan> findByName(PlanName name);

    

}
