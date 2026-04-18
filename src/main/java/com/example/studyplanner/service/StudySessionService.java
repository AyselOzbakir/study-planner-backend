package com.example.studyplanner.service;

import com.example.studyplanner.dto.request.StudySessionRequest;
import com.example.studyplanner.dto.response.StudySessionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudySessionService {
    StudySessionResponse createStudySession(StudySessionRequest request);
    StudySessionResponse getStudySessionById(Long id);
    Page<StudySessionResponse> getAllStudySessionsByUser(Long userId, Pageable pageable);
    StudySessionResponse updateStudySession(Long id, StudySessionRequest request);
    void deleteStudySession(Long id);
}
