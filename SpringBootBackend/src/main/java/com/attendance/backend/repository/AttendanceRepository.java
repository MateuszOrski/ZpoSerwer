package com.attendance.backend.repository;

import com.attendance.backend.entity.Attendance;
import com.attendance.backend.entity.Schedule;
import com.attendance.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByStudentAndSchedule(Student student, Schedule schedule);

    List<Attendance> findByStudent(Student student);

    List<Attendance> findBySchedule(Schedule schedule);

    List<Attendance> findByStudentIndexNumber(String indexNumber);

    void deleteByStudentAndSchedule(Student student, Schedule schedule);

    @Query("SELECT a FROM Attendance a WHERE a.schedule.group.name = :groupName")
    List<Attendance> findByGroupName(String groupName);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.schedule = :schedule AND a.status = :status")
    int countByScheduleAndStatus(Schedule schedule, Attendance.AttendanceStatus status);
}