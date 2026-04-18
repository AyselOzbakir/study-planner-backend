package com.example.studyplanner.controller;

import com.example.studyplanner.dto.request.StudySessionRequest;
import com.example.studyplanner.dto.response.StudySessionResponse;
import com.example.studyplanner.service.StudySessionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/study-sessions")
@SecurityRequirement(name = "bearerAuth")
public class StudySessionController {
    private final StudySessionService studySessionService;

    public StudySessionController(StudySessionService studySessionService) {
        this.studySessionService = studySessionService;
    }

    @PostMapping
    public ResponseEntity<StudySessionResponse> createStudySession(
            @Valid @RequestBody StudySessionRequest request) {
        StudySessionResponse response = studySessionService.createStudySession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudySessionResponse> getStudySessionById(@PathVariable Long id) {
        StudySessionResponse response = studySessionService.getStudySessionById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<StudySessionResponse>> getAllStudySessionsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Page<StudySessionResponse> response = studySessionService.getAllStudySessionsByUser(
                userId,
                PageRequest.of(page, size, sort)
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudySessionResponse> updateStudySession(
            @PathVariable Long id,
            @Valid @RequestBody StudySessionRequest request) {
        StudySessionResponse response = studySessionService.updateStudySession(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudySession(@PathVariable Long id) {
        studySessionService.deleteStudySession(id);
        return ResponseEntity.noContent().build();
    }
}
