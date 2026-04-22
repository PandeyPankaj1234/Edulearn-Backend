package com.edulearn.course.auth.controller;

import com.edulearn.course.auth.dto.AuthResponse;
import com.edulearn.course.auth.dto.LoginRequest;
import com.edulearn.course.auth.dto.RegisterRequest;
import com.edulearn.course.auth.entity.User;
import com.edulearn.course.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // GET /api/auth/profile
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@RequestParam String email) {
        return ResponseEntity.ok(authService.getUserByEmail(email));
    }

    // PUT /api/auth/profile/{userId}
    @PutMapping("/profile/{userId}")
    public ResponseEntity<User> updateProfile(
            @PathVariable Long userId,
            @RequestBody User updatedUser) {
        return ResponseEntity.ok(authService.updateProfile(userId, updatedUser));
    }

    // PUT /api/auth/password/{userId}
    @PutMapping("/password/{userId}")
    public ResponseEntity<String> changePassword(
            @PathVariable Long userId,
            @RequestParam String newPassword) {
        authService.changePassword(userId, newPassword);
        return ResponseEntity.ok("Password changed successfully!");
    }

    // DELETE /api/auth/delete/{userId}
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long userId) {
        authService.deleteAccount(userId);
        return ResponseEntity.ok("Account deleted successfully!");
    }
}
