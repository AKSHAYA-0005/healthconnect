package com.healthconnect.controller;

import com.healthconnect.dto.DoctorDto;
import com.healthconnect.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors(@RequestParam(required = false) Long departmentId) {
        if (departmentId != null) {
            return ResponseEntity.ok(doctorService.getDoctorsByDepartment(departmentId));
        }
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }
}
