package com.attendance.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Imię jest wymagane")
    @Size(max = 50, message = "Imię nie może być dłuższe niż 50 znaków")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Nazwisko jest wymagane")
    @Size(max = 50, message = "Nazwisko nie może być dłuższe niż 50 znaków")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotBlank(message = "Numer indeksu jest wymagany")
    @Pattern(regexp = "\\d{6}", message = "Numer indeksu musi składać się z dokładnie 6 cyfr")
    @Column(name = "index_number", nullable = false, unique = true, length = 6)
    private String indexNumber;


    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Attendance> attendances;

    public Student() {
        this.createdDate = LocalDateTime.now();
    }

    public Student(String firstName, String lastName, String indexNumber) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getIndexNumber() { return indexNumber; }
    public void setIndexNumber(String indexNumber) { this.indexNumber = indexNumber; }



    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public List<Attendance> getAttendances() { return attendances; }
    public void setAttendances(List<Attendance> attendances) { this.attendances = attendances; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getGroupName() {
        return group != null ? group.getName() : null;
    }
}