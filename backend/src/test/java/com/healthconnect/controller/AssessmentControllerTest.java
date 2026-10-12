package com.healthconnect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.triage.dto.TriageInput;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AssessmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin@healthconnect.com", roles = {"ADMIN"})
    @DisplayName("Integration Test: Create Assessment successfully returns triage evaluation")
    void testCreateAssessmentEndpoint() throws Exception {
        TriageInput input = new TriageInput();
        input.setSymptomsText("Severe headache with sudden vision blur and nausea");
        input.setAge(42);
        input.setSeverityScale(7);
        input.setDurationDays(2);
        input.setGender("Female");

        mockMvc.perform(post("/api/assessments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.urgencyLevel").exists())
                .andExpect(jsonPath("$.recommendedDepartment").value("Neurology"))
                .andExpect(jsonPath("$.disclaimer").exists());
    }

    @Test
    @WithMockUser(username = "admin@healthconnect.com", roles = {"ADMIN"})
    @DisplayName("Integration Test: Get patient assessments endpoint")
    void testGetMyAssessmentsEndpoint() throws Exception {
        mockMvc.perform(get("/api/assessments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
