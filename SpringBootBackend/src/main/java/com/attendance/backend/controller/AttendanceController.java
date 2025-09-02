package com.attendance.backend.controller;

import com.attendance.backend.entity.Attendance;
import com.attendance.backend.service.AttendanceService;
import com.attendance.backend.dto.AttendanceDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/mark")
    public ResponseEntity<Attendance> markAttendance(@Valid @RequestBody Attendance attendance) {
        Attendance savedAttendance = attendanceService.markAttendance(attendance);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAttendance);
    }

    @PostMapping("/mark-student")
    public ResponseEntity<Attendance> markStudentAttendance(@Valid @RequestBody AttendanceDTO attendanceDTO) {
        try {
            Attendance attendance = attendanceService.markStudentAttendance(
                    attendanceDTO.getFirstName(),
                    attendanceDTO.getLastName(),
                    attendanceDTO.getIndexNumber(),
                    attendanceDTO.getGroupName(),
                    attendanceDTO.getScheduleId(),
                    attendanceDTO.getStatus(),
                    attendanceDTO.getNotes()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(attendance);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<List<Attendance>> getAttendancesBySchedule(@PathVariable Long scheduleId) {
        List<Attendance> attendances = attendanceService.getAttendancesByScheduleId(scheduleId);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/student/{indexNumber}")
    public ResponseEntity<List<Attendance>> getAttendancesByStudent(@PathVariable String indexNumber) {
        List<Attendance> attendances = attendanceService.getAttendancesByStudentIndexNumber(indexNumber);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/stats/group/{groupName}")
    public ResponseEntity<Map<String, Object>> getGroupAttendanceStats(@PathVariable String groupName) {
        Map<String, Object> stats = attendanceService.getGroupAttendanceStatistics(groupName);
        return ResponseEntity.ok(stats);
    }

    @DeleteMapping("/remove/{indexNumber}/{scheduleId}")
    public ResponseEntity<Void> removeAttendance(@PathVariable String indexNumber,
                                                 @PathVariable Long scheduleId) {
        try {
            attendanceService.removeAttendance(indexNumber, scheduleId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}