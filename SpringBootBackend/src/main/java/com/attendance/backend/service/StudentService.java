package com.attendance.backend.service;

import com.attendance.backend.entity.Student;
import com.attendance.backend.entity.Group;
import com.attendance.backend.repository.StudentRepository;
import com.attendance.backend.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GroupRepository groupRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> getAllActiveStudents() {
        return studentRepository.findByActiveTrue();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Optional<Student> getStudentByIndexNumber(String indexNumber) {
        return studentRepository.findByIndexNumber(indexNumber);
    }

    public List<Student> getStudentsByGroup(Group group) {
        return studentRepository.findActiveStudentsByGroupOrderByName(group);
    }

    public List<Student> getStudentsByGroupName(String groupName) {
        Optional<Group> group = groupRepository.findByName(groupName);
        if (group.isPresent()) {
            return studentRepository.findActiveStudentsByGroupOrderByName(group.get());
        }
        return List.of();
    }

    public List<Student> getStudentsWithoutGroup() {
        return studentRepository.findActiveStudentsWithoutGroupOrderByName();
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public boolean existsByIndexNumber(String indexNumber) {
        return studentRepository.existsByIndexNumber(indexNumber);
    }

    public Student removeStudentFromGroup(String indexNumber) {
        System.out.println("=== SERVICE: REMOVE STUDENT FROM GROUP ===");
        System.out.println("🔍 Szukam studenta o indeksie: " + indexNumber);

        Optional<Student> studentOpt = studentRepository.findByIndexNumber(indexNumber);

        if (studentOpt.isEmpty()) {
            System.err.println("❌ Student o indeksie " + indexNumber + " nie został znaleziony");
            throw new RuntimeException("Student not found with index number: " + indexNumber);
        }

        Student student = studentOpt.get();
        System.out.println("✅ Znaleziono studenta: " + student.getFullName());
        System.out.println("📋 Obecna grupa: " + (student.getGroup() != null ? student.getGroup().getName() : "BRAK"));

        // Usuń studenta z grupy
        student.setGroup(null);

        try {
            Student updatedStudent = studentRepository.save(student);
            System.out.println("💾 Student " + updatedStudent.getFullName() + " został usunięty z grupy");
            System.out.println("📋 Nowa grupa: " + (updatedStudent.getGroup() != null ? updatedStudent.getGroup().getName() : "BRAK"));

            return updatedStudent;

        } catch (Exception e) {
            System.err.println("❌ Błąd zapisywania studenta: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error saving student: " + e.getMessage(), e);
        }
    }
}