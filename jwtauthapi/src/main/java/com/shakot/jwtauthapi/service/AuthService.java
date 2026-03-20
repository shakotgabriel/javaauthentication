package com.shakot.jwtauthapi.service;

import com.shakot.jwtauthapi.dto.AuthResponse;
import com.shakot.jwtauthapi.dto.LoginRequest;
import com.shakot.jwtauthapi.dto.RegisterRequest;
import com.shakot.jwtauthapi.model.Role;
import com.shakot.jwtauthapi.model.User;
import com.shakot.jwtauthapi.repository.UserRepository;
import com.shakot.jwtauthapi.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
		       PasswordEncoder passwordEncoder,
		       AuthenticationManager authenticationManager,
		       JwtService jwtService) {
	this.userRepository = userRepository;
	this.passwordEncoder = passwordEncoder;
	this.authenticationManager = authenticationManager;
	this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
	if (userRepository.existsByEmail(request.getEmail())) {
	    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
	}

	User user = User.builder()
		.name(request.getName())
		.email(request.getEmail())
		.password(passwordEncoder.encode(request.getPassword()))
		.role(Role.ROLE_USER)
		.enabled(true)
		.build();

	User savedUser = userRepository.save(user);
	UserDetails userDetails = org.springframework.security.core.userdetails.User
		.withUsername(savedUser.getEmail())
		.password(savedUser.getPassword())
		.authorities(savedUser.getRole().name())
		.build();

	String token = jwtService.generateToken(Map.of("role", savedUser.getRole().name()), userDetails);
	return AuthResponse.builder()
		.token(token)
		.email(savedUser.getEmail())
		.role(savedUser.getRole().name())
		.build();
    }

    public AuthResponse login(LoginRequest request) {
	try {
	    authenticationManager.authenticate(
		    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
	    );
	} catch (BadCredentialsException ex) {
	    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
	}

	User user = userRepository.findByEmail(request.getEmail())
		.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

	UserDetails userDetails = org.springframework.security.core.userdetails.User
		.withUsername(user.getEmail())
		.password(user.getPassword())
		.authorities(user.getRole().name())
		.build();

	String token = jwtService.generateToken(Map.of("role", user.getRole().name()), userDetails);
	return AuthResponse.builder()
		.token(token)
		.email(user.getEmail())
		.role(user.getRole().name())
		.build();
    }
}
