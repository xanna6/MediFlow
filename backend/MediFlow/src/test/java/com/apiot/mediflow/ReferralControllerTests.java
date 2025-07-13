package com.apiot.mediflow;

import com.apiot.mediflow.referral.ReferralController;
import com.apiot.mediflow.referral.ReferralDto;
import com.apiot.mediflow.referral.ReferralService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReferralController.class)
public class ReferralControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReferralService referralService;

    @Test
    void shouldReturnListOfReferralsAsJson() throws Exception {
        // given
        List<ReferralDto> referralDtoList = List.of(
                new ReferralDto(1L, "Jan Kowalski", "A25000001", LocalDateTime.now()),
                new ReferralDto(2L, "Jakub Kozłowski", "A25000002", LocalDateTime.now())
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
        ReferralDto referralDto = new ReferralDto(1L, "Jan Kowalski", "A25000001", LocalDateTime.now());
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

}
