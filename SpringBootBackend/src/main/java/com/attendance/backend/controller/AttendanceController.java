package com.attendance.backend.controller;

// WSZYSTKIE POTRZEBNE IMPORTY
import com.attendance.backend.entity.Attendance;
import com.attendance.backend.entity.Attendance.AttendanceStatus;
import com.attendance.backend.entity.Student;
import com.attendance.backend.entity.Schedule;
import com.attendance.backend.service.AttendanceService;
import com.attendance.backend.dto.AttendanceDTO;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/mark")
    public ResponseEntity<Attendance> markAttendance(@Valid @RequestBody Attendance attendance) {
        System.out.println("=== POST /api/attendance/mark ===");
        System.out.println("Otrzymano attendance: " + attendance);

        try {
            Attendance savedAttendance = attendanceService.markAttendance(attendance);
            System.out.println("✅ Zapisano attendance z ID: " + savedAttendance.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAttendance);
        } catch (Exception e) {
            System.err.println("❌ Błąd zapisywania attendance: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/mark-student")
    public ResponseEntity<Attendance> markStudentAttendance(@Valid @RequestBody AttendanceDTO attendanceDTO) {
        System.out.println("=== POST /api/attendance/mark-student ===");
        System.out.println("Otrzymano DTO: " + attendanceDTO.getFirstName() + " " + attendanceDTO.getLastName());
        System.out.println("Status: " + attendanceDTO.getStatus());
        System.out.println("Schedule ID: " + attendanceDTO.getScheduleId());

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
            System.out.println("✅ Zapisano attendance studenta z ID: " + attendance.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(attendance);
        } catch (RuntimeException e) {
            System.err.println("❌ Błąd zapisywania attendance studenta: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("❌ Nieoczekiwany błąd: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<List<Attendance>> getAttendancesBySchedule(@PathVariable Long scheduleId) {
        System.out.println("=== GET /api/attendance/schedule/" + scheduleId + " ===");

        try {
            List<Attendance> attendances = attendanceService.getAttendancesByScheduleId(scheduleId);
            System.out.println("📋 Znaleziono " + attendances.size() + " obecności dla schedule ID: " + scheduleId);

            // Debug - wypisz szczegóły każdej obecności
            for (int i = 0; i < attendances.size(); i++) {
                Attendance a = attendances.get(i);
                System.out.println("  " + (i+1) + ". " + a.getStudent().getFullName() +
                        " - " + a.getStatus() + " (" + a.getMarkedAt() + ")");
            }

            return ResponseEntity.ok(attendances);
        } catch (Exception e) {
            System.err.println("❌ Błąd pobierania obecności: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/student/{indexNumber}")
    public ResponseEntity<List<Attendance>> getAttendancesByStudent(@PathVariable String indexNumber) {
        System.out.println("=== GET /api/attendance/student/" + indexNumber + " ===");

        try {
            List<Attendance> attendances = attendanceService.getAttendancesByStudentIndexNumber(indexNumber);
            System.out.println("📋 Znaleziono " + attendances.size() + " obecności dla studenta: " + indexNumber);
            return ResponseEntity.ok(attendances);
        } catch (Exception e) {
            System.err.println("❌ Błąd pobierania obecności studenta: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/stats/group/{groupName}")
    public ResponseEntity<Map<String, Object>> getGroupAttendanceStats(@PathVariable String groupName) {
        System.out.println("=== GET /api/attendance/stats/group/" + groupName + " ===");

        try {
            Map<String, Object> stats = attendanceService.getGroupAttendanceStatistics(groupName);
            System.out.println("📊 Wygenerowano statystyki dla grupy: " + groupName);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            System.err.println("❌ Błąd generowania statystyk: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("error", "Could not generate stats");
            return ResponseEntity.ok(errorMap);
        }
    }

    @DeleteMapping("/remove/{indexNumber}/{scheduleId}")
    public ResponseEntity<Void> removeAttendance(@PathVariable String indexNumber,
                                                 @PathVariable Long scheduleId) {
        System.out.println("=== DELETE /api/attendance/remove/" + indexNumber + "/" + scheduleId + " ===");

        try {
            attendanceService.removeAttendance(indexNumber, scheduleId);
            System.out.println("✅ Usunięto attendance dla studenta: " + indexNumber + ", schedule: " + scheduleId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            System.err.println("❌ Nie znaleziono attendance do usunięcia: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("❌ Błąd usuwania attendance: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Health check
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Attendance API is running");
    }
}