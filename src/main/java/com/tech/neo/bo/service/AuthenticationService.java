package com.tech.neo.bo.service;

import com.tech.neo.bo.domain.entity.Account;
import com.tech.neo.bo.domain.entity.User;
import com.tech.neo.bo.domain.entity.dto.UserDto;
import com.tech.neo.bo.exception.NoRequisitesEx;
import com.tech.neo.bo.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service()
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(UserDto userDto) {
        if (Objects.isNull(userDto.getEmail()) && Objects.isNull(userDto.getPhoneNumber())) {
            throw new NoRequisitesEx("Should be present at least one contact information");
        }
        User user = new User();
        user.setFullName(userDto.getFullName());
        user.setLogin(userDto.getLogin());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.getEmails().add(userDto.getEmail());
        user.getPhoneNumbers().add(userDto.getPhoneNumber());
        user.setAccount(new Account(user, userDto.getInitialDeposit()));
        user.setBirthDate(userDto.getBirthDate());
        return userRepository.save(user);
    }

    public User authenticate(UserDto userDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getLogin(), userDto.getPassword()));
        return userRepository.findByLogin(userDto.getLogin()).orElseThrow();
    }
}