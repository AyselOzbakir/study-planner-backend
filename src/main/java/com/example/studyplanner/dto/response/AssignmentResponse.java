package com.example.studyplanner.dto.response;

import com.example.studyplanner.enums.AssignmentPriority;
import com.example.studyplanner.enums.AssignmentStatus;
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
public class AssignmentResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private AssignmentStatus status;
    private AssignmentPriority priority;
    private Long courseId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
