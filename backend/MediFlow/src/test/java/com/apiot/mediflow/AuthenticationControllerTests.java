package com.apiot.mediflow;

import com.apiot.mediflow.auth.*;
import com.apiot.mediflow.config.AuthConfig;
import com.apiot.mediflow.config.JwtAuthenticationFilter;
import com.apiot.mediflow.config.SecurityConfig;
import com.apiot.mediflow.exceptionHandler.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@Import({SecurityConfig.class, AuthConfig.class, GlobalExceptionHandler.class})
public class AuthenticationControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUpFilterMock() throws Exception {
        // Mock filtra JWT - puszcza request bez sprawdzania
        doAnswer(invocation -> {
            HttpServletRequest req = invocation.getArgument(0);
            HttpServletResponse res = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(req, res);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

        @Test
        void shouldReturnTokenWhenLoginIsSuccessful() throws Exception {
            LoginRequest loginRequest = new LoginRequest("testuser", "password");

            User mockUser = new User();
            mockUser.setId(1L);
            mockUser.setUsername("testuser");
            mockUser.setPassword("password");

            when(userRepository.findByUsername("testuser"))
                    .thenReturn(Optional.of(mockUser));

            when(jwtService.generateToken(mockUser))
                    .thenReturn("fake-jwt-token");
            when(jwtService.getExpirationTime()).thenReturn(3600000L);

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(new UsernamePasswordAuthenticationToken(
                            mockUser, null, mockUser.getAuthorities()
                    ));

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                    .andExpect(jsonPath("$.expiresIn").value(3600000L));
        }

        @Test
        void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
            LoginRequest loginRequest = new LoginRequest("unknownuser", "password");

            when(userRepository.findByUsername("unknownuser"))
                    .thenReturn(Optional.empty());

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(new UsernamePasswordAuthenticationToken(
                            "unknownuser", null, List.of()
                    ));

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string("Incorrect login or password"));
        }

        @Test
        void shouldReturnUnauthorizedWhenPasswordIsInvalid() throws Exception {
            LoginRequest loginRequest = new LoginRequest("testuser", "wrongpassword");

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Incorrect password"));

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string("Incorrect password"));
        }
}
