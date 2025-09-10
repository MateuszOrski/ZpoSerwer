package com.attendance.backend.dto;

import com.attendance.backend.entity.Attendance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AttendanceDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Index number is required")
    private String indexNumber;

    private String groupName;

    @NotNull(message = "Schedule ID is required")
    private Long scheduleId;

    @NotNull(message = "Status is required")
    private Attendance.AttendanceStatus status;

    private String notes;

    // Constructors
    public AttendanceDTO() {}

    public AttendanceDTO(String firstName, String lastName, String indexNumber,
                         Long scheduleId, Attendance.AttendanceStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
        this.scheduleId = scheduleId;
        this.status = status;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getIndexNumber() { return indexNumber; }
    public void setIndexNumber(String indexNumber) { this.indexNumber = indexNumber; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }

    public Attendance.AttendanceStatus getStatus() { return status; }
    public void setStatus(Attendance.AttendanceStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}