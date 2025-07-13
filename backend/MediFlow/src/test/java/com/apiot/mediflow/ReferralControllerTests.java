package com.apiot.mediflow;

import com.apiot.mediflow.referral.ReferralController;
import com.apiot.mediflow.referral.ReferralDto;
import com.apiot.mediflow.referral.ReferralService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
        when(referralService.getAllReferrals()).thenReturn(Collections.emptyList());

        String expectedJson = "[]";

        mockMvc.perform(get("/api/referrals"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

}
