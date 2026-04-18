package com.example.studyplanner.service;

import com.example.studyplanner.dto.request.AssignmentRequest;
import com.example.studyplanner.dto.response.AssignmentResponse;
import com.example.studyplanner.enums.AssignmentPriority;
import com.example.studyplanner.enums.AssignmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface AssignmentService {
    AssignmentResponse createAssignment(AssignmentRequest request);
    AssignmentResponse getAssignmentById(Long id);
    Page<AssignmentResponse> getAssignmentsByCourse(Long courseId, Pageable pageable);
    Page<AssignmentResponse> getAssignmentsByCourseAndStatus(Long courseId, AssignmentStatus status, Pageable pageable);
    Page<AssignmentResponse> getAssignmentsByCourseAndPriority(Long courseId, AssignmentPriority priority, Pageable pageable);
    Page<AssignmentResponse> getAssignmentsByCourseAndDueDateBetween(Long courseId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    AssignmentResponse updateAssignment(Long id, AssignmentRequest request);
    void deleteAssignment(Long id);
}
