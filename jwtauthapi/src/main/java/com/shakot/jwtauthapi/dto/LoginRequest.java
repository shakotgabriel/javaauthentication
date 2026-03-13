package com.shakot.jwtauthapi.dto;

import lombok.*;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class LoginRequest {
    private String email;
    private String password;
}