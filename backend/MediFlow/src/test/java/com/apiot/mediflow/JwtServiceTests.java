package com.apiot.mediflow;

import com.apiot.mediflow.auth.JwtService;
import com.apiot.mediflow.auth.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", Base64.getEncoder()
                .encodeToString("super-secret-key-1234567890-super-secret-key!".getBytes()));
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L);
    }

    @Test
    void shouldGenerateAndValidateToken() {
        User user = new User();
        user.setUsername("john");

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertEquals("john", jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void shouldDetectExpiredToken() throws InterruptedException {
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 1L);

        User user = new User();
        user.setUsername("john");

        String token = jwtService.generateToken(user);

        Thread.sleep(5);
        assertFalse(jwtService.isTokenValid(token, user));
    }
}
