package com.attendance.backend.controller;

import com.attendance.backend.entity.Student;
import com.attendance.backend.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllActiveStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{indexNumber}")
    public ResponseEntity<Student> getStudentByIndex(@PathVariable String indexNumber) {
        return studentService.getStudentByIndexNumber(indexNumber)
                .map(student -> ResponseEntity.ok(student))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/group/{groupName}")
    public ResponseEntity<List<Student>> getStudentsByGroup(@PathVariable String groupName) {
        List<Student> students = studentService.getStudentsByGroupName(groupName);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/without-group")
    public ResponseEntity<List<Student>> getStudentsWithoutGroup() {
        List<Student> students = studentService.getStudentsWithoutGroup();
        return ResponseEntity.ok(students);
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        if (studentService.existsByIndexNumber(student.getIndexNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Student savedStudent = studentService.saveStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @PutMapping("/{indexNumber}")
    public ResponseEntity<Student> updateStudent(@PathVariable String indexNumber,
                                                 @Valid @RequestBody Student student) {
        return studentService.getStudentByIndexNumber(indexNumber)
                .map(existingStudent -> {
                    existingStudent.setFirstName(student.getFirstName());
                    existingStudent.setLastName(student.getLastName());
                    existingStudent.setEmail(student.getEmail());
                    if (student.getGroup() != null) {
                        existingStudent.setGroup(student.getGroup());
                    }
                    Student updatedStudent = studentService.saveStudent(existingStudent);
                    return ResponseEntity.ok(updatedStudent);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{indexNumber}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String indexNumber) {
        return studentService.getStudentByIndexNumber(indexNumber)
                .map(student -> {
                    studentService.deleteStudent(student.getId());
                    return ResponseEntity.<Void>ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}