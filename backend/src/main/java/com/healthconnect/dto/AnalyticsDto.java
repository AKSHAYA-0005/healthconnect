package com.healthconnect.dto;

import java.util.Map;

public class AnalyticsDto {

    private long totalPatients;
    private long totalDoctors;
    private long totalAssessments;
    private long totalAppointments;
    private Map<String, Long> urgencyBreakdown; // LOW, MEDIUM, HIGH counts
    private Map<String, Long> departmentBreakdown; // counts per department name

    public AnalyticsDto() {
    }

    public AnalyticsDto(long totalPatients, long totalDoctors, long totalAssessments, long totalAppointments, Map<String, Long> urgencyBreakdown, Map<String, Long> departmentBreakdown) {
        this.totalPatients = totalPatients;
        this.totalDoctors = totalDoctors;
        this.totalAssessments = totalAssessments;
        this.totalAppointments = totalAppointments;
        this.urgencyBreakdown = urgencyBreakdown;
        this.departmentBreakdown = departmentBreakdown;
    }

    public long getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(long totalPatients) {
        this.totalPatients = totalPatients;
    }

    public long getTotalDoctors() {
        return totalDoctors;
    }

    public void setTotalDoctors(long totalDoctors) {
        this.totalDoctors = totalDoctors;
    }

    public long getTotalAssessments() {
        return totalAssessments;
    }

    public void setTotalAssessments(long totalAssessments) {
        this.totalAssessments = totalAssessments;
    }

    public long getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(long totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public Map<String, Long> getUrgencyBreakdown() {
        return urgencyBreakdown;
    }

    public void setUrgencyBreakdown(Map<String, Long> urgencyBreakdown) {
        this.urgencyBreakdown = urgencyBreakdown;
    }

    public Map<String, Long> getDepartmentBreakdown() {
        return departmentBreakdown;
    }

    public void setDepartmentBreakdown(Map<String, Long> departmentBreakdown) {
        this.departmentBreakdown = departmentBreakdown;
    }
}
