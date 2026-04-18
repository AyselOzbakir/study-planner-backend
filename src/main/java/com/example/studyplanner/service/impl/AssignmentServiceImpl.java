package com.example.studyplanner.service.impl;

import com.example.studyplanner.dto.request.AssignmentRequest;
import com.example.studyplanner.dto.response.AssignmentResponse;
import com.example.studyplanner.entity.Assignment;
import com.example.studyplanner.entity.Course;
import com.example.studyplanner.enums.AssignmentPriority;
import com.example.studyplanner.enums.AssignmentStatus;
import com.example.studyplanner.exception.ResourceNotFoundException;
import com.example.studyplanner.mapper.AssignmentMapper;
import com.example.studyplanner.repository.AssignmentRepository;
import com.example.studyplanner.repository.CourseRepository;
import com.example.studyplanner.service.AssignmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository, CourseRepository courseRepository) {
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public AssignmentResponse createAssignment(AssignmentRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        Assignment assignment = Assignment.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .status(request.getStatus())
                .priority(request.getPriority())
                .course(course)
                .build();

        Assignment savedAssignment = assignmentRepository.save(assignment);
        return AssignmentMapper.toResponse(savedAssignment);
    }

    @Override
    public AssignmentResponse getAssignmentById(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
        return AssignmentMapper.toResponse(assignment);
    }

    @Override
    public Page<AssignmentResponse> getAssignmentsByCourse(Long courseId, Pageable pageable) {
        return assignmentRepository.findByCourseId(courseId, pageable)
                .map(AssignmentMapper::toResponse);
    }

    @Override
    public Page<AssignmentResponse> getAssignmentsByCourseAndStatus(Long courseId, AssignmentStatus status, Pageable pageable) {
        return assignmentRepository.findByCourseIdAndStatus(courseId, status, pageable)
                .map(AssignmentMapper::toResponse);
    }

    @Override
    public Page<AssignmentResponse> getAssignmentsByCourseAndPriority(Long courseId, AssignmentPriority priority, Pageable pageable) {
        return assignmentRepository.findByCourseIdAndPriority(courseId, priority, pageable)
                .map(AssignmentMapper::toResponse);
    }

    @Override
    public Page<AssignmentResponse> getAssignmentsByCourseAndDueDateBetween(Long courseId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return assignmentRepository.findByCourseIdAndDueDateBetween(courseId, startDate, endDate, pageable)
                .map(AssignmentMapper::toResponse);
    }

    @Override
    public AssignmentResponse updateAssignment(Long id, AssignmentRequest request) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));

        if (!assignment.getCourse().getId().equals(request.getCourseId())) {
            Course newCourse = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));
            assignment.setCourse(newCourse);
        }

        AssignmentMapper.updateEntityFromRequest(request, assignment);
        Assignment updatedAssignment = assignmentRepository.save(assignment);
        return AssignmentMapper.toResponse(updatedAssignment);
    }

    @Override
    public void deleteAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
        assignmentRepository.delete(assignment);
    }
}
