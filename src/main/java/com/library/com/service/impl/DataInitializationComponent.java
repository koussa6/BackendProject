package com.library.com.service.impl;

import com.library.com.domain.UserRole;
import com.library.com.modal.User;
import com.library.com.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializationComponent implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args){
        initializeAdminUser();
    }
    private void initializeAdminUser(){
        String adminEmail="codewithelia@gmail.com";
        String adminPassword="*eliakoussa123456*";
        if(userRepository.findByEmail(adminEmail)==null){
            User user=User.builder()
                    .password(passwordEncoder.encode(adminPassword))
                    .email(adminEmail)
                    .role(UserRole.ROLE_ADMIN).build();

            User admin=userRepository.save(user);
        }
    }
}
