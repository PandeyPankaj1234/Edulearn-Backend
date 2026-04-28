package com.edulearn.auth.service;

import com.edulearn.auth.dto.AuthResponse;
import com.edulearn.auth.dto.LoginRequest;
import com.edulearn.auth.dto.RegisterRequest;
import com.edulearn.auth.entity.User;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    User getUserByEmail(String email);

    User updateProfile(Long userId, User updatedUser);

    void changePassword(Long userId, String newPassword);

    void deleteAccount(Long userId);
}
