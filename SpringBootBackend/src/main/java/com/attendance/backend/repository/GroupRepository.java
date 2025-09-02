package com.attendance.backend.repository;

import com.attendance.backend.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByName(String name);

    List<Group> findByActiveTrue();

    boolean existsByName(String name);

    @Query("SELECT g FROM Group g WHERE g.active = true ORDER BY g.name")
    List<Group> findAllActiveOrderByName();

    @Query("SELECT COUNT(s) FROM Student s WHERE s.group = :group AND s.active = true")
    int countActiveStudentsByGroup(Group group);
}