package com.library.com.controller;



import com.library.com.exception.UserException;
import com.library.com.modal.User;
import com.library.com.payload.dto.UserDto;
import com.library.com.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User controller")
public class UserController {
    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("/profile")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<User>getUserProfile() throws UserException {
        return ResponseEntity.ok(userService.getCurrentUser());
    }
}
