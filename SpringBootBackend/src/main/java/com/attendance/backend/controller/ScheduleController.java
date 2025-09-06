package com.attendance.backend.controller;

import com.attendance.backend.entity.Schedule;
import com.attendance.backend.entity.Group; // DODANY IMPORT
import com.attendance.backend.service.ScheduleService;
import com.attendance.backend.service.GroupService; // DODANY IMPORT
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList; // DODANY IMPORT

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "*")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private GroupService groupService; // DODANY SERWIS

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

    // DODANA METODA - pobieranie terminów według nazwy grupy
    @GetMapping("/group/{groupName}")
    public ResponseEntity<List<Schedule>> getSchedulesByGroupName(@PathVariable String groupName) {
        System.out.println("=== DEBUG GET SCHEDULES BY GROUP NAME ===");
        System.out.println("🔍 Otrzymana nazwa grupy: '" + groupName + "'");

        try {
            // Dekoduj nazwę grupy
            String decodedGroupName = java.net.URLDecoder.decode(groupName, java.nio.charset.StandardCharsets.UTF_8);
            System.out.println("🔍 Zdekodowana nazwa: '" + decodedGroupName + "'");

            // Sprawdź czy grupa istnieje
            Optional<Group> groupOpt = groupService.getGroupByName(decodedGroupName);
            if (groupOpt.isEmpty()) {
                System.out.println("❌ Grupa NIE znaleziona w bazie!");
                return ResponseEntity.ok(new ArrayList<>());
            }

            Group group = groupOpt.get();
            System.out.println("✅ Grupa znaleziona: " + group.getName() + " (ID: " + group.getId() + ")");

            // Pobierz terminy dla grupy
            List<Schedule> schedules = scheduleService.getSchedulesByGroupName(decodedGroupName);
            System.out.println("📋 Znaleziono " + schedules.size() + " terminów dla grupy '" + decodedGroupName + "'");

            // Debug - wypisz szczegóły każdego terminu
            for (int i = 0; i < schedules.size(); i++) {
                Schedule s = schedules.get(i);
                System.out.println("  " + (i+1) + ". " + s.getSubject() +
                        " (data: " + s.getStartTime() + ", grupa: " + s.getGroupName() + ")");
            }

            return ResponseEntity.ok(schedules);

        } catch (Exception e) {
            System.err.println("❌ Błąd w getSchedulesByGroupName: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping
    public ResponseEntity<Schedule> createSchedule(@Valid @RequestBody Schedule schedule) {
        System.out.println("=== BACKEND: OTRZYMANO REQUEST TWORZENIA TERMINU ===");
        System.out.println("📋 Subject: " + schedule.getSubject());
        System.out.println("📅 Start: " + schedule.getStartTime());
        System.out.println("📅 End: " + schedule.getEndTime());
        System.out.println("🏫 Group: " + (schedule.getGroup() != null ? schedule.getGroup().getName() : "NULL"));
        System.out.println("🏫 GroupName: " + schedule.getGroupName());

        try {
            // Jeśli brak grupy w obiekcie, ale jest groupName w requescie
            if (schedule.getGroup() == null && schedule.getGroupName() != null) {
                System.out.println("🔍 Szukam grupy o nazwie: " + schedule.getGroupName());
                Optional<Group> group = groupService.getGroupByName(schedule.getGroupName());
                if (group.isPresent()) {
                    schedule.setGroup(group.get());
                    System.out.println("✅ Znaleziono i przypisano grupę: " + group.get().getName());
                } else {
                    System.err.println("❌ Nie znaleziono grupy o nazwie: " + schedule.getGroupName());
                    return ResponseEntity.badRequest().build();
                }
            }

            // Walidacja - termin musi mieć grupę
            if (schedule.getGroup() == null) {
                System.err.println("❌ Termin nie ma przypisanej grupy!");
                return ResponseEntity.badRequest().build();
            }

            // Zapisz termin przez serwis (który obsłuży managed entities)
            Schedule savedSchedule = scheduleService.saveSchedule(schedule);
            System.out.println("✅ Termin zapisany z ID: " + savedSchedule.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(savedSchedule);

        } catch (RuntimeException e) {
            System.err.println("❌ Błąd logiki biznesowej: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.err.println("❌ Błąd wewnętrzny serwera: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
        Optional<Schedule> schedule = scheduleService.getScheduleById(id);
        if (schedule.isPresent()) {
            scheduleService.deleteSchedule(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}