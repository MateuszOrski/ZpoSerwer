package com.attendance.backend.repository;

import com.attendance.backend.entity.Group;
import com.attendance.backend.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByGroup(Group group);

    List<Schedule> findByGroupOrderByStartTimeDesc(Group group);

    @Query("SELECT s FROM Schedule s WHERE s.startTime BETWEEN :startDate AND :endDate ORDER BY s.startTime")
    List<Schedule> findByStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT s FROM Schedule s WHERE s.group = :group AND s.startTime >= :fromDate ORDER BY s.startTime")
    List<Schedule> findUpcomingSchedulesByGroup(Group group, LocalDateTime fromDate);
}