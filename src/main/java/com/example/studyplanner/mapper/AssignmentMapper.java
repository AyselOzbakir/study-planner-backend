package com.example.studyplanner.mapper;

import com.example.studyplanner.dto.request.AssignmentRequest;
import com.example.studyplanner.dto.response.AssignmentResponse;
import com.example.studyplanner.entity.Assignment;

public class AssignmentMapper {

    public static AssignmentResponse toResponse(Assignment assignment) {
        return AssignmentResponse.builder()
                .id(assignment.getId())
                .title(assignment.getTitle())
                .description(assignment.getDescription())
                .dueDate(assignment.getDueDate())
                .status(assignment.getStatus())
                .priority(assignment.getPriority())
                .courseId(assignment.getCourse().getId())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .build();
    }

    public static void updateEntityFromRequest(AssignmentRequest request, Assignment assignment) {
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setDueDate(request.getDueDate());
        assignment.setStatus(request.getStatus());
        assignment.setPriority(request.getPriority());
    }
}
