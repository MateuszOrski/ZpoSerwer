package com.attendance.backend.controller;

import com.attendance.backend.entity.Schedule;
import com.attendance.backend.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id)
                .map(schedule -> ResponseEntity.ok(schedule))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/group/{groupName}")
    public ResponseEntity<List<Schedule>> getSchedulesByGroup(@PathVariable String groupName) {
        List<Schedule> schedules = scheduleService.getSchedulesByGroupName(groupName);
        return ResponseEntity.ok(schedules);
    }

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@Valid @RequestBody Schedule schedule) {
        Schedule savedSchedule = scheduleService.saveSchedule(schedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSchedule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Schedule> updateSchedule(@PathVariable Long id,
                                                   @Valid @RequestBody Schedule schedule) {
        return scheduleService.getScheduleById(id)
                .map(existingSchedule -> {
                    existingSchedule.setSubject(schedule.getSubject());
                    existingSchedule.setClassroom(schedule.getClassroom());
                    existingSchedule.setStartTime(schedule.getStartTime());
                    existingSchedule.setEndTime(schedule.getEndTime());
                    existingSchedule.setInstructor(schedule.getInstructor());
                    existingSchedule.setNotes(schedule.getNotes());
                    Schedule updatedSchedule = scheduleService.saveSchedule(existingSchedule);
                    return ResponseEntity.ok(updatedSchedule);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        return scheduleService.getScheduleById(id)
                .map(schedule -> {
                    scheduleService.deleteSchedule(id);
                    return ResponseEntity.<Void>ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}