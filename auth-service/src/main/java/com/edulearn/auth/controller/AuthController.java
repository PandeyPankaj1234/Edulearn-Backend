package com.edulearn.auth.controller;

import com.edulearn.auth.dto.AuthResponse;
import com.edulearn.auth.dto.LoginRequest;
import com.edulearn.auth.dto.RegisterRequest;
import com.edulearn.auth.entity.User;
import com.edulearn.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")

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

    // ── ADMIN ENDPOINTS ─────────────────────────────────────────────────────

    // GET /api/auth/admin/users  — all users
    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    // GET /api/auth/admin/users/role/{role}  — filter by role
    @GetMapping("/admin/users/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(authService.getUsersByRole(role));
    }

    // PUT /api/auth/admin/users/{userId}/suspend  — toggle suspend
    @PutMapping("/admin/users/{userId}/suspend")
    public ResponseEntity<User> suspendUser(@PathVariable Long userId) {
        return ResponseEntity.ok(authService.suspendUser(userId));
    }

    // GET /api/auth/admin/users/search  — search by name
    @GetMapping("/admin/users/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String name) {
        return ResponseEntity.ok(authService.searchUsers(name));
    }

    // ── Google OAuth ─────────────────────────────────────────────────────────

    // POST /api/auth/google  — receive Google profile, return platform JWT
    @PostMapping("/google")
    public ResponseEntity<com.edulearn.auth.dto.AuthResponse> googleLogin(
            @RequestBody com.edulearn.auth.dto.GoogleAuthRequest request) {
        return ResponseEntity.ok(authService.googleLogin(request));
    }
}

