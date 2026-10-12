package com.healthconnect.service;

import com.healthconnect.dto.DoctorDto;
import com.healthconnect.entity.Department;
import com.healthconnect.entity.Doctor;
import com.healthconnect.entity.Role;
import com.healthconnect.entity.User;
import com.healthconnect.exception.BadRequestException;
import com.healthconnect.exception.ResourceNotFoundException;
import com.healthconnect.repository.DepartmentRepository;
import com.healthconnect.repository.DoctorRepository;
import com.healthconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(DoctorDto::new)
                .collect(Collectors.toList());
    }

    public List<DoctorDto> getDoctorsByDepartment(Long departmentId) {
        return doctorRepository.findByDepartmentId(departmentId).stream()
                .map(DoctorDto::new)
                .collect(Collectors.toList());
    }

    public DoctorDto getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        return new DoctorDto(doctor);
    }

    public DoctorDto getDoctorByUserId(Long userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile for user id " + userId + " not found"));
        return new DoctorDto(doctor);
    }

    public DoctorDto createDoctor(String fullName, String email, String password, String phone,
                                  Long departmentId, String qualification, Integer experienceYears,
                                  Double consultationFee, String availableDays) {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already registered");
        }

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", departmentId));

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setRole(Role.ROLE_DOCTOR);
        user.setEnabled(true);
        User savedUser = userRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setUser(savedUser);
        doctor.setDepartment(department);
        doctor.setQualification(qualification);
        doctor.setExperienceYears(experienceYears);
        doctor.setConsultationFee(consultationFee);
        doctor.setAvailableDays(availableDays);

        Doctor savedDoctor = doctorRepository.save(doctor);
        return new DoctorDto(savedDoctor);
    }

    public DoctorDto updateDoctor(Long id, DoctorDto dto) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        if (dto.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "id", dto.getDepartmentId()));
            doctor.setDepartment(dept);
        }

        if (dto.getQualification() != null) doctor.setQualification(dto.getQualification());
        if (dto.getExperienceYears() != null) doctor.setExperienceYears(dto.getExperienceYears());
        if (dto.getConsultationFee() != null) doctor.setConsultationFee(dto.getConsultationFee());
        if (dto.getAvailableDays() != null) doctor.setAvailableDays(dto.getAvailableDays());

        Doctor updated = doctorRepository.save(doctor);
        return new DoctorDto(updated);
    }
}
