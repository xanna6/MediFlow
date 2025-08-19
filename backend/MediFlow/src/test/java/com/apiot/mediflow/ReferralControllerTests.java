package com.apiot.mediflow;

import com.apiot.mediflow.auth.UserRepository;
import com.apiot.mediflow.config.AuthConfig;
import com.apiot.mediflow.config.JwtAuthenticationFilter;
import com.apiot.mediflow.config.SecurityConfig;
import com.apiot.mediflow.exceptionHandler.GlobalExceptionHandler;
import com.apiot.mediflow.referral.ReferralController;
import com.apiot.mediflow.referral.ReferralCreateDto;
import com.apiot.mediflow.referral.ReferralDto;
import com.apiot.mediflow.referral.ReferralService;
import com.apiot.mediflow.test.MedicalTestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReferralController.class)
@Import({SecurityConfig.class, AuthConfig.class, GlobalExceptionHandler.class})
@WithMockUser(username="doctor",roles={"DOCTOR"})
public class ReferralControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReferralService referralService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {
        // przepuszczane wszystkie requesty w mocku filtra
        doAnswer(invocation -> {
            FilterChain chain = invocation.getArgument(2);
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            chain.doFilter(request, response);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

    @Test
    void shouldReturnListOfReferralsAsJson() throws Exception {
        // given
        Set<MedicalTestDto> medicalTestDtos = Set.of(
                new MedicalTestDto(1L, "TSH", "Badanie funkcji tarczycy", 45.99F));
        List<ReferralDto> referralDtoList = List.of(
                new ReferralDto(1L, "Jan Kowalski", "A25000001", LocalDateTime.now(), medicalTestDtos),
                new ReferralDto(2L, "Jakub Kozłowski", "A25000002", LocalDateTime.now(), medicalTestDtos)
        );
        when(referralService.getAllReferrals()).thenReturn(referralDtoList);

        String expectedJson = new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writeValueAsString(referralDtoList);

        // when + then
        mockMvc.perform(get("/api/referrals"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldReturnEmptyJsonArray() throws Exception {
        // given
        when(referralService.getAllReferrals()).thenReturn(Collections.emptyList());

        String expectedJson = "[]";

        // when + then
        mockMvc.perform(get("/api/referrals"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldReturnReferralDtoById() throws Exception {
        //given
        Set<MedicalTestDto> medicalTestDtos = Set.of(
                new MedicalTestDto(1L, "TSH", "Badanie funkcji tarczycy", 45.99F),
                new MedicalTestDto(2L, "Glukoza", "Badanie poziomu glukozy na czczo", 19.99F));
        ReferralDto referralDto = new ReferralDto(1L, "Jan Kowalski", "A25000001",
                LocalDateTime.now(), medicalTestDtos);

        when(referralService.getReferralById(1L)).thenReturn(referralDto);

        String expectedJson = new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writeValueAsString(referralDto);

        //when + then
        mockMvc.perform(get("/api/referrals/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldReturn404WhenProductNotFound() throws Exception {
        // given
        Long nonExistingId = 999L;
        when(referralService.getReferralById(nonExistingId))
                .thenThrow(new EntityNotFoundException("Referral with id 999 not found"));

        // when + then
        mockMvc.perform(get("/api/referrals/{id}", nonExistingId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Referral with id 999 not found"));
    }

    @Test
    void shouldCreateReferralAndReturn201() throws Exception {
        // given
        Set<MedicalTestDto> medicalTestDtos = Set.of(
                new MedicalTestDto(1L, "TSH", "Badanie funkcji tarczycy", 45.99F),
                new MedicalTestDto(2L, "Glukoza", "Badanie poziomu glukozy na czczo", 19.99F));

        ReferralCreateDto referralCreateDto = new ReferralCreateDto("Jan Kowalski", "A25000003", Set.of(1L, 2L));
        ReferralDto savedReferral = new ReferralDto(5L, "Jan Kowalski", "A25000003",
                LocalDateTime.now(), medicalTestDtos);

        when(referralService.createReferral(any(ReferralCreateDto.class))).thenReturn(savedReferral);

        String expectedJson = new ObjectMapper().registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writeValueAsString(savedReferral);

        // when + then
        mockMvc.perform(post("/api/referrals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule())
                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).writeValueAsString((referralCreateDto))))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/referrals/5"))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
        String invalidRequest = """
            {
                "referrer": "",
                "referralNumber": "",
                "medicalTestIds": []
            }
        """;

        // when + then
        mockMvc.perform(post("/api/referrals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists())
                .andExpect(jsonPath("$.errors.referrer").value("must not be blank"))
                .andExpect(jsonPath("$.errors.referralNumber").value("must not be blank"))
                .andExpect(jsonPath("$.errors.medicalTestIds").value("must not be empty"));;
    }

}
