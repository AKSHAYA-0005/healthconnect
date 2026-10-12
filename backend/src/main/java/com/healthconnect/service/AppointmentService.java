package com.healthconnect.service;

import com.healthconnect.dto.AppointmentDto;
import com.healthconnect.entity.*;
import com.healthconnect.exception.BadRequestException;
import com.healthconnect.exception.ResourceNotFoundException;
import com.healthconnect.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SymptomAssessmentRepository assessmentRepository;

    public AppointmentDto bookAppointment(String patientEmail, AppointmentDto dto) {
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", patientEmail));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", dto.getDoctorId()));

        Department department = doctor.getDepartment();

        SymptomAssessment assessment = null;
        if (dto.getAssessmentId() != null) {
            assessment = assessmentRepository.findById(dto.getAssessmentId()).orElse(null);
        }

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setDepartment(department);
        appointment.setAssessment(assessment);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setReason(dto.getReason());
        appointment.setStatus(Appointment.Status.REQUESTED);

        Appointment saved = appointmentRepository.save(appointment);
        return new AppointmentDto(saved);
    }

    public List<AppointmentDto> getPatientAppointments(String patientEmail) {
        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", patientEmail));
        return appointmentRepository.findByPatientIdOrderByAppointmentDateDesc(patient.getId()).stream()
                .map(AppointmentDto::new)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getDoctorAppointments(String doctorEmail) {
        User doctorUser = userRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", doctorEmail));
        Doctor doctor = doctorRepository.findByUserId(doctorUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found for user: " + doctorEmail));

        return appointmentRepository.findByDoctorIdOrderByAppointmentDateDesc(doctor.getId()).stream()
                .map(AppointmentDto::new)
                .collect(Collectors.toList());
    }

    public List<AppointmentDto> getAllAppointments() {
        return appointmentRepository.findAllByOrderByAppointmentDateDesc().stream()
                .map(AppointmentDto::new)
                .collect(Collectors.toList());
    }

    public AppointmentDto updateStatus(Long appointmentId, Appointment.Status status, String notes, String userEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        appointment.setStatus(status);
        if (notes != null) {
            appointment.setNotes(notes);
        }

        Appointment updated = appointmentRepository.save(appointment);
        return new AppointmentDto(updated);
    }

    public AppointmentDto rescheduleAppointment(Long appointmentId, LocalDateTime newDate, String patientEmail) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment", "id", appointmentId));

        User patient = userRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", patientEmail));

        if (!appointment.getPatient().getId().equals(patient.getId()) && patient.getRole() != Role.ROLE_ADMIN) {
            throw new BadRequestException("Unauthorized to reschedule this appointment");
        }

        appointment.setAppointmentDate(newDate);
        appointment.setStatus(Appointment.Status.REQUESTED);
        Appointment updated = appointmentRepository.save(appointment);
        return new AppointmentDto(updated);
    }
}
