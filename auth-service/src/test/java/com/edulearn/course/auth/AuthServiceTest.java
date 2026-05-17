package com.edulearn.course.auth;

import com.edulearn.auth.AuthServiceApplication;
import com.edulearn.auth.dto.AuthResponse;
import com.edulearn.auth.dto.LoginRequest;
import com.edulearn.auth.dto.RegisterRequest;
import com.edulearn.auth.entity.User;
import com.edulearn.auth.security.JwtUtil;
import com.edulearn.auth.service.AuthService;
import com.edulearn.auth.service.AuthServiceImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({AuthServiceImpl.class, JwtUtil.class, AuthServiceTest.TestConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Auth Service - JUnit Integration Tests")
class AuthServiceTest {

    /** Provides PasswordEncoder + scans correct base package for JPA repos/entities */
    @Configuration
    @AutoConfigurationPackage(basePackages = "com.edulearn.auth")
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private AuthService authService;

    private RegisterRequest buildRegisterRequest(String name, String email, String role) {
        RegisterRequest req = new RegisterRequest();
        req.setFullName(name);
        req.setEmail(email);
        req.setPassword("password123");
        req.setRole(role);
        return req;
    }

    @Test
    @Order(1)
    @DisplayName("Register: New user registers and receives a JWT token")
    void testRegister_success() {
        AuthResponse response = authService.register(
                buildRegisterRequest("John Doe", "john@test.com", "Student"));

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("John Doe", response.getFullName());
        assertEquals("Student", response.getRole());
        assertEquals("john@test.com", response.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("Register: Duplicate email throws RuntimeException")
    void testRegister_duplicateEmail() {
        authService.register(buildRegisterRequest("Jane", "jane@test.com", "Instructor"));
        assertThrows(RuntimeException.class,
                () -> authService.register(buildRegisterRequest("Jane2", "jane@test.com", "Student")));
    }

    @Test
    @Order(3)
    @DisplayName("Login: Valid credentials return a JWT token")
    void testLogin_success() {
        authService.register(buildRegisterRequest("Alice", "alice@test.com", "Instructor"));

        LoginRequest req = new LoginRequest();
        req.setEmail("alice@test.com");
        req.setPassword("password123");

        AuthResponse response = authService.login(req);
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("alice@test.com", response.getEmail());
    }

    @Test
    @Order(4)
    @DisplayName("Login: Wrong password throws RuntimeException")
    void testLogin_wrongPassword() {
        authService.register(buildRegisterRequest("Bob", "bob@test.com", "Student"));

        LoginRequest req = new LoginRequest();
        req.setEmail("bob@test.com");
        req.setPassword("wrongpassword");

        assertThrows(RuntimeException.class, () -> authService.login(req));
    }

    @Test
    @Order(5)
    @DisplayName("Login: Non-existent user throws RuntimeException")
    void testLogin_nonExistentEmail() {
        LoginRequest req = new LoginRequest();
        req.setEmail("nobody@test.com");
        req.setPassword("password123");

        assertThrows(RuntimeException.class, () -> authService.login(req));
    }

    @Test
    @Order(6)
    @DisplayName("GetUserByEmail: Returns correct user for given email")
    void testGetUserByEmail() {
        authService.register(buildRegisterRequest("Carol", "carol@test.com", "Student"));

        User user = authService.getUserByEmail("carol@test.com");
        assertNotNull(user);
        assertEquals("carol@test.com", user.getEmail());
        assertEquals("Student", user.getRole());
    }

    @Test
    @Order(7)
    @DisplayName("UpdateProfile: User bio and mobile are updated successfully")
    void testUpdateProfile() {
        AuthResponse reg = authService.register(
                buildRegisterRequest("Dave", "dave@test.com", "Instructor"));

        User patch = new User();
        patch.setFullName("Dave Updated");
        patch.setBio("10 years Java experience");
        patch.setMobile(9876543210L);

        User result = authService.updateProfile(reg.getUserId(), patch);
        assertNotNull(result);
        assertEquals("Dave Updated", result.getFullName());
        assertEquals("10 years Java experience", result.getBio());
        assertEquals(9876543210L, result.getMobile());
    }

    @Test
    @Order(8)
    @DisplayName("ChangePassword: Old password fails, new password succeeds")
    void testChangePassword() {
        AuthResponse reg = authService.register(
                buildRegisterRequest("Eve", "eve@test.com", "Student"));
        authService.changePassword(reg.getUserId(), "newpass456");

        LoginRequest oldLogin = new LoginRequest();
        oldLogin.setEmail("eve@test.com");
        oldLogin.setPassword("password123");
        assertThrows(RuntimeException.class, () -> authService.login(oldLogin));

        LoginRequest newLogin = new LoginRequest();
        newLogin.setEmail("eve@test.com");
        newLogin.setPassword("newpass456");
        assertNotNull(authService.login(newLogin).getToken());
    }

    @Test
    @Order(9)
    @DisplayName("SuspendUser: Status toggles Active ↔ Suspended")
    void testSuspendUser_togglesStatus() {
        AuthResponse reg = authService.register(
                buildRegisterRequest("Frank", "frank@test.com", "Student"));

        User suspended  = authService.suspendUser(reg.getUserId());
        assertEquals("Suspended", suspended.getStatus());

        User reactivated = authService.suspendUser(reg.getUserId());
        assertEquals("Active", reactivated.getStatus());
    }

    @Test
    @Order(10)
    @DisplayName("GetAllUsers: Returns all registered users")
    void testGetAllUsers() {
        authService.register(buildRegisterRequest("U1", "u1@test.com", "Student"));
        authService.register(buildRegisterRequest("U2", "u2@test.com", "Instructor"));

        List<User> users = authService.getAllUsers();
        assertNotNull(users);
        assertTrue(users.size() >= 2);
    }

    @Test
    @Order(11)
    @DisplayName("GetUsersByRole: Returns only users with the given role")
    void testGetUsersByRole() {
        authService.register(buildRegisterRequest("S1", "s1@test.com", "Student"));
        authService.register(buildRegisterRequest("S2", "s2@test.com", "Student"));
        authService.register(buildRegisterRequest("I1", "i1@test.com", "Instructor"));

        List<User> students = authService.getUsersByRole("Student");
        assertNotNull(students);
        assertTrue(students.size() >= 2);
        students.forEach(u -> assertEquals("Student", u.getRole()));
    }

    @Test
    @Order(12)
    @DisplayName("SearchUsers: Returns users whose name contains search term")
    void testSearchUsers() {
        authService.register(buildRegisterRequest("Pankaj Sharma", "pankaj@test.com", "Student"));
        authService.register(buildRegisterRequest("Pankaj Verma", "pankaj2@test.com", "Instructor"));
        authService.register(buildRegisterRequest("Ravi Kumar",   "ravi@test.com",    "Student"));

        List<User> results = authService.searchUsers("Pankaj");
        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    @Order(13)
    @DisplayName("DeleteAccount: Deleted user cannot be found by email")
    void testDeleteAccount() {
        AuthResponse reg = authService.register(
                buildRegisterRequest("Grace", "grace@test.com", "Student"));
        authService.deleteAccount(reg.getUserId());

        assertThrows(RuntimeException.class,
                () -> authService.getUserByEmail("grace@test.com"));
    }
}
