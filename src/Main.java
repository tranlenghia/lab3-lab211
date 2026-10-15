
import business.StudentMng;
import business.CourseMng;
import model.Student;
import model.Course;
import validation.InputValidation;
import validation.StudentValidation;
import validation.CourseValidation;

import java.time.LocalDate;

public class Main {

    private StudentMng studentMng;
    private CourseMng courseMng;
    private InputValidation input;
    private boolean unsavedChanges;

    public static void main(String[] args) {
        new Main().start();
    }

    // 1. Start the program
    private void start() {

        studentMng = new StudentMng();
        courseMng = new CourseMng();
        input = new InputValidation();
        unsavedChanges = false;

        // Load data from text files
        studentMng.loadData("Students.txt");
        courseMng.loadData("Courses.txt", studentMng);

        boolean running = true;

        while (running) {

            displayMenu();

            int choice = input.inputInt("Choose: ");

            switch (choice) {

                case 1:
                    studentMng.displayAllStudents();
                    break;

                case 2:
                    addNewStudent();
                    break;

                case 3:
                    searchStudentById();
                    break;

                case 4:
                    updateStudentGpa();
                    break;

                case 5:
                    listStudentsByMajor();
                    break;

                case 6:
                    addNewCourse();
                    break;

                case 7:
                    courseMng.displayCoursesGroupedByStudent(
                            studentMng);
                    break;

                case 8:
                    calculateTotalDuration();
                    break;

                case 9:
                    removeStudent();
                    break;

                case 10:
                    studentMng.displayStudentsSortedByGpa();
                    break;

                case 11:
                    saveAllData();
                    break;

                case 12:
                    if (confirmQuit()) {
                        running = false;
                    }
                    break;

                default:
                    System.out.println(
                            "Invalid choice. Please choose 1 to 12.");
            }
        }

        System.out.println("Good bye.");
    }

    // 2. Display main menu
    private void displayMenu() {

        System.out.println();
        System.out.println(
                "==================================================");
        System.out.println(
                " STUDENT AND SHORT COURSE MANAGEMENT SYSTEM");
        System.out.println(
                "==================================================");

        System.out.println("1. List all students");
        System.out.println("2. Add a new student");
        System.out.println("3. Search student by ID");
        System.out.println("4. Update student by ID (GPA)");
        System.out.println("5. List students by major");
        System.out.println("6. Add a new course");
        System.out.println(
                "7. List courses by student (grouped)");
        System.out.println(
                "8. Calculate total duration by student ID");
        System.out.println("9. Remove a student by ID");
        System.out.println("10. Sort students by GPA");
        System.out.println("11. Save data to files");
        System.out.println("12. Quit program");

        System.out.println(
                "==================================================");
    }

    // 3. Add a new student
    private void addNewStudent() {

        StudentValidation validation
                = new StudentValidation();

        String studentId;

        while (true) {

            studentId = input.inputString("Student ID: ")
                    .toUpperCase();

            if (!validation.isValidStudentId(studentId)) {
                System.out.println(
                        "Invalid ID. Use STU followed by 4 digits.");
                continue;
            }

            if (studentMng.studentExists(studentId)) {
                System.out.println(
                        "Student ID already exists.");
                continue;
            }

            break;
        }

        String fullName;

        do {
            fullName = input.inputString("Full Name: ");

            if (!validation.isValidFullName(fullName)) {
                System.out.println(
                        "Full name must contain at least two words.");
            }

        } while (!validation.isValidFullName(fullName));

        String major;

        do {
            major = input.inputString("Major: ");

            if (!validation.isValidMajor(major)) {
                System.out.println("Major cannot be empty.");
            }

        } while (!validation.isValidMajor(major));

        double gpa;

        do {
            gpa = input.inputDouble("GPA (0.0 - 4.0): ");

            if (!validation.isValidGpa(gpa)) {
                System.out.println(
                        "GPA must be between 0.0 and 4.0.");
            }

        } while (!validation.isValidGpa(gpa));

        Student student = new Student(
                studentId, fullName, major, gpa);

        if (studentMng.addStudent(student)) {
            unsavedChanges = true;
        }
    }

    // 4. Search student by ID
    private void searchStudentById() {

        String studentId
                = input.inputString("Student ID: ");

        Student student
                = studentMng.findStudentById(studentId);

        if (student == null) {

            System.out.println(
                    "Student ID does not exist!");

            return;
        }

        System.out.println();
        System.out.println(
                "----------------------------------------------------------------------------");

        System.out.printf(
                "%-10s | %-25s | %-28s | %s%n",
                "Student ID",
                "Full Name",
                "Major",
                "GPA");

        System.out.println(
                "----------------------------------------------------------------------------");

        System.out.println(student);

        System.out.println(
                "----------------------------------------------------------------------------");
    }

