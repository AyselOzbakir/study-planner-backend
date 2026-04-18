package com.example.studyplanner.mapper;

import com.example.studyplanner.dto.request.StudySessionRequest;
import com.example.studyplanner.dto.response.StudySessionResponse;
import com.example.studyplanner.entity.StudySession;

public class StudySessionMapper {

    public static StudySessionResponse toResponse(StudySession studySession) {
        return StudySessionResponse.builder()
                .id(studySession.getId())
                .topic(studySession.getTopic())
                .sessionDate(studySession.getSessionDate())
                .durationMinutes(studySession.getDurationMinutes())
                .notes(studySession.getNotes())
                .status(studySession.getStatus())
                .userId(studySession.getUser().getId())
                .courseId(studySession.getCourse().getId())
                .createdAt(studySession.getCreatedAt())
                .updatedAt(studySession.getUpdatedAt())
                .build();
    }

    public static void updateEntityFromRequest(StudySessionRequest request, StudySession studySession) {
        studySession.setTopic(request.getTopic());
        studySession.setSessionDate(request.getSessionDate());
        studySession.setDurationMinutes(request.getDurationMinutes());
        studySession.setNotes(request.getNotes());
        studySession.setStatus(request.getStatus());
    }
}
