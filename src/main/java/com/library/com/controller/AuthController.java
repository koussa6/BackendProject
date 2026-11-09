package com.library.com.controller;


import com.library.com.service.AuthService;
import com.library.com.exception.UserException;
import com.library.com.payload.dto.UserDto;
import com.library.com.payload.request.ForgetPasswordRequest;
import com.library.com.payload.request.LoginRequest;
import com.library.com.payload.request.ResetPasswordRequest;
import com.library.com.payload.response.ApiResponse;
import com.library.com.payload.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "Create account for new users")
    public ResponseEntity<?>signup(@RequestBody @Valid UserDto dto) throws UserException {
        AuthResponse response=authService.signup(dto);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    @Operation(summary = "Log in and get a JWT")
    public ResponseEntity<?>login(@RequestBody @Valid LoginRequest req) throws UserException {
        AuthResponse response=authService.login(req.getEmail(),req.getPassword());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/forget-password")
    @Operation(summary = "Request if missing the password")
    public ResponseEntity<ApiResponse>forgetPassword(@RequestBody ForgetPasswordRequest req) throws UserException {
        authService.createPasswordResetToken(req.getEmail());
        ApiResponse resp=new ApiResponse("A reset link was sent to your email",true);
        return ResponseEntity.ok(resp);
    }
    @PostMapping
    @Operation(summary = "Reset password")
    public ResponseEntity<ApiResponse>resetPassword(@RequestBody ResetPasswordRequest req) throws Exception {
        authService.resetPassword(req.getToken(),req.getPassword());
        ApiResponse resp=new ApiResponse("Password reset successfully",true);
        return ResponseEntity.ok(resp);
    }
}