    // 5. Update student GPA
    private void updateStudentGpa() {

        String studentId
                = input.inputString("Student ID: ");

        Student student
                = studentMng.findStudentById(studentId);

        if (student == null) {

            System.out.println(
                    "Student ID does not exist!");

            return;
        }

        System.out.println(
                "Current GPA: " + student.getGpa());

        double newGpa;

        do {
            newGpa = input.inputDouble(
                    "New GPA (0.0 - 4.0): ");

            if (newGpa < 0.0 || newGpa > 4.0) {
                System.out.println(
                        "GPA must be between 0.0 and 4.0.");
            }

        } while (newGpa < 0.0 || newGpa > 4.0);

        if (studentMng.updateGpa(studentId, newGpa)) {
            unsavedChanges = true;
        }
    }

    // 6. List students by major
    private void listStudentsByMajor() {

        String major
                = input.inputString("Enter Major: ");

        studentMng.displayStudentsByMajor(major);
    }

    // 7. Add a new course
    private void addNewCourse() {

        CourseValidation validation
                = new CourseValidation();

        // Check whether students are available
        if (studentMng.isEmpty()) {

            System.out.println(
                    "No students available. Add a student first.");

            return;
        }

// Display the list of existing students
        System.out.println();
        System.out.println("Select a student:");

        for (int i = 0;
                i < studentMng.getStudentList().size();
                i++) {

            Student student
                    = studentMng.getStudentList().get(i);

            System.out.printf(
                    "%d. %s - %s%n",
                    i + 1,
                    student.getStudentId(),
                    student.getFullName());
        }

// Enter Student ID directly
        String studentId;

        while (true) {

            studentId = input.inputString(
                    "Enter Student ID (e.g. STU0001): ")
                    .trim()
                    .toUpperCase();

            // Check Student ID format
            if (!validation.isValidStudentId(studentId)) {

                System.out.println(
                        "Invalid Student ID. Please use STU followed by 4 digits.");

                continue;
            }

            // Check whether the student exists
            if (!studentMng.studentExists(studentId)) {

                System.out.println(
                        "Student ID does not exist. Please choose an ID from the list.");

                continue;
            }

            break;
        }
        String courseId;

        while (true) {

            courseId
                    = input.inputString("Course ID: ");

            if (!validation.isValidCourseId(courseId)) {

                System.out.println(
                        "Course ID cannot be empty.");

                continue;
            }

            if (courseMng.courseExists(courseId)) {

                System.out.println(
                        "Course ID already exists.");

                continue;
            }

            break;
        }

        String courseName;

        do {
            courseName
                    = input.inputString("Course Name: ");

            if (!validation.isValidCourseName(courseName)) {
                System.out.println(
                        "Course Name cannot be empty.");
            }

        } while (!validation.isValidCourseName(courseName));

        int duration;

        do {
            duration
                    = input.inputInt("Duration (weeks): ");

            if (!validation.isValidDuration(duration)) {
                System.out.println(
                        "Duration must be at least 1 week.");
            }

        } while (!validation.isValidDuration(duration));

        LocalDate startDate;

        do {
            startDate = input.inputDate("Start Date");

            if (!validation.isValidStartDate(startDate)) {
                System.out.println(
                        "Start Date must be in the future.");
            }

        } while (!validation.isValidStartDate(startDate));

        Course course = new Course(
                courseId,
                studentId,
                courseName,
                duration,
                startDate);

        if (courseMng.addCourse(course, studentMng)) {
            unsavedChanges = true;
        }
    }

    // 8. Calculate total course duration
    private void calculateTotalDuration() {

        String studentId
                = input.inputString("Student ID: ");

        courseMng.displayTotalDurationByStudentId(
                studentId,
                studentMng);
    }

    // 9. Remove a student by ID
    private void removeStudent() {

        String studentId
                = input.inputString("Student ID: ");

        boolean removed
                = studentMng.removeStudentById(
                        studentId,
                        courseMng.getCourseList());

        if (removed) {
            unsavedChanges = true;
        }
    }

    // 10. Save both data files
    private boolean saveAllData() {

        boolean studentsSaved
                = studentMng.saveData("Students.txt");

        boolean coursesSaved
                = courseMng.saveData("Courses.txt");

        if (studentsSaved && coursesSaved) {

            unsavedChanges = false;

            System.out.println(
                    "All data saved successfully.");

            return true;
        }

        System.out.println(
                "Some data could not be saved.");

        return false;
    }

    // 11. Ask whether to save before quitting
    private boolean confirmQuit() {

        if (!unsavedChanges) {
            return true;
        }

        while (true) {

            String answer = input.inputString(
                    "Do you want to save the changes before exiting? (Y/N): ");

            if (answer.equalsIgnoreCase("Y")) {

                if (saveAllData()) {
                    return true;
                }

                String quitAnyway = input.inputString(
                        "Saving failed. Quit without saving? (Y/N): ");

                if (quitAnyway.equalsIgnoreCase("Y")) {
                    return true;
                }

                if (quitAnyway.equalsIgnoreCase("N")) {
                    return false;
                }

                System.out.println(
                        "Please enter Y or N.");

            } else if (answer.equalsIgnoreCase("N")) {

                return true;

            } else {

                System.out.println(
                        "Please enter Y or N.");
            }
        }
    }
}
