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

    // DODANA METODA - pobieranie terminów według nazwy grupy
    public List<Schedule> getSchedulesByGroupName(String groupName) {
        System.out.println("🔄 ScheduleService: getSchedulesByGroupName dla grupy: '" + groupName + "'");

        Optional<Group> group = groupRepository.findByName(groupName);
        if (group.isPresent()) {
            List<Schedule> schedules = scheduleRepository.findByGroupOrderByStartTimeDesc(group.get());
            System.out.println("✅ ScheduleService: Znaleziono " + schedules.size() + " terminów");
            return schedules;
        } else {
            System.out.println("❌ ScheduleService: Grupa '" + groupName + "' nie została znaleziona");
            return List.of();
        }
    }

    // ================================
    // 🔧 GŁÓWNA POPRAWKA - saveSchedule
    // ================================
    public Schedule saveSchedule(Schedule schedule) {
        System.out.println("=== BACKEND: SAVE SCHEDULE ===");
        System.out.println("📋 Subject: " + schedule.getSubject());
        System.out.println("📅 Start: " + schedule.getStartTime());
        System.out.println("🏫 Group w obiekcie: " + (schedule.getGroup() != null ? schedule.getGroup().getName() : "NULL"));
        System.out.println("🏫 GroupName: " + schedule.getGroupName());

        try {
            // 🔧 KLUCZOWA POPRAWKA: Znajdź i przypisz MANAGED Group entity
            if (schedule.getGroup() != null) {
                String groupName = schedule.getGroup().getName();
                System.out.println("🔍 Szukam istniejącej grupy: " + groupName);

                Optional<Group> managedGroup = groupRepository.findByName(groupName);
                if (managedGroup.isPresent()) {
                    // Przypisz MANAGED entity z bazy danych
                    schedule.setGroup(managedGroup.get());
                    System.out.println("✅ Przypisano managed group: " + managedGroup.get().getName() + " (ID: " + managedGroup.get().getId() + ")");
                } else {
                    System.err.println("❌ Nie znaleziono grupy: " + groupName);
                    throw new RuntimeException("Grupa '" + groupName + "' nie istnieje w bazie danych!");
                }
            }
            // Fallback - jeśli brak group ale jest groupName (stara logika)
            else if (schedule.getGroupName() != null && !schedule.getGroupName().trim().isEmpty()) {
                System.out.println("🔍 Fallback: szukam grupy po groupName: " + schedule.getGroupName());

                Optional<Group> group = groupRepository.findByName(schedule.getGroupName());
                if (group.isPresent()) {
                    schedule.setGroup(group.get());
                    System.out.println("✅ Fallback: Przypisano grupę: " + group.get().getName());
                } else {
                    System.err.println("❌ Fallback: Nie znaleziono grupy: " + schedule.getGroupName());
                    throw new RuntimeException("Grupa '" + schedule.getGroupName() + "' nie istnieje w bazie danych!");
                }
            } else {
                System.err.println("❌ Brak informacji o grupie w schedule!");
                throw new RuntimeException("Termin musi być przypisany do grupy!");
            }

            // Walidacja - sprawdź czy grupa jest managed
            if (schedule.getGroup().getId() == null) {
                System.err.println("❌ Grupa nie ma ID - nie jest managed entity!");
                throw new RuntimeException("Błąd wewnętrzny: grupa nie jest managed entity");
            }

            // Zapisz schedule z managed Group entity
            Schedule savedSchedule = scheduleRepository.save(schedule);
            System.out.println("✅ Termin zapisany z ID: " + savedSchedule.getId());
            System.out.println("✅ Przypisany do grupy: " + savedSchedule.getGroup().getName() + " (ID: " + savedSchedule.getGroup().getId() + ")");

            return savedSchedule;

        } catch (Exception e) {
            System.err.println("❌ Błąd zapisywania terminu: " + e.getMessage());
            e.printStackTrace();
            throw e; // Przekaż błąd dalej
        }
    }

    public void deleteSchedule(Long id) {
        System.out.println("🗑️ Usuwam termin ID: " + id);
        scheduleRepository.deleteById(id);
        System.out.println("✅ Termin usunięty");
    }

    public List<Schedule> getUpcomingSchedules(Group group) {
        return scheduleRepository.findUpcomingSchedulesByGroup(group, LocalDateTime.now());
    }
}