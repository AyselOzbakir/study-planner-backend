package com.example.studyplanner.service;

import com.example.studyplanner.dto.request.CourseRequest;
import com.example.studyplanner.dto.response.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseService {
    CourseResponse createCourse(Long userId, CourseRequest request);
    CourseResponse getCourseById(Long id);
    Page<CourseResponse> getAllCoursesByUser(Long userId, Pageable pageable);
    CourseResponse updateCourse(Long id, CourseRequest request);
    void deleteCourse(Long id);
}
