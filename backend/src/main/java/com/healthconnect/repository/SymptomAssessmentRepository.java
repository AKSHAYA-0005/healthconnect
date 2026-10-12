package com.healthconnect.repository;

import com.healthconnect.entity.SymptomAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SymptomAssessmentRepository extends JpaRepository<SymptomAssessment, Long> {
    List<SymptomAssessment> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    List<SymptomAssessment> findAllByOrderByCreatedAtDesc();
    long countByUrgencyLevel(String urgencyLevel);
}
