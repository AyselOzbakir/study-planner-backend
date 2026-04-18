# Study Planner - Backend API

A comprehensive REST API for managing university courses, assignments, and study sessions. Built with Spring Boot 3 and secured with JWT authentication.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [API Endpoints](#api-endpoints)
- [Authentication](#authentication)
- [Database Schema](#database-schema)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Error Handling](#error-handling)
- [Security](#security)
- [Troubleshooting](#troubleshooting)
- [Future Enhancements](#future-enhancements)

## Overview

Study Planner is a backend service designed to help university students manage their academic workload efficiently. Users can create and organize courses, track assignments with priority levels and due dates, and schedule study sessions. The API provides CRUD operations, role-based access control, validation, and filtering capabilities.

This project was developed as a university backend development final project using Spring Boot 3, Spring Security, Spring Data JPA, PostgreSQL, and Swagger/OpenAPI.

## Features

### Authentication & Authorization

- User registration with validation
- Secure login with JWT token generation
- Token-based authentication using Bearer tokens
- Role-based access control (USER and ADMIN roles)
- Password encryption with BCrypt

### Course Management

- Create, read, update, delete courses
- Courses linked to user accounts
- Course metadata such as code, semester, and description
- Paginated course listing with sorting

### Assignment Management

- Full CRUD operations for assignments
- Assignment status tracking:
  - `PENDING`
  - `IN_PROGRESS`
  - `COMPLETED`
  - `OVERDUE`
- Priority levels:
  - `LOW`
  - `MEDIUM`
  - `HIGH`
- Due date tracking
- Filter assignments by status, priority, or date range
- Paginated results with sorting

### Study Sessions

- Schedule and manage study sessions
- Track session duration and status
- Associate study sessions with courses
- Paginated study session listing
- Session notes support

### Data & Validation

- DTO-based request/response structure
- Validation annotations for input checking
- Automatic audit fields:
  - `createdAt`
  - `updatedAt`
- Global exception handling with consistent error responses

### API Documentation

- Swagger UI for interactive API exploration
- OpenAPI 3 documentation
- Endpoints can be tested via Swagger UI
- JWT-protected endpoints can also be tested with curl or Postman

## Technology Stack

| Component         | Technology                  | Version |
| ----------------- | --------------------------- | ------- |
| Runtime           | Java                        | 17+     |
| Framework         | Spring Boot                 | 3.5.13  |
| Security          | Spring Security             | 6.x     |
| Database          | PostgreSQL                  | 18.3    |
| ORM               | Spring Data JPA / Hibernate | 6.6.45  |
| Authentication    | JJWT                        | 0.12.6  |
| API Documentation | Springdoc OpenAPI           | 2.8.17  |
| Build Tool        | Maven                       | 3.9+    |
| Testing           | JUnit 5, Mockito            | Latest  |
| Validation        | Jakarta Bean Validation     | 3.x     |
| Utilities         | Lombok                      | 1.18.x  |

## Prerequisites

Before running this application, make sure the following are installed:

- Java 17 or newer
- Maven 3.9+
- PostgreSQL
- Git

Example check commands:

```bash
java -version
mvn -version
psql --version
```
