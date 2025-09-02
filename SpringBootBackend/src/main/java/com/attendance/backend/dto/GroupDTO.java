package com.attendance.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class GroupDTO {

    private Long id;

    @NotBlank(message = "Nazwa grupy jest wymagana")
    @Size(max = 100, message = "Nazwa grupy nie może być dłuższa niż 100 znaków")
    private String name;

    @NotBlank(message = "Specjalizacja jest wymagana")
    @Size(max = 100, message = "Specjalizacja nie może być dłuższa niż 100 znaków")
    private String specialization;

    private LocalDateTime createdDate;

    private Boolean active = true;

    private Integer studentCount;

    // Constructors
    public GroupDTO() {}

    public GroupDTO(String name, String specialization) {
        this.name = name;
        this.specialization = specialization;
    }

    public GroupDTO(Long id, String name, String specialization, LocalDateTime createdDate, Boolean active) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.createdDate = createdDate;
        this.active = active;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Integer getStudentCount() { return studentCount; }
    public void setStudentCount(Integer studentCount) { this.studentCount = studentCount; }
}