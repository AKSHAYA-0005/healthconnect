package com.healthconnect.triage.dto;

import java.util.List;

public class TriageResult {

    public static final String MANDATORY_DISCLAIMER =
            "This result is a preliminary automated triage recommendation and is not a medical diagnosis. If symptoms are severe, worsening, or concerning, seek professional medical care promptly.";

    private String urgencyLevel; // LOW, MEDIUM, HIGH
    private String recommendedDepartment;
    private List<String> reasons;
    private boolean isEmergency;
    private String disclaimer = MANDATORY_DISCLAIMER;
    private String emergencyNumber = "112";

    public TriageResult() {
    }

    public TriageResult(String urgencyLevel, String recommendedDepartment, List<String> reasons, boolean isEmergency) {
        this.urgencyLevel = urgencyLevel;
        this.recommendedDepartment = recommendedDepartment;
        this.reasons = reasons;
        this.isEmergency = isEmergency;
    }

    public String getUrgencyLevel() {
        return urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public String getRecommendedDepartment() {
        return recommendedDepartment;
    }

    public void setRecommendedDepartment(String recommendedDepartment) {
        this.recommendedDepartment = recommendedDepartment;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public boolean isEmergency() {
        return isEmergency;
    }

    public void setEmergency(boolean emergency) {
        isEmergency = emergency;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }
}
