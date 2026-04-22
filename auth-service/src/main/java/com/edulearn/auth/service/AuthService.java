package com.edulearn.course.auth.service;

import com.edulearn.course.auth.dto.AuthResponse;
import com.edulearn.course.auth.dto.LoginRequest;
import com.edulearn.course.auth.dto.RegisterRequest;
import com.edulearn.course.auth.entity.User;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    User getUserByEmail(String email);

    User updateProfile(Long userId, User updatedUser);

    void changePassword(Long userId, String newPassword);

    void deleteAccount(Long userId);
}
