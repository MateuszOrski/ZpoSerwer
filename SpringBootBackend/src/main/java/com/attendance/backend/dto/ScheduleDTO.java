package com.attendance.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class ScheduleDTO {

    private Long id;

    @NotBlank(message = "Nazwa terminu jest wymagana")
    @Size(max = 255, message = "Nazwa terminu nie może być dłuższa niż 255 znaków")
    private String subject;

    @Size(max = 100, message = "Sala nie może być dłuższa niż 100 znaków")
    private String classroom;

    @NotNull(message = "Data rozpoczęcia jest wymagana")
    private LocalDateTime startTime;

    @NotNull(message = "Data zakończenia jest wymagana")
    private LocalDateTime endTime;

    @Size(max = 100, message = "Prowadzący nie może być dłuższy niż 100 znaków")
    private String instructor;

    private String notes;

    private LocalDateTime createdDate;

    private Long groupId;

    private String groupName;

    private Integer attendanceCount;

    private Integer presentCount;

    private Integer absentCount;

    // Constructors
    public ScheduleDTO() {}

    public ScheduleDTO(String subject, LocalDateTime startTime, LocalDateTime endTime, String groupName) {
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.groupName = groupName;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public Integer getAttendanceCount() { return attendanceCount; }
    public void setAttendanceCount(Integer attendanceCount) { this.attendanceCount = attendanceCount; }

    public Integer getPresentCount() { return presentCount; }
    public void setPresentCount(Integer presentCount) { this.presentCount = presentCount; }

    public Integer getAbsentCount() { return absentCount; }
    public void setAbsentCount(Integer absentCount) { this.absentCount = absentCount; }
}