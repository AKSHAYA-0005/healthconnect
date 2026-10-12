package com.healthconnect.controller;

import com.healthconnect.dto.*;
import com.healthconnect.entity.Role;
import com.healthconnect.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<UserDto> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        Boolean enabled = body.get("enabled");
        return ResponseEntity.ok(userService.updateUserStatus(id, enabled != null ? enabled : true));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<UserDto> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String roleStr = body.get("role");
        Role role = Role.valueOf(roleStr);
        return ResponseEntity.ok(userService.updateUserRole(id, role));
    }

    @PostMapping("/departments")
    public ResponseEntity<DepartmentDto> createDepartment(@RequestBody DepartmentDto dto) {
        return ResponseEntity.ok(departmentService.createDepartment(dto));
    }

    @PutMapping("/departments/{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDto dto) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, dto));
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/doctors")
    public ResponseEntity<DoctorDto> createDoctor(@RequestBody Map<String, Object> body) {
        String fullName = (String) body.get("fullName");
        String email = (String) body.get("email");
        String password = (String) body.get("password");
        String phone = (String) body.get("phone");
        Long departmentId = Long.valueOf(body.get("departmentId").toString());
        String qualification = (String) body.get("qualification");
        Integer experienceYears = Integer.valueOf(body.get("experienceYears").toString());
        Double consultationFee = Double.valueOf(body.get("consultationFee").toString());
        String availableDays = (String) body.get("availableDays");

        return ResponseEntity.ok(doctorService.createDoctor(
                fullName, email, password, phone, departmentId,
                qualification, experienceYears, consultationFee, availableDays
        ));
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<DoctorDto> updateDoctor(@PathVariable Long id, @RequestBody DoctorDto dto) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, dto));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsDto> getAnalytics() {
        return ResponseEntity.ok(adminService.getAnalytics());
    }
}
