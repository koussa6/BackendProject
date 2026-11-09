package com.library.com.service.impl;

import com.library.com.exception.UserException;
import com.library.com.mapper.UserMapper;
import com.library.com.modal.User;
import com.library.com.payload.dto.UserDto;
import com.library.com.repository.UserRepository;
import com.library.com.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public User getCurrentUser() throws UserException {
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByEmail(email);
        if(user==null){
            throw new UserException("user not found by given email");
        }
        return user;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User>users=userRepository.findAll();
        return users.stream().map(userMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).
                orElseThrow(()->new UsernameNotFoundException("user not found by id"+id));
    }
}
