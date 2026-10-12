package com.healthconnect.chatbot.dto;

import java.time.LocalDateTime;

public class ChatMessageDto {

    private String sender; // "USER" or "BOT"
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();
    private boolean isEmergencyAlert;
    private String emergencyBanner;

    public ChatMessageDto() {
    }

    public ChatMessageDto(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public ChatMessageDto(String sender, String message, boolean isEmergencyAlert, String emergencyBanner) {
        this.sender = sender;
        this.message = message;
        this.isEmergencyAlert = isEmergencyAlert;
        this.emergencyBanner = emergencyBanner;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isEmergencyAlert() {
        return isEmergencyAlert;
    }

    public void setEmergencyAlert(boolean emergencyAlert) {
        isEmergencyAlert = emergencyAlert;
    }

    public String getEmergencyBanner() {
        return emergencyBanner;
    }

    public void setEmergencyBanner(String emergencyBanner) {
        this.emergencyBanner = emergencyBanner;
    }
}
