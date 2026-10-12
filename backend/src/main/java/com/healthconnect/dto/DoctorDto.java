package com.healthconnect.dto;

import com.healthconnect.entity.Doctor;

public class DoctorDto {

    private Long id;
    private UserDto user;
    private DepartmentDto department;
    private Long departmentId;
    private String qualification;
    private Integer experienceYears;
    private Double consultationFee;
    private String availableDays;

    public DoctorDto() {
    }

    public DoctorDto(Doctor doctor) {
        this.id = doctor.getId();
        if (doctor.getUser() != null) {
            this.user = new UserDto(doctor.getUser());
        }
        if (doctor.getDepartment() != null) {
            this.department = new DepartmentDto(doctor.getDepartment());
            this.departmentId = doctor.getDepartment().getId();
        }
        this.qualification = doctor.getQualification();
        this.experienceYears = doctor.getExperienceYears();
        this.consultationFee = doctor.getConsultationFee();
        this.availableDays = doctor.getAvailableDays();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public DepartmentDto getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDto department) {
        this.department = department;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public Double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(Double consultationFee) {
        this.consultationFee = consultationFee;
    }

    public String getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(String availableDays) {
        this.availableDays = availableDays;
    }
}
