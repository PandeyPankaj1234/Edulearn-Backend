package com.edulearn.course.auth;

import com.edulearn.auth.AuthServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AuthServiceApplication.class)
class AuthServiceApplicationTests {

    @Test
    void contextLoads() {
        // Verifies the Spring context loads successfully with H2 in test scope
    }
}
