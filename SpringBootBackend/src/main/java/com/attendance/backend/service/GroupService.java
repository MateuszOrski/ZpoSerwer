package com.attendance.backend.service;

import com.attendance.backend.entity.Group;
import com.attendance.backend.entity.Student;
import com.attendance.backend.entity.Schedule;
import com.attendance.backend.repository.GroupRepository;
import com.attendance.backend.repository.StudentRepository;
import com.attendance.backend.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    public List<Group> getAllActiveGroups() {
        return groupRepository.findAllActiveOrderByName();
    }

    public Optional<Group> getGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public Optional<Group> getGroupByName(String name) {
        return groupRepository.findByName(name);
    }

    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }

    /**
     * ⭐ NOWA LOGIKA USUWANIA GRUPY
     * 1. Znajdź wszystkich studentów tej grupy
     * 2. Ustaw im group = null (nie usuwaj studentów!)
     * 3. Opcjonalnie usuń harmonogramy grupy
     * 4. Usuń grupę
     */
    public void deleteGroup(Long id) {
        System.out.println("=== ROZPOCZĘCIE USUWANIA GRUPY (ID: " + id + ") ===");

        Optional<Group> groupOpt = groupRepository.findById(id);
        if (groupOpt.isEmpty()) {
            System.out.println("❌ Grupa o ID " + id + " nie istnieje");
            throw new RuntimeException("Grupa nie znaleziona");
        }

        Group group = groupOpt.get();
        System.out.println("🔍 Usuwam grupę: " + group.getName());

        // KROK 1: Znajdź wszystkich studentów tej grupy
        List<Student> studentsInGroup = studentRepository.findByGroup(group);
        System.out.println("👥 Znaleziono " + studentsInGroup.size() + " studentów w grupie");

        // KROK 2: Usuń studentów z grupy (ustaw group = null)
        for (Student student : studentsInGroup) {
            System.out.println("🔄 Usuwam studenta " + student.getFullName() + " z grupy");
            student.setGroup(null);
            studentRepository.save(student);
        }
        System.out.println("✅ Wszyscy studenci usunięci z grupy (pozostają w systemie)");

        // KROK 3: Usuń harmonogramy tej grupy (opcjonalnie)
        List<Schedule> schedulesInGroup = scheduleRepository.findByGroup(group);
        System.out.println("📅 Znaleziono " + schedulesInGroup.size() + " harmonogramów w grupie");

        if (!schedulesInGroup.isEmpty()) {
            scheduleRepository.deleteAll(schedulesInGroup);
            System.out.println("🗑️ Usunięto wszystkie harmonogramy grupy");
        }

        // KROK 4: Usuń grupę
        groupRepository.deleteById(id);
        System.out.println("✅ Grupa " + group.getName() + " została usunięta");
        System.out.println("📊 PODSUMOWANIE:");
        System.out.println("   - Studentów przeniesionych do stanu 'bez grupy': " + studentsInGroup.size());
        System.out.println("   - Usuniętych harmonogramów: " + schedulesInGroup.size());
        System.out.println("   - Grupa usunięta: " + group.getName());
        System.out.println("=====================================");
    }

    public boolean existsByName(String name) {
        return groupRepository.existsByName(name);
    }

    public int countStudentsInGroup(Group group) {
        return groupRepository.countActiveStudentsByGroup(group);
    }

    /**
     * ⭐ NOWA METODA - Usuń wszystkich studentów z grupy (bez usuwania grupy)
     */
    public void removeAllStudentsFromGroup(Long groupId) {
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        if (groupOpt.isEmpty()) {
            throw new RuntimeException("Grupa nie znaleziona");
        }

        Group group = groupOpt.get();
        List<Student> studentsInGroup = studentRepository.findByGroup(group);

        System.out.println("🔄 Usuwam " + studentsInGroup.size() + " studentów z grupy " + group.getName());

        for (Student student : studentsInGroup) {
            student.setGroup(null);
            studentRepository.save(student);
        }

        System.out.println("✅ Wszyscy studenci usunięci z grupy " + group.getName());
    }

    /**
     * ⭐ NOWA METODA - Sprawdź ile studentów i harmonogramów ma grupa przed usunięciem
     */
    public GroupDeletionInfo getGroupDeletionInfo(Long groupId) {
        Optional<Group> groupOpt = groupRepository.findById(groupId);
        if (groupOpt.isEmpty()) {
            return null;
        }

        Group group = groupOpt.get();
        List<Student> students = studentRepository.findByGroup(group);
        List<Schedule> schedules = scheduleRepository.findByGroup(group);

        return new GroupDeletionInfo(
                group.getName(),
                students.size(),
                schedules.size(),
                students.stream().map(Student::getFullName).toList(),
                schedules.stream().map(Schedule::getSubject).toList()
        );
    }

    /**
     * Klasa pomocnicza dla informacji o usuwaniu grupy
     */
    public static class GroupDeletionInfo {
        private final String groupName;
        private final int studentCount;
        private final int scheduleCount;
        private final List<String> studentNames;
        private final List<String> scheduleNames;

        public GroupDeletionInfo(String groupName, int studentCount, int scheduleCount,
                                 List<String> studentNames, List<String> scheduleNames) {
            this.groupName = groupName;
            this.studentCount = studentCount;
            this.scheduleCount = scheduleCount;
            this.studentNames = studentNames;
            this.scheduleNames = scheduleNames;
        }

        // Gettery
        public String getGroupName() { return groupName; }
        public int getStudentCount() { return studentCount; }
        public int getScheduleCount() { return scheduleCount; }
        public List<String> getStudentNames() { return studentNames; }
        public List<String> getScheduleNames() { return scheduleNames; }
    }
}