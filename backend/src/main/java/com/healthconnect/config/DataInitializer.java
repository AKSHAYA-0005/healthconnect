package com.healthconnect.config;

import com.healthconnect.entity.*;
import com.healthconnect.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Default Admin User
        if (!userRepository.existsByEmail("admin@healthconnect.com")) {
            User admin = new User();
            admin.setFullName("System Administrator");
            admin.setEmail("admin@healthconnect.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhone("+1-800-555-0199");
            admin.setRole(Role.ROLE_ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);
            System.out.println("Default Admin account created: admin@healthconnect.com / admin123");
        }

        // 2. Seed Default Departments
        Department cardiology = createDepartmentIfNotFound("Cardiology", "Specialized cardiac and cardiovascular patient care", "❤️", "112");
        Department pulmonology = createDepartmentIfNotFound("Pulmonology", "Respiratory health and lung condition treatments", "🫁", "112");
        Department neurology = createDepartmentIfNotFound("Neurology", "Comprehensive neurological and nervous system care", "🧠", "112");
        Department gastroenterology = createDepartmentIfNotFound("Gastroenterology", "Digestive system and abdominal care", "🩺", "112");
        Department dermatology = createDepartmentIfNotFound("Dermatology", "Skin, hair, and allergic condition diagnosis", "🧴", "112");
        Department orthopedics = createDepartmentIfNotFound("Orthopedics", "Bone, joint, and musculoskeletal therapy", "🦴", "112");
        Department generalMed = createDepartmentIfNotFound("General Medicine", "Primary health evaluation, internal medicine, and triage", "🏥", "112");

        // 3. Seed Sample Doctors
        createDoctorIfNotFound("Dr. Sarah Jenkins", "doctor.jenkins@healthconnect.com", "doc123", "+1-555-0101", cardiology, "MD Cardiology, FACC", 14, 150.0, "Mon, Wed, Fri");
        createDoctorIfNotFound("Dr. Michael Chang", "doctor.chang@healthconnect.com", "doc123", "+1-555-0102", neurology, "MD Neurology, Ph.D.", 10, 140.0, "Tue, Thu, Sat");
        createDoctorIfNotFound("Dr. Emily Rivera", "doctor.rivera@healthconnect.com", "doc123", "+1-555-0103", generalMed, "MD Internal Medicine", 8, 100.0, "Mon-Fri");
    }

    private Department createDepartmentIfNotFound(String name, String description, String icon, String emergencyPhone) {
        Optional<Department> existing = departmentRepository.findByNameIgnoreCase(name);
        if (existing.isPresent()) {
            return existing.get();
        }
        Department dept = new Department();
        dept.setName(name);
        dept.setDescription(description);
        dept.setIcon(icon);
        dept.setEmergencyPhone(emergencyPhone);
        return departmentRepository.save(dept);
    }

    private void createDoctorIfNotFound(String fullName, String email, String password, String phone,
                                        Department department, String qualification, Integer experience,
                                        Double fee, String availableDays) {
        if (!userRepository.existsByEmail(email)) {
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
            doctor.setExperienceYears(experience);
            doctor.setConsultationFee(fee);
            doctor.setAvailableDays(availableDays);
            doctorRepository.save(doctor);
        }
    }
}
