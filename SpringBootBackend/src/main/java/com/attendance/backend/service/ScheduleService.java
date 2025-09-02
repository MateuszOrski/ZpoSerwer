package com.attendance.backend.service;

import com.attendance.backend.entity.Schedule;
import com.attendance.backend.entity.Group;
import com.attendance.backend.repository.ScheduleRepository;
import com.attendance.backend.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private GroupRepository groupRepository;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    public List<Schedule> getSchedulesByGroup(Group group) {
        return scheduleRepository.findByGroupOrderByStartTimeDesc(group);
    }

    public List<Schedule> getSchedulesByGroupName(String groupName) {
        Optional<Group> group = groupRepository.findByName(groupName);
        if (group.isPresent()) {
            return scheduleRepository.findByGroupOrderByStartTimeDesc(group.get());
        }
        return List.of();
    }

    public Schedule saveSchedule(Schedule schedule) {
        // Jeśli brak grupy w obiekcie, ale jest groupName w requescie
        if (schedule.getGroup() == null && schedule.getGroupName() != null) {
            Optional<Group> group = groupRepository.findByName(schedule.getGroupName());
            if (group.isPresent()) {
                schedule.setGroup(group.get());
            }
        }
        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    public List<Schedule> getUpcomingSchedules(Group group) {
        return scheduleRepository.findUpcomingSchedulesByGroup(group, LocalDateTime.now());
    }
}