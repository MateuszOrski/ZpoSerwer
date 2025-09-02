package com.attendance.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazwa terminu jest wymagana")
    @Size(max = 255, message = "Nazwa terminu nie może być dłuższa niż 255 znaków")
    @Column(name = "subject", nullable = false)
    private String subject;

    @Size(max = 100, message = "Sala nie może być dłuższa niż 100 znaków")
    @Column(name = "classroom", length = 100)
    private String classroom;

    @NotNull(message = "Data rozpoczęcia jest wymagana")
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull(message = "Data zakończenia jest wymagana")
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Size(max = 100, message = "Prowadzący nie może być dłuższy niż 100 znaków")
    @Column(name = "instructor", length = 100)
    private String instructor;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attendance> attendances;

    public Schedule() {
        this.createdDate = LocalDateTime.now();
    }

    public Schedule(String subject, LocalDateTime startTime, LocalDateTime endTime, Group group) {
        this();
        this.subject = subject;
        this.startTime = startTime;
        this.endTime = endTime;
        this.group = group;
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

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public List<Attendance> getAttendances() { return attendances; }
    public void setAttendances(List<Attendance> attendances) { this.attendances = attendances; }

    public String getGroupName() {
        return group != null ? group.getName() : null;
    }
}