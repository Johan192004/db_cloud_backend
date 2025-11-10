package com.johan.db_cloud.dto;

import org.springframework.stereotype.Service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    
    private String token;
    private String message;

}
