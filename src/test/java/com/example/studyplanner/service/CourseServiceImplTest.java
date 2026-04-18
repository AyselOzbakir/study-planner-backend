package com.example.studyplanner.service;

import com.example.studyplanner.dto.request.CourseRequest;
import com.example.studyplanner.dto.response.CourseResponse;
import com.example.studyplanner.entity.Course;
import com.example.studyplanner.entity.User;
import com.example.studyplanner.enums.Role;
import com.example.studyplanner.exception.ResourceNotFoundException;
import com.example.studyplanner.mapper.CourseMapper;
import com.example.studyplanner.repository.CourseRepository;
import com.example.studyplanner.repository.UserRepository;
import com.example.studyplanner.service.impl.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseServiceImpl Unit Tests")
class CourseServiceImplTest {
    
    @Mock
    private CourseRepository courseRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private CourseServiceImpl courseService;
    
    private User testUser;
    private Course testCourse;
    private CourseRequest validCourseRequest;
    
    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .fullName("Jane Doe")
                .email("jane@example.com")
                .password("encoded_password")
                .role(Role.USER)
                .build();
        
        testCourse = Course.builder()
                .id(1L)
                .name("Database Systems")
                .code("CS202")
                .description("Learn database design and SQL")
                .semester("Spring 2026")
                .user(testUser)
                .build();
        testCourse.setCreatedAt(LocalDateTime.now());
        testCourse.setUpdatedAt(LocalDateTime.now());
        
        validCourseRequest = CourseRequest.builder()
                .name("Database Systems")
                .code("CS202")
                .description("Learn database design and SQL")
                .semester("Spring 2026")
                .build();
    }
    
    @Test
    @DisplayName("Successfully create a new course")
    void testCreateCourseSuccess() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);
        
        // Act
        CourseResponse response = courseService.createCourse(1L, validCourseRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(testCourse.getName(), response.getName());
        assertEquals(testCourse.getCode(), response.getCode());
        
        verify(userRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }
    
    @Test
    @DisplayName("Create course fails when user not found")
    void testCreateCourseUserNotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
                () -> courseService.createCourse(999L, validCourseRequest),
                "Should throw ResourceNotFoundException when user not found");
        
        verify(userRepository, times(1)).findById(999L);
        verify(courseRepository, never()).save(any(Course.class));
    }
    
    @Test
    @DisplayName("Successfully retrieve course by ID")
    void testGetCourseByIdSuccess() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        
        // Act
        CourseResponse response = courseService.getCourseById(1L);
        
        // Assert
        assertNotNull(response);
        assertEquals(testCourse.getName(), response.getName());
        assertEquals(testCourse.getCode(), response.getCode());
        
        verify(courseRepository, times(1)).findById(1L);
    }
    
    @Test
    @DisplayName("Get course fails when course not found")
    void testGetCourseByIdNotFound() {
        // Arrange
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
                () -> courseService.getCourseById(999L),
                "Should throw ResourceNotFoundException when course not found");
        
        verify(courseRepository, times(1)).findById(999L);
    }
    
    @Test
    @DisplayName("Successfully retrieve paginated courses by user")
    void testGetAllCoursesByUserSuccess() {
        // Arrange
        List<Course> courses = Arrays.asList(testCourse);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> coursePage = new PageImpl<>(courses, pageable, 1);
        
        when(courseRepository.findByUserId(1L, pageable)).thenReturn(coursePage);
        
        // Act
        Page<CourseResponse> response = courseService.getAllCoursesByUser(1L, pageable);
        
        // Assert
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(testCourse.getName(), response.getContent().get(0).getName());
        
        verify(courseRepository, times(1)).findByUserId(1L, pageable);
    }
    
    @Test
    @DisplayName("Get courses returns empty page when user has no courses")
    void testGetAllCoursesByUserEmptyPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> emptyPage = new PageImpl<>(Arrays.asList(), pageable, 0);
        
        when(courseRepository.findByUserId(1L, pageable)).thenReturn(emptyPage);
        
        // Act
        Page<CourseResponse> response = courseService.getAllCoursesByUser(1L, pageable);
        
        // Assert
        assertNotNull(response);
        assertEquals(0, response.getTotalElements());
        assertTrue(response.getContent().isEmpty());
        
        verify(courseRepository, times(1)).findByUserId(1L, pageable);
    }
    
    @Test
    @DisplayName("Successfully update course")
    void testUpdateCourseSuccess() {
        // Arrange
        CourseRequest updateRequest = CourseRequest.builder()
                .name("Advanced Database Systems")
                .code("CS202")
                .description("Advanced SQL and NoSQL")
                .semester("Spring 2026")
                .build();
        
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act
        CourseResponse response = courseService.updateCourse(1L, updateRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(updateRequest.getName(), response.getName());
        assertEquals(updateRequest.getDescription(), response.getDescription());
        
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }
    
    @Test
    @DisplayName("Update course fails when course not found")
    void testUpdateCourseNotFound() {
        // Arrange
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
                () -> courseService.updateCourse(999L, validCourseRequest),
                "Should throw ResourceNotFoundException when course not found");
        
        verify(courseRepository, times(1)).findById(999L);
        verify(courseRepository, never()).save(any(Course.class));
    }
    
    @Test
    @DisplayName("Successfully delete course")
    void testDeleteCourseSuccess() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        doNothing().when(courseRepository).delete(testCourse);
        
        // Act
        courseService.deleteCourse(1L);
        
        // Assert
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).delete(testCourse);
    }
    
    @Test
    @DisplayName("Delete course fails when course not found")
    void testDeleteCourseNotFound() {
        // Arrange
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
                () -> courseService.deleteCourse(999L),
                "Should throw ResourceNotFoundException when course not found");
        
        verify(courseRepository, times(1)).findById(999L);
        verify(courseRepository, never()).delete(any(Course.class));
    }
}
