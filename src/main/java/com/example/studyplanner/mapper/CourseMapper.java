package com.example.studyplanner.mapper;

import com.example.studyplanner.dto.request.CourseRequest;
import com.example.studyplanner.dto.response.CourseResponse;
import com.example.studyplanner.entity.Course;

public class CourseMapper {

    public static CourseResponse toResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .code(course.getCode())
                .description(course.getDescription())
                .semester(course.getSemester())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }

    public static void updateEntityFromRequest(CourseRequest request, Course course) {
        course.setName(request.getName());
        course.setCode(request.getCode());
        course.setDescription(request.getDescription());
        course.setSemester(request.getSemester());
    }
}
