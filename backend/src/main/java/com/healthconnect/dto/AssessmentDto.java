package com.healthconnect.dto;

import com.healthconnect.entity.SymptomAssessment;

import java.time.LocalDateTime;

public class AssessmentDto {

    public static final String MANDATORY_DISCLAIMER =
            "This result is a preliminary automated triage recommendation and is not a medical diagnosis. If symptoms are severe, worsening, or concerning, seek professional medical care promptly.";

    private Long id;
    private UserDto patient;
    private String symptomsText;
    private Integer age;
    private String gender;
    private Integer severityScale;
    private Integer durationDays;
    private String preExistingConditions;
    private String urgencyLevel;
    private String recommendedDepartment;
    private String reasonsJson;
    private boolean isEmergencyAlert;
    private LocalDateTime createdAt;
    private String disclaimer = MANDATORY_DISCLAIMER;

    public AssessmentDto() {
    }

    public AssessmentDto(SymptomAssessment assessment) {
        this.id = assessment.getId();
        if (assessment.getPatient() != null) {
            this.patient = new UserDto(assessment.getPatient());
        }
        this.symptomsText = assessment.getSymptomsText();
        this.age = assessment.getAge();
        this.gender = assessment.getGender();
        this.severityScale = assessment.getSeverityScale();
        this.durationDays = assessment.getDurationDays();
        this.preExistingConditions = assessment.getPreExistingConditions();
        this.urgencyLevel = assessment.getUrgencyLevel();
        this.recommendedDepartment = assessment.getRecommendedDepartment();
        this.reasonsJson = assessment.getReasonsJson();
        this.isEmergencyAlert = assessment.isEmergencyAlert();
        this.createdAt = assessment.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getPatient() {
        return patient;
    }

    public void setPatient(UserDto patient) {
        this.patient = patient;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getReasonsJson() {
        return reasonsJson;
    }

    public void setReasonsJson(String reasonsJson) {
        this.reasonsJson = reasonsJson;
    }

    public boolean isEmergencyAlert() {
        return isEmergencyAlert;
    }

    public void setEmergencyAlert(boolean emergencyAlert) {
        isEmergencyAlert = emergencyAlert;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }
}
