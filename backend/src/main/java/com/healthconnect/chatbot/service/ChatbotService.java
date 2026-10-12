package com.healthconnect.chatbot.service;

import com.healthconnect.chatbot.dto.ChatMessageDto;
import com.healthconnect.chatbot.dto.ChatSessionDto;
import com.healthconnect.triage.TriageEngine;
import com.healthconnect.triage.dto.TriageInput;
import com.healthconnect.triage.dto.TriageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatbotService {

    @Value("${app.emergency.number:112}")
    private String emergencyNumber;

    @Autowired
    private TriageEngine triageEngine;

    private final Map<String, ChatSessionDto> activeSessions = new ConcurrentHashMap<>();

    private static final List<String> EMERGENCY_KEYWORDS = List.of(
            "chest pain", "shortness of breath", "stroke", "numbness",
            "fainted", "unconscious", "bleeding", "cannot breathe",
            "heart attack", "anaphylaxis", "seizure", "hemorrhage"
    );

    public ChatSessionDto startNewSession() {
        String sessionId = UUID.randomUUID().toString();
        ChatSessionDto session = new ChatSessionDto(sessionId);

        String welcomeMsg = "Hello! I am your HEALTHCONNECT Assistant. " +
                "I will ask you a few questions to evaluate your symptoms and guide you to the right department. " +
                "Please describe your current symptoms in detail.";

        session.getMessages().add(new ChatMessageDto("BOT", welcomeMsg));
        session.setStep("SYMPTOMS");
        activeSessions.put(sessionId, session);
        return session;
    }

    public ChatSessionDto processUserMessage(String sessionId, String userMessage) {
        ChatSessionDto session = activeSessions.get(sessionId);
        if (session == null) {
            session = startNewSession();
        }

        // Add user message to history
        session.getMessages().add(new ChatMessageDto("USER", userMessage));

        String lowerMsg = userMessage != null ? userMessage.toLowerCase(Locale.ROOT) : "";

        // Check for emergency keywords at ANY stage
        for (String keyword : EMERGENCY_KEYWORDS) {
            if (lowerMsg.contains(keyword)) {
                return handleEmergencyTrigger(session, keyword);
            }
        }

        switch (session.getStep()) {
            case "SYMPTOMS":
                session.setSymptomsText(userMessage);
                session.getMessages().add(new ChatMessageDto("BOT", "Thank you. What is your age in years? (e.g. 35)"));
                session.setStep("AGE");
                break;

            case "AGE":
                try {
                    int age = Integer.parseInt(userMessage.replaceAll("[^0-9]", ""));
                    session.setAge(age);
                    session.getMessages().add(new ChatMessageDto("BOT", "Got it. On a scale of 1 to 10 (1=mild, 10=unbearable), how severe are your symptoms?"));
                    session.setStep("SEVERITY");
                } catch (Exception e) {
                    session.getMessages().add(new ChatMessageDto("BOT", "Please enter a valid numeric age in years (e.g. 30)."));
                }
                break;

            case "SEVERITY":
                try {
                    int severity = Integer.parseInt(userMessage.replaceAll("[^0-9]", ""));
                    if (severity < 1 || severity > 10) {
                        session.getMessages().add(new ChatMessageDto("BOT", "Please enter a rating between 1 and 10."));
                    } else {
                        session.setSeverityScale(severity);
                        session.getMessages().add(new ChatMessageDto("BOT", "How many days have you been experiencing these symptoms? (e.g. 2)"));
                        session.setStep("DURATION");
                    }
                } catch (Exception e) {
                    session.getMessages().add(new ChatMessageDto("BOT", "Please enter a valid number from 1 to 10 for severity."));
                }
                break;

            case "DURATION":
                try {
                    int duration = Integer.parseInt(userMessage.replaceAll("[^0-9]", ""));
                    session.setDurationDays(duration);
                    session.getMessages().add(new ChatMessageDto("BOT", "Do you have any pre-existing medical conditions? (e.g. Diabetes, Hypertension, Asthma, or type 'None')"));
                    session.setStep("PRE_EXISTING");
                } catch (Exception e) {
                    session.getMessages().add(new ChatMessageDto("BOT", "Please enter the duration in number of days (e.g. 3)."));
                }
                break;

            case "PRE_EXISTING":
                session.setPreExistingConditions(userMessage);
                // Evaluate using Triage Engine
                TriageInput input = new TriageInput();
                input.setSymptomsText(session.getSymptomsText());
                input.setAge(session.getAge());
                input.setSeverityScale(session.getSeverityScale());
                input.setDurationDays(session.getDurationDays());
                input.setPreExistingConditions(session.getPreExistingConditions());

                TriageResult result = triageEngine.evaluate(input);
                session.setFinalTriageResult(result);
                session.setStep("COMPLETED");

                String completionMsg = String.format(
                        "Assessment Completed! Recommended Urgency: %s | Department: %s.",
                        result.getUrgencyLevel(), result.getRecommendedDepartment()
                );
                session.getMessages().add(new ChatMessageDto("BOT", completionMsg));
                break;

            case "COMPLETED":
                session.getMessages().add(new ChatMessageDto("BOT", "Your assessment is already complete. You can view your recommendation below or start a new triage session."));
                break;

            default:
                session.getMessages().add(new ChatMessageDto("BOT", "Let's start over. Please describe your symptoms."));
                session.setStep("SYMPTOMS");
                break;
        }

        return session;
    }

    private ChatSessionDto handleEmergencyTrigger(ChatSessionDto session, String keyword) {
        session.setEmergencyTriggered(true);
        session.setStep("COMPLETED");

        String bannerText = "Possible emergency. Contact your local emergency number (" + emergencyNumber + ") or go to the nearest emergency department now.";

        ChatMessageDto botAlert = new ChatMessageDto("BOT",
                "EMERGENCY ALERT: Potential critical symptom detected (" + keyword + "). " + bannerText,
                true, bannerText);

        session.getMessages().add(botAlert);

        List<String> reasons = List.of("Red-flag keyword detected in chat conversation: " + keyword);
        TriageResult emergencyResult = new TriageResult("HIGH", "Emergency / Cardiology", reasons, true);
        emergencyResult.setEmergencyNumber(emergencyNumber);
        session.setFinalTriageResult(emergencyResult);

        return session;
    }

    public ChatSessionDto getSession(String sessionId) {
        return activeSessions.get(sessionId);
    }
}
