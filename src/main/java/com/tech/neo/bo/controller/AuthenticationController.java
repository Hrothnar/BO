package com.tech.neo.bo.controller;

import com.tech.neo.bo.domain.entity.User;
import com.tech.neo.bo.domain.entity.dto.LoginDto;
import com.tech.neo.bo.domain.entity.dto.UserDto;
import com.tech.neo.bo.service.AuthenticationService;
import com.tech.neo.bo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController()
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @Autowired()
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        User registeredUser = authenticationService.signup(userDto);
        return ResponseEntity.ok("User has been registered");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDto> authenticate(@RequestBody UserDto userDto) {
        User authenticatedUser = authenticationService.authenticate(userDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginDto loginDto = new LoginDto(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginDto);
    }
}