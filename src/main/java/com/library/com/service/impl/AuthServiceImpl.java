package com.library.com.service.impl;

import com.library.com.service.AuthService;
import com.library.com.configurations.JwtProvider;
import com.library.com.domain.UserRole;
import com.library.com.exception.UserException;
import com.library.com.mapper.UserMapper;
import com.library.com.modal.PasswordResetToken;
import com.library.com.modal.User;
import com.library.com.payload.dto.UserDto;
import com.library.com.payload.response.AuthResponse;
import com.library.com.repository.PasswordResetTokenRepo;
import com.library.com.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;
    private final CustomUserServiceImpl customUserService;
    private final PasswordResetTokenRepo passwordResetTokenRepo;
    private final EmailServieImpl emailServie;
    private final CompromisedPasswordChecker compromisedPasswordChecker;
    @Override
    public AuthResponse login(String email, String password) throws UserException {
        User user=userRepository.findByEmail(email);
        if(user==null){
            throw new UserException("user not found by given email"+email);
        }
        Authentication authentication=authenticate(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);


// update last Login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        AuthResponse response = new AuthResponse();
        response.setTitle("Login success");
        response.setMessage("Welcome Back " + email);
        response.setJwt(token);
        response.setUser(userMapper.toDTO(user));

        return response;    }

    private Authentication authenticate(String username, String password) throws UserException {
        UserDetails userDetails=customUserService.loadUserByUsername(username);
        if(userDetails==null){
            throw new UserException("user not found with given email"+username);
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new UserException("password not match");
        }
        return new UsernamePasswordAuthenticationToken(username,null,userDetails.getAuthorities());
    }

    @Override
    public AuthResponse signup(UserDto req) throws UserException {

        User exisitingUser=userRepository.findByEmail(req.getEmail());
        if(exisitingUser!=null) {
            throw new UserException("Email already used");
        }
        if(compromisedPasswordChecker.check(req.getPassword()).isCompromised()){
            throw new UserException("you have to enter a stronger password");
        }
        User createdUser = new User();
        createdUser.setEmail(req.getEmail());
        createdUser.setPassword(passwordEncoder.encode(req.getPassword()));
        createdUser.setPhone(req.getPhone());
        createdUser.setFullName(req.getFullName());
        createdUser.setRole(UserRole.ROLE_USER);

        User savedUser = userRepository.save(createdUser);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassword()
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setTitle("Welcome " + savedUser.getFullName());
        authResponse.setMessage("register success");
        authResponse.setUser(userMapper.toDTO(savedUser));

        return authResponse;
    }
    @Transactional
    public void createPasswordResetToken(String email) throws UserException {
        String frontendUrl="http://localhost:5173";
        User user=userRepository.findByEmail(email);
        if(user==null){
            throw new UserException("user not found by given email");
        }
        String token= UUID.randomUUID().toString();
        PasswordResetToken resetToken=PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(5)).build();
        passwordResetTokenRepo.save(resetToken);
        String resetLink=frontendUrl+token;
        String subject="Password Reset Request!";
        String body="You requested to reset your password. Use this link (valid for 5 min) "+resetLink;
        emailServie.sendEmail(user.getEmail(), subject,body);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) throws Exception {
        PasswordResetToken resetToken=passwordResetTokenRepo.findByToken(token)
                .orElseThrow(()->new Exception("token not valid!"));
        if(resetToken.isExpired()){
            passwordResetTokenRepo.delete(resetToken);
            throw new Exception("token expired");
        }
        User user=resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetTokenRepo.delete(resetToken);
    }
}
