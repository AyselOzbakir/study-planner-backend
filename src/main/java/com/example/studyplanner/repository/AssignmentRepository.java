package com.example.studyplanner.repository;

import com.example.studyplanner.entity.Assignment;
import com.example.studyplanner.enums.AssignmentPriority;
import com.example.studyplanner.enums.AssignmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Page<Assignment> findByCourseId(Long courseId, Pageable pageable);
    Page<Assignment> findByCourseIdAndStatus(Long courseId, AssignmentStatus status, Pageable pageable);
    Page<Assignment> findByCourseIdAndPriority(Long courseId, AssignmentPriority priority, Pageable pageable);
    Page<Assignment> findByCourseIdAndDueDateBetween(Long courseId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
