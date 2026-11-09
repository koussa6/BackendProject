package com.library.com.mapper;

import com.library.com.modal.User;
import com.library.com.payload.dto.UserDto;
import org.springframework.stereotype.Component; // Import this

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component // 1. Add this so it can be injected
public class UserMapper {

    public UserDto toDTO(User user) {
        if (user == null) return null;
        UserDto userDTO = new UserDto();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullName(user.getFullName());
        userDTO.setPhone(user.getPhone());
        userDTO.setLastLogin(user.getLastLogin());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public List<UserDto> toDTOList(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Set<UserDto> toDTOSet(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toSet());
    }

    public User toEntity(UserDto userDTO) {
        return User.builder()
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .createdAt(LocalDateTime.now())
                .phone(userDTO.getPhone())
                .fullName(userDTO.getFullName())
                .role(userDTO.getRole())
                .build();
    }
}