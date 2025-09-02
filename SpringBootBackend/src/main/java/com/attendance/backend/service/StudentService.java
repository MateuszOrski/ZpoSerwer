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
}