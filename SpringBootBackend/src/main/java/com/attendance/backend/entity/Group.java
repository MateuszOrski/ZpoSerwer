package com.attendance.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "`groups`")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazwa grupy jest wymagana")
    @Size(max = 100, message = "Nazwa grupy nie może być dłuższa niż 100 znaków")
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @NotBlank(message = "Specjalizacja jest wymagana")
    @Size(max = 100, message = "Specjalizacja nie może być dłuższa niż 100 znaków")
    @Column(name = "specialization", nullable = false, length = 100)
    private String specialization;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    // ⭐ KLUCZOWA ZMIANA: USUNIĘTO cascade = CascadeType.ALL
    // Teraz usuwanie grupy NIE usuwa studentów automatycznie
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @JsonIgnore  // Ignoruj students w JSON - unikaj rekursji
    private List<Student> students;

    // ⭐ KLUCZOWA ZMIANA: USUNIĘTO cascade = CascadeType.ALL dla schedules też
    // Harmonogramy będą usuwane osobno przez serwis
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    @JsonIgnore  // Ignoruj schedules w JSON - unikaj rekursji
    private List<Schedule> schedules;

    public Group() {
        this.createdDate = LocalDateTime.now();
    }

    public Group(String name, String specialization) {
        this();
        this.name = name;
        this.specialization = specialization;
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

    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }

    public List<Schedule> getSchedules() { return schedules; }
    public void setSchedules(List<Schedule> schedules) { this.schedules = schedules; }
}