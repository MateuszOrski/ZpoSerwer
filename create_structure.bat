@echo off
REM Tworzenie struktury projektu SpringBootBackend

set ROOT=SpringBootBackend

REM Katalogi
mkdir %ROOT%\src\main\java\com\attendance\backend\config
mkdir %ROOT%\src\main\java\com\attendance\backend\controller
mkdir %ROOT%\src\main\java\com\attendance\backend\entity
mkdir %ROOT%\src\main\java\com\attendance\backend\repository
mkdir %ROOT%\src\main\java\com\attendance\backend\service
mkdir %ROOT%\src\main\java\com\attendance\backend\dto
mkdir %ROOT%\src\main\resources

REM Pliki w backend/
type nul > %ROOT%\src\main\java\com\attendance\backend\AttendanceBackendApplication.java

REM Config
type nul > %ROOT%\src\main\java\com\attendance\backend\config\CorsConfig.java

REM Controller
type nul > %ROOT%\src\main\java\com\attendance\backend\controller\GroupController.java
type nul > %ROOT%\src\main\java\com\attendance\backend\controller\StudentController.java
type nul > %ROOT%\src\main\java\com\attendance\backend\controller\ScheduleController.java
type nul > %ROOT%\src\main\java\com\attendance\backend\controller\AttendanceController.java

REM Entity
type nul > %ROOT%\src\main\java\com\attendance\backend\entity\Group.java
type nul > %ROOT%\src\main\java\com\attendance\backend\entity\Student.java
type nul > %ROOT%\src\main\java\com\attendance\backend\entity\Schedule.java
type nul > %ROOT%\src\main\java\com\attendance\backend\entity\Attendance.java

REM Repository
type nul > %ROOT%\src\main\java\com\attendance\backend\repository\GroupRepository.java
type nul > %ROOT%\src\main\java\com\attendance\backend\repository\StudentRepository.java
type nul > %ROOT%\src\main\java\com\attendance\backend\repository\ScheduleRepository.java
type nul > %ROOT%\src\main\java\com\attendance\backend\repository\AttendanceRepository.java

REM Service
type nul > %ROOT%\src\main\java\com\attendance\backend\service\GroupService.java
type nul > %ROOT%\src\main\java\com\attendance\backend\service\StudentService.java
type nul > %ROOT%\src\main\java\com\attendance\backend\service\ScheduleService.java
type nul > %ROOT%\src\main\java\com\attendance\backend\service\AttendanceService.java

REM DTO
type nul > %ROOT%\src\main\java\com\attendance\backend\dto\GroupDTO.java
type nul > %ROOT%\src\main\java\com\attendance\backend\dto\StudentDTO.java
type nul > %ROOT%\src\main\java\com\attendance\backend\dto\ScheduleDTO.java
type nul > %ROOT%\src\main\java\com\attendance\backend\dto\AttendanceDTO.java

REM Resources
type nul > %ROOT%\src\main\resources\application.yml
type nul > %ROOT%\src\main\resources\data.sql

REM Główny plik Maven
type nul > %ROOT%\pom.xml

echo Struktura projektu zostala utworzona w folderze %ROOT%.
pause

