package com.library.com.service;

import com.library.com.exception.UserException;
import com.library.com.payload.dto.UserDto;
import com.library.com.payload.response.AuthResponse;

public interface AuthService {
    AuthResponse login(String username,String password) throws UserException;
    AuthResponse signup(UserDto req) throws UserException;

    void createPasswordResetToken(String email) throws UserException;
    void resetPassword(String token,String newPassword) throws Exception;

}
