package com.attendance.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class StudentDTO {

    private Long id;

    @NotBlank(message = "Imię jest wymagane")
    @Size(max = 50, message = "Imię nie może być dłuższe niż 50 znaków")
    private String firstName;

    @NotBlank(message = "Nazwisko jest wymagane")
    @Size(max = 50, message = "Nazwisko nie może być dłuższe niż 50 znaków")
    private String lastName;

    @NotBlank(message = "Numer indeksu jest wymagany")
    @Pattern(regexp = "\\d{6}", message = "Numer indeksu musi składać się z dokładnie 6 cyfr")
    private String indexNumber;

    // USUNIĘTE POLE EMAIL

    private LocalDateTime createdDate;

    private Boolean active = true;

    private Long groupId;

    private String groupName;

    private String fullName;

    private Integer attendanceCount;

    private Integer presentCount;

    private Integer absentCount;

    private Double attendanceRate;

    // Constructors
    public StudentDTO() {}

    public StudentDTO(String firstName, String lastName, String indexNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
        this.fullName = firstName + " " + lastName;
    }

    public StudentDTO(String firstName, String lastName, String indexNumber, String groupName) {
        this(firstName, lastName, indexNumber);
        this.groupName = groupName;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateFullName();
    }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateFullName();
    }

    public String getIndexNumber() { return indexNumber; }
    public void setIndexNumber(String indexNumber) { this.indexNumber = indexNumber; }

    // USUNIĘTE GETTERY I SETTERY DLA EMAIL

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Long getGroupId() { return groupId; }
    public void setGroupId(Long groupId) { this.groupId = groupId; }

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getFullName() {
        if (fullName == null) {
            updateFullName();
        }
        return fullName;
    }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public Integer getAttendanceCount() { return attendanceCount; }
    public void setAttendanceCount(Integer attendanceCount) { this.attendanceCount = attendanceCount; }

    public Integer getPresentCount() { return presentCount; }
    public void setPresentCount(Integer presentCount) { this.presentCount = presentCount; }

    public Integer getAbsentCount() { return absentCount; }
    public void setAbsentCount(Integer absentCount) { this.absentCount = absentCount; }

    public Double getAttendanceRate() { return attendanceRate; }
    public void setAttendanceRate(Double attendanceRate) { this.attendanceRate = attendanceRate; }

    // Helper method
    private void updateFullName() {
        if (firstName != null && lastName != null) {
            this.fullName = firstName + " " + lastName;
        }
    }
}