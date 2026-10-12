package com.healthconnect.service;

import com.healthconnect.dto.AnalyticsDto;
import com.healthconnect.entity.Role;
import com.healthconnect.entity.SymptomAssessment;
import com.healthconnect.repository.AppointmentRepository;
import com.healthconnect.repository.DoctorRepository;
import com.healthconnect.repository.SymptomAssessmentRepository;
import com.healthconnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SymptomAssessmentRepository assessmentRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public AnalyticsDto getAnalytics() {
        long totalPatients = userRepository.findByRole(Role.ROLE_PATIENT).size();
        long totalDoctors = doctorRepository.count();
        long totalAssessments = assessmentRepository.count();
        long totalAppointments = appointmentRepository.count();

        Map<String, Long> urgencyBreakdown = new HashMap<>();
        urgencyBreakdown.put("LOW", assessmentRepository.countByUrgencyLevel("LOW"));
        urgencyBreakdown.put("MEDIUM", assessmentRepository.countByUrgencyLevel("MEDIUM"));
        urgencyBreakdown.put("HIGH", assessmentRepository.countByUrgencyLevel("HIGH"));

        Map<String, Long> departmentBreakdown = new HashMap<>();
        List<SymptomAssessment> assessments = assessmentRepository.findAll();
        for (SymptomAssessment assessment : assessments) {
            String dept = assessment.getRecommendedDepartment();
            if (dept != null) {
                departmentBreakdown.put(dept, departmentBreakdown.getOrDefault(dept, 0L) + 1);
            }
        }

        return new AnalyticsDto(
                totalPatients,
                totalDoctors,
                totalAssessments,
                totalAppointments,
                urgencyBreakdown,
                departmentBreakdown
        );
    }
}
