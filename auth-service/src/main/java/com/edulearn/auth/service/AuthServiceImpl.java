package com.edulearn.auth.service;

import com.edulearn.auth.dto.AuthResponse;
import com.edulearn.auth.dto.LoginRequest;
import com.edulearn.auth.dto.RegisterRequest;
import com.edulearn.auth.entity.User;
import com.edulearn.auth.repository.UserRepository;
import com.edulearn.auth.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Register logic
    @Override
    public AuthResponse register(RegisterRequest request) {

        // Email pehle se exist karta hai toh error
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered! Please use a different email or login instead.");
        }

        // Naya user banao
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setProvider("local");

        // Database mein save karo
        User saved = userRepository.save(user);

        // JWT token banao
        String token = jwtUtil.generateToken(saved.getEmail(), saved.getRole());

        return new AuthResponse(
                token,
                saved.getRole(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getUserId()
        );
    }

    // Login logic
    @Override
    public AuthResponse login(LoginRequest request) {

        // Email se user dhundho
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No account found with this email."));

        // Password match karo
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect password. Please try again.");
        }

        // Token banao
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return new AuthResponse(
                token,
                user.getRole(),
                user.getFullName(),
                user.getEmail(),
                user.getUserId()
        );
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
    }

    @Override
    public User updateProfile(Long userId, User updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        user.setFullName(updatedUser.getFullName());
        user.setBio(updatedUser.getBio());
        user.setMobile(updatedUser.getMobile());
        user.setProfilePicUrl(updatedUser.getProfilePicUrl());

        return userRepository.save(user);
    }

    @Override
    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void deleteAccount(Long userId) {
        userRepository.deleteById(userId);
    }

    // ── Admin implementations ─────────────────────────────────────────────

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return userRepository.findAllByRole(role);
    }

    @Override
    public User suspendUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found."));
        user.setStatus("Suspended".equals(user.getStatus()) ? "Active" : "Suspended");
        return userRepository.save(user);
    }

    @Override
    public List<User> searchUsers(String name) {
        return userRepository.findByFullNameContaining(name);
    }

    // ── Google OAuth ──────────────────────────────────────────────────────────

    @Override
    public com.edulearn.auth.dto.AuthResponse googleLogin(
            com.edulearn.auth.dto.GoogleAuthRequest request) {

        // 1. Find existing user by email
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            // 2. Auto-register: first-time Google sign-in creates a Student account
            user = new User();
            user.setFullName(request.getName() != null ? request.getName() : "Google User");
            user.setEmail(request.getEmail());
            // Random password — Google users never use password login
            user.setPasswordHash(passwordEncoder.encode(
                    java.util.UUID.randomUUID().toString()));
            user.setRole("Student");
            user.setProvider("google");
            user.setProfilePicUrl(request.getPictureUrl());
            user.setStatus("Active");
            user = userRepository.save(user);
        } else if ("Suspended".equals(user.getStatus())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN,
                    "Your account has been suspended.");
        } else {
            // Update profile picture from Google on each login
            if (request.getPictureUrl() != null) {
                user.setProfilePicUrl(request.getPictureUrl());
                userRepository.save(user);
            }
        }

        // 3. Issue our JWT exactly like normal login
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new com.edulearn.auth.dto.AuthResponse(
                token, user.getRole(), user.getFullName(),
                user.getEmail(), user.getUserId());
    }
}

