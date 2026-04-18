package com.example.studyplanner.controller;

import com.example.studyplanner.dto.request.AssignmentRequest;
import com.example.studyplanner.dto.response.AssignmentResponse;
import com.example.studyplanner.enums.AssignmentPriority;
import com.example.studyplanner.enums.AssignmentStatus;
import com.example.studyplanner.service.AssignmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/assignments")
@SecurityRequirement(name = "bearerAuth")
public class AssignmentController {
    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<AssignmentResponse> createAssignment(
            @Valid @RequestBody AssignmentRequest request) {
        AssignmentResponse response = assignmentService.createAssignment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentResponse> getAssignmentById(@PathVariable Long id) {
        AssignmentResponse response = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Page<AssignmentResponse>> getAssignmentsByCourse(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Page<AssignmentResponse> response = assignmentService.getAssignmentsByCourse(
                courseId,
                PageRequest.of(page, size, sort)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}/status")
    public ResponseEntity<Page<AssignmentResponse>> getAssignmentsByCourseAndStatus(
            @PathVariable Long courseId,
            @RequestParam AssignmentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Page<AssignmentResponse> response = assignmentService.getAssignmentsByCourseAndStatus(
                courseId,
                status,
                PageRequest.of(page, size, sort)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}/priority")
    public ResponseEntity<Page<AssignmentResponse>> getAssignmentsByCourseAndPriority(
            @PathVariable Long courseId,
            @RequestParam AssignmentPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Page<AssignmentResponse> response = assignmentService.getAssignmentsByCourseAndPriority(
                courseId,
                priority,
                PageRequest.of(page, size, sort)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}/date-range")
    public ResponseEntity<Page<AssignmentResponse>> getAssignmentsByCourseAndDueDateBetween(
            @PathVariable Long courseId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Page<AssignmentResponse> response = assignmentService.getAssignmentsByCourseAndDueDateBetween(
                courseId,
                startDate,
                endDate,
                PageRequest.of(page, size, sort)
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignmentResponse> updateAssignment(
            @PathVariable Long id,
            @Valid @RequestBody AssignmentRequest request) {
        AssignmentResponse response = assignmentService.updateAssignment(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
