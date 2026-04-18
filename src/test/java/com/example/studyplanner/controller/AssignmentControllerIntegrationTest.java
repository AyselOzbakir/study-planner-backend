package com.example.studyplanner.controller;

import com.example.studyplanner.dto.response.AssignmentResponse;
import com.example.studyplanner.enums.AssignmentPriority;
import com.example.studyplanner.enums.AssignmentStatus;
import com.example.studyplanner.service.AssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import com.example.studyplanner.security.CustomUserDetailsService;
import com.example.studyplanner.security.JwtService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssignmentController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AssignmentController Integration Tests")
class AssignmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssignmentService assignmentService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private AssignmentResponse assignment1;
    private AssignmentResponse assignment2;

    @BeforeEach
    void setUp() {
        assignment1 = AssignmentResponse.builder()
                .id(1L)
                .title("Math Assignment")
                .description("Solve calculus problems")
                .courseId(5L)
                .status(AssignmentStatus.PENDING)
                .priority(AssignmentPriority.HIGH)
                .dueDate(LocalDateTime.now().plusDays(5))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assignment2 = AssignmentResponse.builder()
                .id(2L)
                .title("Physics Assignment")
                .description("Read chapter 5")
                .courseId(5L)
                .status(AssignmentStatus.PENDING)
                .priority(AssignmentPriority.MEDIUM)
                .dueDate(LocalDateTime.now().plusDays(3))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("GET /api/assignments/course/{courseId}/status - Returns 200 with paged content for PENDING status")
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testGetAssignmentsByCourseAndStatus() throws Exception {
        // Arrange
        Long courseId = 5L;
        AssignmentStatus status = AssignmentStatus.PENDING;
        Pageable pageable = PageRequest.of(0, 10);
        List<AssignmentResponse> assignments = Arrays.asList(assignment1, assignment2);
        Page<AssignmentResponse> pagedResponse = new PageImpl<>(assignments, pageable, 2);

        when(assignmentService.getAssignmentsByCourseAndStatus(eq(courseId), eq(status), any(Pageable.class)))
                .thenReturn(pagedResponse);

        // Act & Assert
        mockMvc.perform(get("/api/assignments/course/{courseId}/status", courseId)
                .param("status", "PENDING")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id")
                .param("sortDir", "asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", equalTo(1)))
                .andExpect(jsonPath("$.content[0].title", equalTo("Math Assignment")))
                .andExpect(jsonPath("$.content[0].status", equalTo("PENDING")))
                .andExpect(jsonPath("$.content[1].id", equalTo(2)))
                .andExpect(jsonPath("$.content[1].title", equalTo("Physics Assignment")))
                .andExpect(jsonPath("$.totalElements", equalTo(2)))
                .andExpect(jsonPath("$.totalPages", equalTo(1)))
                .andExpect(jsonPath("$.number", equalTo(0)));
    }
}
