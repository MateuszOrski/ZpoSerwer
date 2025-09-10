package com.attendance.backend.service;

import com.attendance.backend.entity.Attendance;
import com.attendance.backend.entity.Student;
import com.attendance.backend.entity.Schedule;
import com.attendance.backend.entity.Group;
import com.attendance.backend.repository.AttendanceRepository;
import com.attendance.backend.repository.StudentRepository;
import com.attendance.backend.repository.ScheduleRepository;
import com.attendance.backend.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private GroupRepository groupRepository;

    public Attendance markAttendance(Attendance attendance) {
        Optional<Attendance> existing = attendanceRepository.findByStudentAndSchedule(
                attendance.getStudent(), attendance.getSchedule());

        if (existing.isPresent()) {
            Attendance existingAttendance = existing.get();
            existingAttendance.setStatus(attendance.getStatus());
            existingAttendance.setNotes(attendance.getNotes());
            existingAttendance.setJustified(attendance.getJustified());
            return attendanceRepository.save(existingAttendance);
        } else {
            return attendanceRepository.save(attendance);
        }
    }

    public Attendance markStudentAttendance(String firstName, String lastName, String indexNumber,
                                            String groupName, Long scheduleId,
                                            Attendance.AttendanceStatus status, String notes) {
        Optional<Student> studentOpt = studentRepository.findByIndexNumber(indexNumber);
        Student student;

        if (studentOpt.isPresent()) {
            student = studentOpt.get();
        } else {
            student = new Student(firstName, lastName, indexNumber);
            if (groupName != null && !groupName.trim().isEmpty()) {
                Optional<Group> group = groupRepository.findByName(groupName);
                if (group.isPresent()) {
                    student.setGroup(group.get());
                }
            }

            student = studentRepository.save(student);
        }

        Optional<Schedule> scheduleOpt = scheduleRepository.findById(scheduleId);
        if (!scheduleOpt.isPresent()) {
            throw new RuntimeException("Schedule not found with id: " + scheduleId);
        }

        Attendance attendance = new Attendance(student, scheduleOpt.get(), status);
        attendance.setNotes(notes);

        return markAttendance(attendance);
    }

    public List<Attendance> getAttendancesByScheduleId(Long scheduleId) {
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        if (schedule.isPresent()) {
            return attendanceRepository.findBySchedule(schedule.get());
        }
        return List.of();
    }

    public List<Attendance> getAttendancesByStudentIndexNumber(String indexNumber) {
        return attendanceRepository.findByStudentIndexNumber(indexNumber);
    }

    public Map<String, Object> getGroupAttendanceStatistics(String groupName) {
        List<Attendance> attendances = attendanceRepository.findByGroupName(groupName);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAttendances", attendances.size());

        long presentCount = attendances.stream()
                .filter(a -> a.getStatus() == Attendance.AttendanceStatus.PRESENT)
                .count();

        long lateCount = attendances.stream()
                .filter(a -> a.getStatus() == Attendance.AttendanceStatus.LATE)
                .count();

        long absentCount = attendances.stream()
                .filter(a -> a.getStatus() == Attendance.AttendanceStatus.ABSENT)
                .count();

        stats.put("presentCount", presentCount);
        stats.put("lateCount", lateCount);
        stats.put("absentCount", absentCount);

        double attendanceRate = attendances.isEmpty() ? 0.0 :
                ((double) (presentCount + lateCount) / attendances.size()) * 100;
        stats.put("attendanceRate", Math.round(attendanceRate * 100.0) / 100.0);

        return stats;
    }

    public void removeAttendance(String indexNumber, Long scheduleId) {
        Optional<Student> student = studentRepository.findByIndexNumber(indexNumber);
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);

        if (student.isPresent() && schedule.isPresent()) {
            attendanceRepository.deleteByStudentAndSchedule(student.get(), schedule.get());
        } else {
            throw new RuntimeException("Student or Schedule not found");
        }
    }
}