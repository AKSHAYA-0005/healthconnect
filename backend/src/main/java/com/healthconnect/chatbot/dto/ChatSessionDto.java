package com.healthconnect.chatbot.dto;

import com.healthconnect.triage.dto.TriageResult;

import java.util.ArrayList;
import java.util.List;

public class ChatSessionDto {

    public static final String MANDATORY_DISCLAIMER =
            "This result is a preliminary automated triage recommendation and is not a medical diagnosis. If symptoms are severe, worsening, or concerning, seek professional medical care promptly.";

    private String sessionId;
    private String step; // "WELCOME", "SYMPTOMS", "AGE", "SEVERITY", "DURATION", "PRE_EXISTING", "COMPLETED"
    private List<ChatMessageDto> messages = new ArrayList<>();
    private TriageResult finalTriageResult;
    private boolean emergencyTriggered;
    private String disclaimer = MANDATORY_DISCLAIMER;

    // Temporary collected data state
    private String symptomsText;
    private Integer age;
    private Integer severityScale;
    private Integer durationDays;
    private String preExistingConditions;

    public ChatSessionDto() {
    }

    public ChatSessionDto(String sessionId) {
        this.sessionId = sessionId;
        this.step = "WELCOME";
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public List<ChatMessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessageDto> messages) {
        this.messages = messages;
    }

    public TriageResult getFinalTriageResult() {
        return finalTriageResult;
    }

    public void setFinalTriageResult(TriageResult finalTriageResult) {
        this.finalTriageResult = finalTriageResult;
    }

    public boolean isEmergencyTriggered() {
        return emergencyTriggered;
    }

    public void setEmergencyTriggered(boolean emergencyTriggered) {
        this.emergencyTriggered = emergencyTriggered;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getSymptomsText() {
        return symptomsText;
    }

    public void setSymptomsText(String symptomsText) {
        this.symptomsText = symptomsText;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSeverityScale() {
        return severityScale;
    }

    public void setSeverityScale(Integer severityScale) {
        this.severityScale = severityScale;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public String getPreExistingConditions() {
        return preExistingConditions;
    }

    public void setPreExistingConditions(String preExistingConditions) {
        this.preExistingConditions = preExistingConditions;
    }
}
