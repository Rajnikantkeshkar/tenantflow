package com.tenantflow.service;

import com.tenantflow.dto.LoginRequestDto;
import com.tenantflow.exception.ResourceNotFoundException;
import com.tenantflow.model.Tenant;
import com.tenantflow.model.User;
import com.tenantflow.repository.TenantRepository;
import com.tenantflow.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            TenantRepository tenantRepository,
            BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /*
     * Register User
     */
    public User register(User user, java.util.UUID tenantId) {

        logger.info("Registering user with email: {}", user.getEmail());

        Tenant tenant = tenantRepository
                .findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant not found"));

        /*
         * Encrypt password
         */
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setTenant(tenant);

        User savedUser = userRepository.save(user);

        logger.info("User registered successfully");

        return savedUser;
    }

    /*
     * Login User
     */
    public String login(LoginRequestDto dto) {

        logger.info("Login attempt for email: {}", dto.getEmail());

        User user = userRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Invalid email or password"));

        /*
         * Verify password
         */
        boolean passwordMatches = passwordEncoder.matches(
                        dto.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {
            throw new ResourceNotFoundException("Invalid email or password");
        }

        /*
         * Generate JWT
         */
        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getTenant().getId().toString()
        );

        logger.info("JWT generated successfully");

        return token;
    }

}
