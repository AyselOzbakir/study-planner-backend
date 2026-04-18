package com.example.studyplanner.service.impl;

import com.example.studyplanner.dto.request.CourseRequest;
import com.example.studyplanner.dto.response.CourseResponse;
import com.example.studyplanner.entity.Course;
import com.example.studyplanner.entity.User;
import com.example.studyplanner.exception.ResourceNotFoundException;
import com.example.studyplanner.mapper.CourseMapper;
import com.example.studyplanner.repository.CourseRepository;
import com.example.studyplanner.repository.UserRepository;
import com.example.studyplanner.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseServiceImpl(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CourseResponse createCourse(Long userId, CourseRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Course course = Course.builder()
                .name(request.getName())
                .code(request.getCode())
                .description(request.getDescription())
                .semester(request.getSemester())
                .user(user)
                .build();

        Course savedCourse = courseRepository.save(course);
        return CourseMapper.toResponse(savedCourse);
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return CourseMapper.toResponse(course);
    }

    @Override
    public Page<CourseResponse> getAllCoursesByUser(Long userId, Pageable pageable) {
        return courseRepository.findByUserId(userId, pageable)
                .map(CourseMapper::toResponse);
    }

    @Override
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        CourseMapper.updateEntityFromRequest(request, course);
        Course updatedCourse = courseRepository.save(course);
        return CourseMapper.toResponse(updatedCourse);
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        courseRepository.delete(course);
    }
}
