package com.library.com.payload.dto;

import com.library.com.domain.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    @NotNull(message="email is required")
    private String email;
    @NotNull(message = "Full Name is required")
    private String fullName;
    private UserRole role;
    @NotNull(message = "Password is required")
    private String password;
    private String phone;
    private String username;

    private LocalDateTime lastLogin;
}
