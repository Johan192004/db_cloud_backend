package com.johan.db_cloud.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.johan.db_cloud.dto.AuthResponse;
import com.johan.db_cloud.dto.LoginRequest;
import com.johan.db_cloud.dto.RegisterRequest;
import com.johan.db_cloud.exception.ConflictException;
import com.johan.db_cloud.exception.UnauthorizedException;
import com.johan.db_cloud.model.Individual;
import com.johan.db_cloud.model.Organization;
import com.johan.db_cloud.model.Role;
import com.johan.db_cloud.model.User;
import com.johan.db_cloud.repository.IndividualRepository;
import com.johan.db_cloud.repository.OrganizationRepository;
import com.johan.db_cloud.repository.UserRepository;
import com.johan.db_cloud.util.JwtUtil;

@Service
public class AuthService {
    
    private UserRepository userRepository;
    private IndividualRepository individualRepository;
    private OrganizationRepository organizationRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       IndividualRepository individualRepository,
                       OrganizationRepository organizationRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.individualRepository = individualRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request){
        if(userRepository.findByEmail(request.getEmail()) != null){
            throw new ConflictException("Email already registered");
        }

        Role role = Role.valueOf(request.getUserType().toUpperCase());  

        User user = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .userType(role)
            .build();

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getUserType().toString());

        if("INDIVIDUAL".equals(request.getUserType())){
            Individual individual = Individual.builder()
                .fullName(request.getFullName())
                .user(savedUser)
                .build();
            
                individualRepository.save(individual);
        } else if ("ORGANIZATION".equals(request.getUserType())){
            Organization organization = Organization.builder()
                .name(request.getOrgName())
                .user(savedUser)
                .build();
            
                organizationRepository.save(organization);
        }

        return AuthResponse.builder()
            .token(token)
            .message("Successful register")
            .build();
    }

    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UnauthorizedException());

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new UnauthorizedException();
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getUserType().toString());

        return AuthResponse.builder()
            .token(token)
            .message("Successful login")
            .build();
    }
}
