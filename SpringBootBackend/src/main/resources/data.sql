-- Przykładowe grupy
INSERT INTO groups (name, specialization, created_date, active) VALUES
('Grupa INF-A', 'Informatyka', CURRENT_TIMESTAMP, true),
('Grupa MAT-B', 'Matematyka', CURRENT_TIMESTAMP, true),
('Grupa FIZ-C', 'Fizyka', CURRENT_TIMESTAMP, true);

-- Przykładowi studenci
INSERT INTO students (first_name, last_name, index_number, email, created_date, active, group_id) VALUES
('Jan', 'Kowalski', '123456', 'jan.kowalski@student.edu', CURRENT_TIMESTAMP, true, 1),
('Anna', 'Nowak', '123457', 'anna.nowak@student.edu', CURRENT_TIMESTAMP, true, 1),
('Piotr', 'Wiśniewski', '123458', 'piotr.wisniewski@student.edu', CURRENT_TIMESTAMP, true, 1),
('Maria', 'Dąbrowska', '123459', 'maria.dabrowska@student.edu', CURRENT_TIMESTAMP, true, 2),
('Tomasz', 'Lewandowski', '123460', 'tomasz.lewandowski@student.edu', CURRENT_TIMESTAMP, true, 2);

-- Studenci bez grupy
INSERT INTO students (first_name, last_name, index_number, email, created_date, active) VALUES
('Katarzyna', 'Zielińska', '123461', 'katarzyna.zielinska@student.edu', CURRENT_TIMESTAMP, true),
('Michał', 'Szymański', '123462', 'michal.szymanski@student.edu', CURRENT_TIMESTAMP, true);

-- Przykładowe terminy
INSERT INTO schedules (subject, classroom, start_time, end_time, instructor, notes, created_date, group_id) VALUES
('Egzamin z Javy', 'Sala 101', '2025-01-15 10:00:00', '2025-01-15 12:00:00', 'Dr Jan Nowak', 'Przynieść długopisy', CURRENT_TIMESTAMP, 1),
('Laboratorie z Baz Danych', 'Sala 205', '2025-01-16 14:00:00', '2025-01-16 16:00:00', 'Mgr Anna Kowalska', '', CURRENT_TIMESTAMP, 1),
('Wykład z Algorytmów', 'Aula Magna', '2025-01-17 08:00:00', '2025-01-17 10:00:00', 'Prof. Piotr Zalewski', 'Wykład obowiązkowy', CURRENT_TIMESTAMP, 2);

-- Przykładowe obecności
INSERT INTO attendances (student_id, schedule_id, status, notes, marked_at, justified) VALUES
(1, 1, 'PRESENT', 'Na czas', CURRENT_TIMESTAMP, false),
(2, 1, 'LATE', 'Spóźnił się 10 minut', CURRENT_TIMESTAMP, false),
(3, 1, 'ABSENT', 'Nieobecny', CURRENT_TIMESTAMP, false),
(1, 2, 'PRESENT', '', CURRENT_TIMESTAMP, false),
(2, 2, 'PRESENT', '', CURRENT_TIMESTAMP, false);