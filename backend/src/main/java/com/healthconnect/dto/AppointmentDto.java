package com.healthconnect.dto;

import com.healthconnect.entity.Appointment;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AppointmentDto {

    private Long id;

    private UserDto patient;
    private Long patientId;

    @NotNull(message = "Doctor selection is required")
    private Long doctorId;
    private DoctorDto doctor;

    private Long departmentId;
    private DepartmentDto department;

    private Long assessmentId;
    private AssessmentDto assessment;

    @NotNull(message = "Appointment date and time is required")
    private LocalDateTime appointmentDate;

    private String reason;
    private Appointment.Status status;
    private String notes;
    private LocalDateTime createdAt;

    public AppointmentDto() {
    }

    public AppointmentDto(Appointment appointment) {
        this.id = appointment.getId();
        if (appointment.getPatient() != null) {
            this.patient = new UserDto(appointment.getPatient());
            this.patientId = appointment.getPatient().getId();
        }
        if (appointment.getDoctor() != null) {
            this.doctor = new DoctorDto(appointment.getDoctor());
            this.doctorId = appointment.getDoctor().getId();
        }
        if (appointment.getDepartment() != null) {
            this.department = new DepartmentDto(appointment.getDepartment());
            this.departmentId = appointment.getDepartment().getId();
        }
        if (appointment.getAssessment() != null) {
            this.assessment = new AssessmentDto(appointment.getAssessment());
            this.assessmentId = appointment.getAssessment().getId();
        }
        this.appointmentDate = appointment.getAppointmentDate();
        this.reason = appointment.getReason();
        this.status = appointment.getStatus();
        this.notes = appointment.getNotes();
        this.createdAt = appointment.getCreatedAt();
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

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public DoctorDto getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorDto doctor) {
        this.doctor = doctor;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public DepartmentDto getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDto department) {
        this.department = department;
    }

    public Long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Long assessmentId) {
        this.assessmentId = assessmentId;
    }

    public AssessmentDto getAssessment() {
        return assessment;
    }

    public void setAssessment(AssessmentDto assessment) {
        this.assessment = assessment;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Appointment.Status getStatus() {
        return status;
    }

    public void setStatus(Appointment.Status status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
