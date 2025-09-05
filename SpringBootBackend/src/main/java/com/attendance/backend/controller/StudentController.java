package com.attendance.backend.controller;

import com.attendance.backend.entity.Student;
import com.attendance.backend.entity.Group;  // ⭐ DODAJ TEN IMPORT
import com.attendance.backend.service.StudentService;
import com.attendance.backend.service.GroupService;  // ⭐ DODAJ TEN IMPORT
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private GroupService groupService; // Teraz powinno działać!

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllActiveStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{indexNumber}")
    public ResponseEntity<Student> getStudentByIndex(@PathVariable String indexNumber) {
        return studentService.getStudentByIndexNumber(indexNumber)
                .map(student -> ResponseEntity.ok(student))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/group/{groupName}")
    public ResponseEntity<List<Student>> getStudentsByGroup(@PathVariable String groupName) {
        System.out.println("=== DEBUG GET STUDENTS BY GROUP ===");
        System.out.println("🔍 Otrzymana nazwa grupy: '" + groupName + "'");
        System.out.println("🔍 Długość nazwy: " + groupName.length());
        System.out.println("🔍 Zakodowana nazwa: " + java.net.URLDecoder.decode(groupName, java.nio.charset.StandardCharsets.UTF_8));

        try {
            // Dekoduj nazwę grupy (na wypadek problemów z kodowaniem)
            String decodedGroupName = java.net.URLDecoder.decode(groupName, java.nio.charset.StandardCharsets.UTF_8);
            System.out.println("🔍 Zdekodowana nazwa: '" + decodedGroupName + "'");

            // Sprawdź czy grupa istnieje
            Optional<Group> groupOpt = groupService.getGroupByName(decodedGroupName);
            if (groupOpt.isPresent()) {
                System.out.println("✅ Grupa znaleziona w bazie: " + groupOpt.get().getName() + " (ID: " + groupOpt.get().getId() + ")");
            } else {
                System.out.println("❌ Grupa NIE znaleziona w bazie!");

                // Sprawdź wszystkie grupy
                List<Group> allGroups = groupService.getAllActiveGroups();
                System.out.println("📋 Wszystkie grupy w bazie:");
                for (Group g : allGroups) {
                    System.out.println("  - '" + g.getName() + "' (ID: " + g.getId() + ")");
                }
            }

            // Pobierz studentów
            List<Student> students = studentService.getStudentsByGroupName(decodedGroupName);
            System.out.println("📋 Znaleziono " + students.size() + " studentów w grupie '" + decodedGroupName + "'");

            // Wypisz szczegóły każdego studenta
            for (int i = 0; i < students.size(); i++) {
                Student s = students.get(i);
                System.out.println("  " + (i+1) + ". " + s.getFullName() +
                        " (index: " + s.getIndexNumber() +
                        ", grupa: '" + (s.getGroup() != null ? s.getGroup().getName() : "NULL") + "')");
            }

            // Sprawdź też wszystkich studentów
            List<Student> allStudents = studentService.getAllActiveStudents();
            System.out.println("📊 WSZYSTKICH studentów w bazie: " + allStudents.size());

            long studentsWithThisGroup = allStudents.stream()
                    .filter(s -> s.getGroup() != null && s.getGroup().getName().equals(decodedGroupName))
                    .count();
            System.out.println("📊 Studentów z grupą '" + decodedGroupName + "': " + studentsWithThisGroup);

            System.out.println("================================");

            return ResponseEntity.ok(students);

        } catch (Exception e) {
            System.err.println("❌ Błąd w getStudentsByGroup: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/without-group")
    public ResponseEntity<List<Student>> getStudentsWithoutGroup() {
        List<Student> students = studentService.getStudentsWithoutGroup();
        return ResponseEntity.ok(students);
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        if (studentService.existsByIndexNumber(student.getIndexNumber())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Student savedStudent = studentService.saveStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
    }

    @PutMapping("/{indexNumber}")
    public ResponseEntity<Student> updateStudent(@PathVariable String indexNumber,
                                                 @Valid @RequestBody Student student) {
        System.out.println("=== OTRZYMANO REQUEST PUT ===");
        System.out.println("Index: " + indexNumber);
        System.out.println("Student firstName: " + student.getFirstName());
        System.out.println("Student lastName: " + student.getLastName());
        System.out.println("Student group: " + (student.getGroup() != null ? student.getGroup().getName() : "NULL"));

        Optional<Student> existingStudentOpt = studentService.getStudentByIndexNumber(indexNumber);

        if (existingStudentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Student existingStudent = existingStudentOpt.get();

        // Aktualizuj podstawowe dane
        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());

        // 🔧 POPRAWKA: Prawidłowa obsługa grupy
        if (student.getGroup() != null && student.getGroup().getName() != null) {
            String groupName = student.getGroup().getName().trim();
            System.out.println("🔍 Szukam grupy w bazie: '" + groupName + "'");

            if (!groupName.isEmpty()) {
                // Znajdź ISTNIEJĄCĄ grupę w bazie danych
                Optional<Group> groupOpt = groupService.getGroupByName(groupName);
                if (groupOpt.isPresent()) {
                    Group existingGroup = groupOpt.get();
                    existingStudent.setGroup(existingGroup);
                    System.out.println("✅ Przypisano istniejącą grupę: " + groupName + " (ID: " + existingGroup.getId() + ")");
                } else {
                    System.out.println("❌ Nie znaleziono grupy: " + groupName);
                    existingStudent.setGroup(null);
                }
            } else {
                System.out.println("🔄 Pusta nazwa grupy - ustawiam null");
                existingStudent.setGroup(null);
            }
        } else {
            System.out.println("🔄 Brak grupy w requescie - ustawiam null");
            existingStudent.setGroup(null);
        }

        try {
            Student updatedStudent = studentService.saveStudent(existingStudent);
            System.out.println("💾 Zapisano studenta: " + updatedStudent.getFullName() +
                    " w grupie: " + (updatedStudent.getGroup() != null ? updatedStudent.getGroup().getName() : "BRAK"));

            return ResponseEntity.ok(updatedStudent);
        } catch (Exception e) {
            System.err.println("❌ Błąd zapisywania studenta: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{indexNumber}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String indexNumber) {
        Optional<Student> student = studentService.getStudentByIndexNumber(indexNumber);
        if (student.isPresent()) {
            studentService.deleteStudent(student.get().getId());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/remove-from-group/{indexNumber}")
    public ResponseEntity<Student> removeStudentFromGroup(@PathVariable String indexNumber) {
        System.out.println("=== CONTROLLER: REMOVE STUDENT FROM GROUP ===");
        System.out.println("🔍 Otrzymano request dla indeksu: " + indexNumber);

        try {
            // 🔧 KLUCZOWE: Używamy nowej metody StudentService
            Student updatedStudent = studentService.removeStudentFromGroup(indexNumber);

            System.out.println("✅ Controller: Student " + updatedStudent.getFullName() + " usunięty z grupy");
            return ResponseEntity.ok(updatedStudent);

        } catch (RuntimeException e) {
            System.err.println("❌ Controller: Błąd usuwania studenta z grupy: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("❌ Controller: Nieoczekiwany błąd: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}