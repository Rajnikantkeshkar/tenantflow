package com.tenantflow.dto;

import com.tenantflow.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserResponseDto {

    private UUID id;
    private String name;
    private String email;
    private Role role;
    private String companyName;

}
