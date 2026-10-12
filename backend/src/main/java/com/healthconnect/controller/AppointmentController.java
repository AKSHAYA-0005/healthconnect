package com.healthconnect.controller;

import com.healthconnect.dto.AppointmentDto;
import com.healthconnect.entity.Appointment;
import com.healthconnect.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentDto> bookAppointment(Authentication authentication, @Valid @RequestBody AppointmentDto dto) {
        String email = authentication.getName();
        return ResponseEntity.ok(appointmentService.bookAppointment(email, dto));
    }

    @GetMapping("/my")
    public ResponseEntity<List<AppointmentDto>> getMyAppointments(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(appointmentService.getPatientAppointments(email));
    }

    @GetMapping("/doctor")
    public ResponseEntity<List<AppointmentDto>> getDoctorAppointments(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(email));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentDto> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication authentication) {
        String statusStr = body.get("status");
        String notes = body.get("notes");
        Appointment.Status status = Appointment.Status.valueOf(statusStr.toUpperCase());
        return ResponseEntity.ok(appointmentService.updateStatus(id, status, notes, authentication.getName()));
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<AppointmentDto> reschedule(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication authentication) {
        String dateStr = body.get("appointmentDate");
        LocalDateTime newDate = LocalDateTime.parse(dateStr);
        return ResponseEntity.ok(appointmentService.rescheduleAppointment(id, newDate, authentication.getName()));
    }
}
