package com.healthconnect.triage.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TriageInput {

    @NotBlank(message = "Symptoms description is required")
    private String symptomsText;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age must be >= 0")
    @Max(value = 120, message = "Age must be <= 120")
    private Integer age;

    private String gender;

    @NotNull(message = "Severity scale is required")
    @Min(value = 1, message = "Severity scale must be between 1 and 10")
    @Max(value = 10, message = "Severity scale must be between 1 and 10")
    private Integer severityScale;

    @NotNull(message = "Duration in days is required")
    @Min(value = 0, message = "Duration must be >= 0")
    private Integer durationDays;

    private String preExistingConditions;

    // Direct Red-flag toggle flags
    private boolean fever;
    private boolean chestPain;
    private boolean shortnessOfBreath;
    private boolean suddenNumbness;
    private boolean severeBleeding;
    private boolean lossOfConsciousness;

    public TriageInput() {
    }

    public TriageInput(String symptomsText, Integer age, String gender, Integer severityScale, Integer durationDays, String preExistingConditions) {
        this.symptomsText = symptomsText;
        this.age = age;
        this.gender = gender;
        this.severityScale = severityScale;
        this.durationDays = durationDays;
        this.preExistingConditions = preExistingConditions;
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

    public boolean isFever() {
        return fever;
    }

    public void setFever(boolean fever) {
        this.fever = fever;
    }

    public boolean isChestPain() {
        return chestPain;
    }

    public void setChestPain(boolean chestPain) {
        this.chestPain = chestPain;
    }

    public boolean isShortnessOfBreath() {
        return shortnessOfBreath;
    }

    public void setShortnessOfBreath(boolean shortnessOfBreath) {
        this.shortnessOfBreath = shortnessOfBreath;
    }

    public boolean isSuddenNumbness() {
        return suddenNumbness;
    }

    public void setSuddenNumbness(boolean suddenNumbness) {
        this.suddenNumbness = suddenNumbness;
    }

    public boolean isSevereBleeding() {
        return severeBleeding;
    }

    public void setSevereBleeding(boolean severeBleeding) {
        this.severeBleeding = severeBleeding;
    }

    public boolean isLossOfConsciousness() {
        return lossOfConsciousness;
    }

    public void setLossOfConsciousness(boolean lossOfConsciousness) {
        this.lossOfConsciousness = lossOfConsciousness;
    }
}
