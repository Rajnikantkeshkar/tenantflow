package com.tenantflow.dto;

import com.tenantflow.model.PlanName;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PlanRequestDto {

    @NotNull(message = "Plan name is required")
    private PlanName name;

    @NotNull(message = "Monthly price is required")

    @PositiveOrZero(message = "Monthly price must be zero or positive")
    private BigDecimal monthlyPrice;

    @NotNull(message = "API call limit is required")

    @Positive(message = "API call limit must be positive")
    private Long apiCallLimit;

    @NotNull(message = "Max users is required")

    @Positive(message = "Max users must be positive")
    private Integer maxUsers;

}