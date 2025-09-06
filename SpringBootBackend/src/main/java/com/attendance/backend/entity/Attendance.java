package com.attendance.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "attendances", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "schedule_id"})
})
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) // ZMIENIONE na EAGER dla łatwiejszego dostępu
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER) // ZMIENIONE na EAGER
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttendanceStatus status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "marked_at", nullable = false)
    private LocalDateTime markedAt;

    @Column(name = "justified", nullable = false)
    private Boolean justified = false;

    public enum AttendanceStatus {
        PRESENT("Obecny"),
        LATE("Spóźniony"),
        ABSENT("Nieobecny");

        private final String displayName;

        AttendanceStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Konstruktory
    public Attendance() {
        this.markedAt = LocalDateTime.now();
    }

    public Attendance(Student student, Schedule schedule, AttendanceStatus status) {
        this();
        this.student = student;
        this.schedule = schedule;
        this.status = status;
    }

    public Attendance(Student student, Schedule schedule, AttendanceStatus status, String notes) {
        this(student, schedule, status);
        this.notes = notes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
        this.markedAt = LocalDateTime.now();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getMarkedAt() {
        return markedAt;
    }

    public void setMarkedAt(LocalDateTime markedAt) {
        this.markedAt = markedAt;
    }

    public Boolean getJustified() {
        return justified;
    }

    public void setJustified(Boolean justified) {
        this.justified = justified;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", student=" + (student != null ? student.getFullName() : "null") +
                ", status=" + status +
                ", markedAt=" + markedAt +
                '}';
    }
}