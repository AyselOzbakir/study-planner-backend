package com.example.studyplanner.service.impl;

import com.example.studyplanner.dto.request.StudySessionRequest;
import com.example.studyplanner.dto.response.StudySessionResponse;
import com.example.studyplanner.entity.Course;
import com.example.studyplanner.entity.StudySession;
import com.example.studyplanner.entity.User;
import com.example.studyplanner.exception.ResourceNotFoundException;
import com.example.studyplanner.mapper.StudySessionMapper;
import com.example.studyplanner.repository.CourseRepository;
import com.example.studyplanner.repository.StudySessionRepository;
import com.example.studyplanner.repository.UserRepository;
import com.example.studyplanner.service.StudySessionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StudySessionServiceImpl implements StudySessionService {
    private final StudySessionRepository studySessionRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public StudySessionServiceImpl(StudySessionRepository studySessionRepository,
                                  UserRepository userRepository,
                                  CourseRepository courseRepository) {
        this.studySessionRepository = studySessionRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public StudySessionResponse createStudySession(StudySessionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        StudySession studySession = StudySession.builder()
                .topic(request.getTopic())
                .sessionDate(request.getSessionDate())
                .durationMinutes(request.getDurationMinutes())
                .notes(request.getNotes())
                .status(request.getStatus())
                .user(user)
                .course(course)
                .build();

        StudySession savedStudySession = studySessionRepository.save(studySession);
        return StudySessionMapper.toResponse(savedStudySession);
    }

    @Override
    public StudySessionResponse getStudySessionById(Long id) {
        StudySession studySession = studySessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Study session not found with id: " + id));
        return StudySessionMapper.toResponse(studySession);
    }

    @Override
    public Page<StudySessionResponse> getAllStudySessionsByUser(Long userId, Pageable pageable) {
        return studySessionRepository.findByUserId(userId, pageable)
                .map(StudySessionMapper::toResponse);
    }

    @Override
    public StudySessionResponse updateStudySession(Long id, StudySessionRequest request) {
        StudySession studySession = studySessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Study session not found with id: " + id));

        if (!studySession.getUser().getId().equals(request.getUserId())) {
            User newUser = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
            studySession.setUser(newUser);
        }

        if (!studySession.getCourse().getId().equals(request.getCourseId())) {
            Course newCourse = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));
            studySession.setCourse(newCourse);
        }

        StudySessionMapper.updateEntityFromRequest(request, studySession);
        StudySession updatedStudySession = studySessionRepository.save(studySession);
        return StudySessionMapper.toResponse(updatedStudySession);
    }

    @Override
    public void deleteStudySession(Long id) {
        StudySession studySession = studySessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Study session not found with id: " + id));
        studySessionRepository.delete(studySession);
    }
}
