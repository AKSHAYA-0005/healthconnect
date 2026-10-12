package com.healthconnect.service;

import com.healthconnect.dto.DepartmentDto;
import com.healthconnect.entity.Department;
import com.healthconnect.exception.ResourceNotFoundException;
import com.healthconnect.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(DepartmentDto::new)
                .collect(Collectors.toList());
    }

    public DepartmentDto getDepartmentById(Long id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        return new DepartmentDto(dept);
    }

    public DepartmentDto createDepartment(DepartmentDto dto) {
        Department dept = new Department();
        dept.setName(dto.getName());
        dept.setDescription(dto.getDescription());
        dept.setIcon(dto.getIcon());
        dept.setEmergencyPhone(dto.getEmergencyPhone());
        Department saved = departmentRepository.save(dept);
        return new DepartmentDto(saved);
    }

    public DepartmentDto updateDepartment(Long id, DepartmentDto dto) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        dept.setName(dto.getName());
        dept.setDescription(dto.getDescription());
        dept.setIcon(dto.getIcon());
        dept.setEmergencyPhone(dto.getEmergencyPhone());
        Department updated = departmentRepository.save(dept);
        return new DepartmentDto(updated);
    }

    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department", "id", id);
        }
        departmentRepository.deleteById(id);
    }
}
