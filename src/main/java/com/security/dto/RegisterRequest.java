package com.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String role;
    @JsonProperty("isActive")
    private boolean isActive = true;

    @JsonProperty("isNotLocked")
    private boolean isNotLocked = true;

    @JsonProperty("isChangePassword")
    private boolean isChangePassword = true;
}