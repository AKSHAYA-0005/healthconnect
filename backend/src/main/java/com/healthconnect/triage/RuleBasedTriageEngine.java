package com.healthconnect.triage;

import com.healthconnect.triage.dto.TriageInput;
import com.healthconnect.triage.dto.TriageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class RuleBasedTriageEngine implements TriageEngine {

    @Value("${app.emergency.number:112}")
    private String emergencyNumber;

    @Override
    public TriageResult evaluate(TriageInput input) {
        List<String> reasons = new ArrayList<>();
        boolean isEmergency = false;
        String text = input.getSymptomsText() != null ? input.getSymptomsText().toLowerCase(Locale.ROOT) : "";

        // 1. Red-Flag Emergency Checks
        if (input.isChestPain() || text.contains("chest pain") || text.contains("angina") || text.contains("heart attack")) {
            isEmergency = true;
            reasons.add("Red-flag symptom detected: Severe/Acute Chest Pain suggesting potential cardiac event.");
        }
        if (input.isShortnessOfBreath() || text.contains("shortness of breath") || text.contains("gasping") || text.contains("cannot breathe") || text.contains("anaphylaxis")) {
            isEmergency = true;
            reasons.add("Red-flag symptom detected: Acute Respiratory Distress or severe dyspnea.");
        }
        if (input.isSuddenNumbness() || text.contains("sudden numbness") || text.contains("stroke") || text.contains("face drooping") || text.contains("slurred speech")) {
            isEmergency = true;
            reasons.add("Red-flag symptom detected: Sudden neurological deficit suggesting acute stroke or TIA.");
        }
        if (input.isLossOfConsciousness() || text.contains("loss of consciousness") || text.contains("fainted") || text.contains("passed out") || text.contains("unconscious")) {
            isEmergency = true;
            reasons.add("Red-flag symptom detected: Syncope or loss of consciousness.");
        }
        if (input.isSevereBleeding() || text.contains("severe bleeding") || text.contains("uncontrolled bleeding") || text.contains("hemorrhage")) {
            isEmergency = true;
            reasons.add("Red-flag symptom detected: Active severe/uncontrolled bleeding.");
        }

        // If red-flag fired, urgency is automatically HIGH
        if (isEmergency) {
            String department = determineDepartment(text, input);
            if (department.equalsIgnoreCase("General Medicine")) {
                department = "Emergency / Cardiology";
            }
            TriageResult result = new TriageResult("HIGH", department, reasons, true);
            result.setEmergencyNumber(emergencyNumber);
            return result;
        }

        // 2. Score Calculation for Non-Emergency Evaluation
        int totalScore = 0;

        // Base severity score (Scale 1-10)
        int severity = input.getSeverityScale() != null ? input.getSeverityScale() : 5;
        totalScore += severity * 2;
        reasons.add(String.format("Patient self-reported symptom severity level: %d/10 (+%d pts).", severity, severity * 2));

        // Age vulnerability factor
        int age = input.getAge() != null ? input.getAge() : 30;
        if (age >= 65) {
            totalScore += 4;
            reasons.add("Vulnerable age category (>= 65 years) increases urgency risks (+4 pts).");
        } else if (age <= 5) {
            totalScore += 3;
            reasons.add("Pediatric age group (<= 5 years) requires earlier intervention (+3 pts).");
        }

        // Duration factor
        int duration = input.getDurationDays() != null ? input.getDurationDays() : 1;
        if (duration > 14) {
            totalScore += 3;
            reasons.add("Chronic / persistent symptoms (> 14 days) needing specialized evaluation (+3 pts).");
        } else if (duration >= 3) {
            totalScore += 2;
            reasons.add("Symptom duration of 3+ days indicates non-transient condition (+2 pts).");
        }

        // Co-morbidities
        if (input.getPreExistingConditions() != null && !input.getPreExistingConditions().isBlank()) {
            totalScore += 3;
            reasons.add("Pre-existing medical conditions noted: " + input.getPreExistingConditions() + " (+3 pts).");
        }

        // High Fever check
        if (input.isFever() || text.contains("fever") || text.contains("high temperature")) {
            totalScore += 2;
            reasons.add("Fever present (+2 pts).");
        }

        // 3. Determine Urgency Level
        String urgencyLevel;
        if (totalScore >= 16 || severity >= 8) {
            urgencyLevel = "HIGH";
        } else if (totalScore >= 9 || severity >= 5) {
            urgencyLevel = "MEDIUM";
        } else {
            urgencyLevel = "LOW";
        }

        // 4. Department Recommendation Routing
        String department = determineDepartment(text, input);

        TriageResult result = new TriageResult(urgencyLevel, department, reasons, false);
        result.setEmergencyNumber(emergencyNumber);
        return result;
    }

    private String determineDepartment(String text, TriageInput input) {
        if (input.isChestPain() || text.contains("chest") || text.contains("heart") || text.contains("palpitation") || text.contains("bp") || text.contains("hypertension")) {
            return "Cardiology";
        }
        if (input.isShortnessOfBreath() || text.contains("breath") || text.contains("cough") || text.contains("asthma") || text.contains("lung") || text.contains("wheezing")) {
            return "Pulmonology";
        }
        if (input.isSuddenNumbness() || text.contains("headache") || text.contains("dizziness") || text.contains("migraine") || text.contains("seizure") || text.contains("nerve") || text.contains("vertigo")) {
            return "Neurology";
        }
        if (text.contains("stomach") || text.contains("nausea") || text.contains("vomit") || text.contains("diarrhea") || text.contains("acid") || text.contains("abdomen") || text.contains("gut")) {
            return "Gastroenterology";
        }
        if (text.contains("skin") || text.contains("rash") || text.contains("itch") || text.contains("eczema") || text.contains("acne") || text.contains("allergy")) {
            return "Dermatology";
        }
        if (text.contains("bone") || text.contains("joint") || text.contains("fracture") || text.contains("back pain") || text.contains("sprain") || text.contains("knee") || text.contains("muscle")) {
            return "Orthopedics";
        }
        return "General Medicine";
    }
}
