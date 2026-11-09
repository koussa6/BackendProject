package com.library.com.service;

import com.library.com.exception.UserException;
import com.library.com.modal.User;
import com.library.com.payload.dto.UserDto;

import java.util.List;

public interface UserService {

    public User getCurrentUser() throws UserException;
    public List<UserDto> getAllUsers();
    User findById(Long id);
}
