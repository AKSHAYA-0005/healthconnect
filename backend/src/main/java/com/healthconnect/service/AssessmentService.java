package com.healthconnect.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthconnect.dto.AssessmentDto;
import com.healthconnect.entity.Role;
import com.healthconnect.entity.SymptomAssessment;
import com.healthconnect.entity.User;
import com.healthconnect.exception.BadRequestException;
import com.healthconnect.exception.ResourceNotFoundException;
import com.healthconnect.repository.AppointmentRepository;
import com.healthconnect.repository.DoctorRepository;
import com.healthconnect.repository.SymptomAssessmentRepository;
import com.healthconnect.repository.UserRepository;
import com.healthconnect.triage.TriageEngine;
import com.healthconnect.triage.dto.TriageInput;
import com.healthconnect.triage.dto.TriageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    @Autowired
    private SymptomAssessmentRepository assessmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TriageEngine triageEngine;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AssessmentDto createAssessment(String patientEmail, TriageInput input) {
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", patientEmail));

        TriageResult result = triageEngine.evaluate(input);

        SymptomAssessment assessment = new SymptomAssessment();
        assessment.setPatient(patient);
        assessment.setSymptomsText(input.getSymptomsText());
        assessment.setAge(input.getAge());
        assessment.setGender(input.getGender());
        assessment.setSeverityScale(input.getSeverityScale());
        assessment.setDurationDays(input.getDurationDays());
        assessment.setPreExistingConditions(input.getPreExistingConditions());
        assessment.setUrgencyLevel(result.getUrgencyLevel());
        assessment.setRecommendedDepartment(result.getRecommendedDepartment());
        assessment.setEmergencyAlert(result.isEmergency());

        try {
            assessment.setReasonsJson(objectMapper.writeValueAsString(result.getReasons()));
        } catch (Exception e) {
            assessment.setReasonsJson("[]");
        }

        SymptomAssessment saved = assessmentRepository.save(assessment);
        return new AssessmentDto(saved);
    }

    public List<AssessmentDto> getPatientAssessments(String patientEmail) {
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", patientEmail));
        return assessmentRepository.findByPatientIdOrderByCreatedAtDesc(patient.getId()).stream()
                .map(AssessmentDto::new)
                .collect(Collectors.toList());
    }

    public AssessmentDto getAssessmentById(Long id, String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", currentUserEmail));

        SymptomAssessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment", "id", id));

        // Authorization check: Patient owns assessment, or Admin, or Doctor who has an appointment with this patient
        if (user.getRole() == Role.ROLE_PATIENT && !assessment.getPatient().getId().equals(user.getId())) {
            throw new BadRequestException("Unauthorized access to assessment record");
        }

        if (user.getRole() == Role.ROLE_DOCTOR) {
            var doctorOpt = doctorRepository.findByUserId(user.getId());
            if (doctorOpt.isPresent()) {
                Long doctorId = doctorOpt.get().getId();
                boolean hasAppointment = appointmentRepository.findByDoctorIdOrderByAppointmentDateDesc(doctorId).stream()
                        .anyMatch(app -> app.getPatient().getId().equals(assessment.getPatient().getId()));
                if (!hasAppointment) {
                    throw new BadRequestException("Doctor is not authorized to view this patient's assessment history without an active appointment link.");
                }
            }
        }

        return new AssessmentDto(assessment);
    }

    public List<AssessmentDto> getAllAssessmentsForAdmin() {
        return assessmentRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(AssessmentDto::new)
                .collect(Collectors.toList());
    }
}
