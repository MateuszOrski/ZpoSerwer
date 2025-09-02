package com.attendance.backend.repository;

import com.attendance.backend.entity.Group;
import com.attendance.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByIndexNumber(String indexNumber);

    List<Student> findByGroup(Group group);

    List<Student> findByGroupIsNull();

    List<Student> findByActiveTrue();

    boolean existsByIndexNumber(String indexNumber);

    @Query("SELECT s FROM Student s WHERE s.group = :group AND s.active = true ORDER BY s.lastName, s.firstName")
    List<Student> findActiveStudentsByGroupOrderByName(Group group);

    @Query("SELECT s FROM Student s WHERE s.group IS NULL AND s.active = true ORDER BY s.lastName, s.firstName")
    List<Student> findActiveStudentsWithoutGroupOrderByName();
}