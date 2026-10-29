package business;

import java.util.ArrayList;
import model.Course;
import model.Student;
import fileio.CourseTextFile;
import validation.CourseValidation;

public class CourseMng {

    private ArrayList<Course> courseList;
    private CourseValidation courseValidation;
    private CourseTextFile courseTextFile;
    private final int outerContentWidth = 89;

    // 1. Constructor
    public CourseMng() {
        courseList = new ArrayList<>();
        courseValidation = new CourseValidation();
        courseTextFile = new CourseTextFile();
    }

    // 2. Get and set course list
    public ArrayList<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<Course> courseList) {

        if (courseList == null) {
            this.courseList = new ArrayList<>();
        } else {
            this.courseList = courseList;
        }
    }

    // 3. Find course by ID
    public Course findCourseById(String courseId) {

        if (courseId == null || courseId.trim().isEmpty()) {
            return null;
        }

        for (Course course : courseList) {

            if (course.getCourseId()
                    .equalsIgnoreCase(courseId.trim())) {

                return course;
            }
        }

        return null;
    }

    // 4. Check whether course ID exists
    public boolean courseExists(String courseId) {
        return findCourseById(courseId) != null;
    }

    // 5. Add a new course
    public boolean addCourse(
            Course course,
            StudentMng studentMng) {

        if (course == null || studentMng == null) {
            System.out.println("Invalid course data.");
            return false;
        }

        // Validate Course ID
        if (!courseValidation.isValidCourseId(
                course.getCourseId())) {

            System.out.println("Course ID cannot be empty.");
            return false;
        }

        // Check duplicate Course ID
        if (courseExists(course.getCourseId())) {

            System.out.println("Course ID already exists.");
            return false;
        }

        // Validate Student ID format
        if (!courseValidation.isValidStudentId(
                course.getStudentId())) {

            System.out.println("Invalid Student ID format.");
            return false;
        }

        // Check whether Student exists
        if (!studentMng.studentExists(
                course.getStudentId())) {

            System.out.println(
                    "Student ID does not exist!");
            return false;
        }

        // Validate Course Name
        if (!courseValidation.isValidCourseName(
                course.getCourseName())) {

            System.out.println(
                    "Course Name cannot be empty.");
            return false;
        }

        // Validate duration
        if (!courseValidation.isValidDuration(
                course.getDuration())) {

            System.out.println(
                    "Duration must be at least 1 week.");
            return false;
        }

        // Validate start date
        if (!courseValidation.isValidStartDate(
                course.getStartDate())) {

            System.out.println(
                    "Start Date must be in the future.");
            return false;
        }

        courseList.add(course);

        System.out.println("Course added successfully.");
        return true;
    }

    // 6. Find courses by Student ID
    public ArrayList<Course> findCoursesByStudentId(
            String studentId) {

        ArrayList<Course> result = new ArrayList<>();

        if (studentId == null || studentId.trim().isEmpty()) {
            return result;
        }

        for (Course course : courseList) {

            if (course.getStudentId()
                    .equalsIgnoreCase(studentId.trim())) {

                result.add(course);
            }
        }

        return result;
    }

    // 7. Display courses for one student
    public void displayCoursesByStudentId(
            String studentId,
            StudentMng studentMng) {

        Student student
                = studentMng.findStudentById(studentId);

        if (student == null) {

            System.out.println("Student ID does not exist!");
            return;
        }

        ArrayList<Course> result
                = findCoursesByStudentId(studentId);

        System.out.println();
        System.out.println(
                "==============================================================");
        System.out.println(
                "                    STUDENT COURSE LIST");
        System.out.println(
                "==============================================================");

        System.out.println(
                "Student ID   : " + student.getStudentId());

        System.out.println(
                "Student Name : " + student.getFullName());

        if (result.isEmpty()) {

            System.out.println(
                    "This student has no registered courses.");

            System.out.println(
                    "==============================================================");

            return;
        }

        printCourseHeader();

        for (Course course : result) {
            System.out.println(course);
        }

        printCourseSeparator();

        System.out.println(
                "Total courses: " + result.size());

        System.out.println(
                "==============================================================");
    }

    // 8. Display courses grouped by student
    public void displayCoursesGroupedByStudent(
            StudentMng studentMng) {

        if (studentMng == null) {

            System.out.println("Student management is unavailable.");
            return;
        }

        if (studentMng.isEmpty()) {

            System.out.println("No students found.");
            return;
        }

        System.out.println();
        System.out.println(
                "===================== COURSES GROUPED BY STUDENT =====================");

        for (Student student : studentMng.getStudentList()) {

            System.out.println();
            System.out.println(
                    "-----------------------------------------------------------------------------------");

            System.out.println(
                    "Student ID   : " + student.getStudentId());

            System.out.println(
                    "Student Name : " + student.getFullName());

            ArrayList<Course> result
                    = findCoursesByStudentId(
                            student.getStudentId());

            if (result.isEmpty()) {

                System.out.println(
                        "No courses registered.");

                continue;
            }

            printCourseHeader();

            for (Course course : result) {
                System.out.println(course);
            }

            printCourseSeparator();

            System.out.println(
                    "Total courses: " + result.size());
        }

        System.out.println();
        System.out.println(
                "========================================================================");
    }

    // 9. Calculate total duration by Student ID
    public int calculateTotalDurationByStudentId(
            String studentId) {

        int totalDuration = 0;

        if (studentId == null || studentId.trim().isEmpty()) {
            return totalDuration;
        }

        for (Course course : courseList) {

            if (course.getStudentId()
                    .equalsIgnoreCase(studentId.trim())) {

                totalDuration += course.getDuration();
            }
        }

        return totalDuration;
    }

    // 10. Display total duration by Student ID
    public void displayTotalDurationByStudentId(
            String studentId,
            StudentMng studentMng) {

        Student student
                = studentMng.findStudentById(studentId);

        if (student == null) {

            System.out.println("Student ID does not exist!");
            return;
        }

        int totalDuration
                = calculateTotalDurationByStudentId(studentId);

        System.out.println();
        System.out.println(
                "========== TOTAL COURSE DURATION ==========");

        System.out.println(
                "Student ID   : " + student.getStudentId());

        System.out.println(
                "Student Name : " + student.getFullName());

        System.out.println(
                "Total Weeks  : " + totalDuration);

        System.out.println(
                "============================================");
    }

    // 11. Display all courses
    public void displayAllCourses() {

        if (courseList.isEmpty()) {

            System.out.println("No courses found.");
            return;
        }

        System.out.println();
        printCourseHeader();

        for (Course course : courseList) {
            System.out.println(course);
        }

        printCourseSeparator();

        System.out.println(
                "Total courses: " + courseList.size());
    }

    // 12. Print course table header
    private void printCourseHeader() {

        printCourseSeparator();

        System.out.printf(
                "| %-10s | %-10s | %-28s | %7s | %-12s |%n",
                "Course ID",
                "Student ID",
                "Course Name",
                "Weeks",
                "Start Date"
        );

        printCourseSeparator();
    }

