package com.healthconnect.dto;

import com.healthconnect.entity.Department;
import jakarta.validation.constraints.NotBlank;

public class DepartmentDto {

    private Long id;

    @NotBlank(message = "Department name is required")
    private String name;

    private String description;
    private String icon;
    private String emergencyPhone;

    public DepartmentDto() {
    }

    public DepartmentDto(Department department) {
        this.id = department.getId();
        this.name = department.getName();
        this.description = department.getDescription();
        this.icon = department.getIcon();
        this.emergencyPhone = department.getEmergencyPhone();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }
}
