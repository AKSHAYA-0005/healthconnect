package com.healthconnect.triage;

import com.healthconnect.triage.dto.TriageInput;
import com.healthconnect.triage.dto.TriageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class RuleBasedTriageEngineTest {

    private RuleBasedTriageEngine triageEngine;

    @BeforeEach
    void setUp() {
        triageEngine = new RuleBasedTriageEngine();
        ReflectionTestUtils.setField(triageEngine, "emergencyNumber", "112");
    }

    @Test
    @DisplayName("Red Flag Test: Acute Chest Pain triggers HIGH urgency and emergency alert")
    void testChestPainRedFlag() {
        TriageInput input = new TriageInput();
        input.setSymptomsText("I have sharp chest pain spreading to my left arm");
        input.setAge(45);
        input.setSeverityScale(9);

        TriageResult result = triageEngine.evaluate(input);

        assertEquals("HIGH", result.getUrgencyLevel());
        assertTrue(result.isEmergency());
        assertEquals("112", result.getEmergencyNumber());
        assertTrue(result.getReasons().stream().anyMatch(r -> r.contains("Chest Pain")));
        assertEquals(TriageResult.MANDATORY_DISCLAIMER, result.getDisclaimer());
    }

    @Test
    @DisplayName("Red Flag Test: Sudden Numbness triggers HIGH urgency")
    void testSuddenNumbnessRedFlag() {
        TriageInput input = new TriageInput();
        input.setSuddenNumbness(true);
        input.setSymptomsText("Face drooping on one side");
        input.setAge(60);
        input.setSeverityScale(8);

        TriageResult result = triageEngine.evaluate(input);

        assertEquals("HIGH", result.getUrgencyLevel());
        assertTrue(result.isEmergency());
        assertEquals("Neurology", result.getRecommendedDepartment());
    }

    @Test
    @DisplayName("Scoring Test: Mild skin rash results in LOW urgency")
    void testLowUrgency() {
        TriageInput input = new TriageInput();
        input.setSymptomsText("Mild skin rash on my arm with slight itchiness");
        input.setAge(28);
        input.setSeverityScale(2);
        input.setDurationDays(1);

        TriageResult result = triageEngine.evaluate(input);

        assertEquals("LOW", result.getUrgencyLevel());
        assertFalse(result.isEmergency());
        assertEquals("Dermatology", result.getRecommendedDepartment());
    }

    @Test
    @DisplayName("Scoring Test: Vulnerable age (>65) with moderate symptoms results in MEDIUM or HIGH urgency")
    void testVulnerableAgeScoring() {
        TriageInput input = new TriageInput();
        input.setSymptomsText("Persistent stomach pain and dizziness");
        input.setAge(72);
        input.setSeverityScale(5);
        input.setDurationDays(4);

        TriageResult result = triageEngine.evaluate(input);

        assertNotEquals("LOW", result.getUrgencyLevel());
        assertTrue(result.getReasons().stream().anyMatch(r -> r.contains(">= 65 years")));
    }
}
