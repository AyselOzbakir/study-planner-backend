package com.example.studyplanner.dto.response;

import com.example.studyplanner.enums.StudySessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudySessionResponse {
    private Long id;
    private String topic;
    private LocalDateTime sessionDate;
    private Integer durationMinutes;
    private String notes;
    private StudySessionStatus status;
    private Long userId;
    private Long courseId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