// Print top, middle and bottom borders
    private void printCourseSeparator() {

        System.out.println(
                "---------------------------"
                + "-------------------------------"
                + "-------------------------"
        );
    }

    // 14. Load courses from a text file
    public void loadData(String fileName, StudentMng studentMng) {

        courseList = courseTextFile.load(fileName);

        if (courseList == null) {
            courseList = new ArrayList<>();
            return;
        }

        if (studentMng == null) {
            System.out.println(
                    "Cannot validate courses: Student Manager is unavailable.");
            courseList = new ArrayList<>();
            return;
        }

        ArrayList<Course> validCourses = new ArrayList<>();

        for (Course course : courseList) {

            if (studentMng.studentExists(course.getStudentId())) {

                validCourses.add(course);

            } else {

                System.out.println(
                        "Skipping course " + course.getCourseId()
                        + ": Student ID does not exist: "
                        + course.getStudentId());
            }
        }

        courseList = validCourses;

        System.out.println(
                "Courses linked to existing students: "
                + courseList.size());
    }

    // 15. Save courses to a text file
    public boolean saveData(String fileName) {

        return courseTextFile.save(
                fileName,
                courseList);
    }

    // 16. Get total number of courses
    public int size() {
        return courseList.size();
    }

    // 17. Check whether course list is empty
    public boolean isEmpty() {
        return courseList.isEmpty();
    }
}
