package com.healthconnect.controller;

import com.healthconnect.dto.AssessmentDto;
import com.healthconnect.service.AssessmentService;
import com.healthconnect.triage.dto.TriageInput;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @PostMapping
    public ResponseEntity<AssessmentDto> createAssessment(Authentication authentication, @Valid @RequestBody TriageInput input) {
        String email = authentication.getName();
        return ResponseEntity.ok(assessmentService.createAssessment(email, input));
    }

    @GetMapping
    public ResponseEntity<List<AssessmentDto>> getMyAssessments(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(assessmentService.getPatientAssessments(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssessmentDto> getAssessmentById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(assessmentService.getAssessmentById(id, email));
    }
}
