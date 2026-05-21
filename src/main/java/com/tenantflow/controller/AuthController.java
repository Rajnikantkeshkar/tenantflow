package com.tenantflow.controller;

import com.tenantflow.dto.LoginRequestDto;
import com.tenantflow.dto.LoginResponseDto;
import com.tenantflow.dto.UserRequestDto;
import com.tenantflow.dto.UserResponseDto;
import com.tenantflow.model.User;
import com.tenantflow.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /*
     * Register API
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto dto) {

        User user = mapToEntity(dto);
        User savedUser = userService.register(user, dto.getTenantId());

        return new ResponseEntity<>(mapToResponse(savedUser), HttpStatus.CREATED);
    }

    /*
     * Login API
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {

        String token = userService.login(dto);

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    /*
     * DTO -> Entity
     */
    private User mapToEntity(UserRequestDto dto) {

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());

        return user;
    }

    /*
     * Entity -> Response DTO
     */
    private UserResponseDto mapToResponse(User user) {
        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setCompanyName(user.getTenant().getCompanyName());
        
        return response;
    }

}
