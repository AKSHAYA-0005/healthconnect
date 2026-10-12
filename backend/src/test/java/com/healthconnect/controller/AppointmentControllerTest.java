package com.healthconnect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.dto.AppointmentDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin@healthconnect.com", roles = {"ADMIN"})
    @DisplayName("Integration Test: Fetch patient appointments list")
    void testGetMyAppointments() throws Exception {
        mockMvc.perform(get("/api/appointments/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "admin@healthconnect.com", roles = {"ADMIN"})
    @DisplayName("Integration Test: Book appointment with seeded doctor ID 1")
    void testBookAppointment() throws Exception {
        AppointmentDto dto = new AppointmentDto();
        dto.setDoctorId(1L);
        dto.setAppointmentDate(LocalDateTime.now().plusDays(2));
        dto.setReason("Routine cardiovascular checkup based on triage");

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("REQUESTED"));
    }
}
