
package business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import model.Course;
import model.Student;
import fileio.StudentTextFile;
import validation.StudentValidation;

public class StudentMng {

    private ArrayList<Student> studentList;
    private StudentValidation studentValidation;
    private StudentTextFile studentTextFile;

    // 1. Constructor
    public StudentMng() {
        studentList = new ArrayList<>();
        studentValidation = new StudentValidation();
        studentTextFile = new StudentTextFile();
    }

    // 2. Get and set student list
    public ArrayList<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(ArrayList<Student> studentList) {
        if (studentList == null) {
            this.studentList = new ArrayList<>();
        } else {
            this.studentList = studentList;
        }
    }

    // 3. Find student by ID
    public Student findStudentById(String studentId) {

        if (studentId == null || studentId.trim().isEmpty()) {
            return null;
        }

        for (Student student : studentList) {

            if (student.getStudentId()
                    .equalsIgnoreCase(studentId.trim())) {
                return student;
            }
        }

        return null;
    }

    // 4. Check whether a student exists
    public boolean studentExists(String studentId) {
        return findStudentById(studentId) != null;
    }

    // 5. Add a new student
    public boolean addStudent(Student student) {

        if (student == null) {
            System.out.println("Invalid student data.");
            return false;
        }

        if (!studentValidation.isValidStudentId(
                student.getStudentId())) {

            System.out.println("Invalid Student ID.");
            return false;
        }

        if (studentExists(student.getStudentId())) {
            System.out.println("Student ID already exists.");
            return false;
        }

        if (!studentValidation.isValidFullName(
                student.getFullName())) {

            System.out.println(
                    "Full name must contain at least two words.");
            return false;
        }

        if (!studentValidation.isValidMajor(
                student.getMajor())) {

            System.out.println("Major cannot be empty.");
            return false;
        }

        if (!studentValidation.isValidGpa(
                student.getGpa())) {

            System.out.println(
                    "GPA must be between 0.0 and 4.0.");
            return false;
        }

        studentList.add(student);

        System.out.println("Student added successfully.");
        return true;
    }

    // 6. Display all students
    public void displayAllStudents() {

        if (studentList.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println(
                "------------------------------------------------------------------------------------");

        System.out.printf(
                "%-10s | %-25s | %-28s | %s%n",
                "Student ID",
                "Full Name",
                "Major",
                "GPA");

        System.out.println(
                "------------------------------------------------------------------------------------");

        for (Student student : studentList) {
            System.out.println(student);
        }

        System.out.println(
                "------------------------------------------------------------------------------------");

        System.out.println(
                "Total students: " + studentList.size());
    }

    // 7. Update student GPA
    public boolean updateGpa(String studentId, double newGpa) {

        Student student = findStudentById(studentId);

        if (student == null) {
            System.out.println("Student ID does not exist!");
            return false;
        }

        if (!studentValidation.isValidGpa(newGpa)) {
            System.out.println(
                    "GPA must be between 0.0 and 4.0.");
            return false;
        }

        student.setGpa(newGpa);

        System.out.println("Student GPA updated successfully.");
        return true;
    }

    // 8. Find students by major
    public ArrayList<Student> findStudentsByMajor(String major) {

        ArrayList<Student> result = new ArrayList<>();

        if (major == null || major.trim().isEmpty()) {
            return result;
        }

        for (Student student : studentList) {

            if (student.getMajor()
                    .equalsIgnoreCase(major.trim())) {

                result.add(student);
            }
        }

        return result;
    }

    // 9. Display students by major
    public void displayStudentsByMajor(String major) {

        ArrayList<Student> result =
                findStudentsByMajor(major);

        if (result.isEmpty()) {
            System.out.println(
                    "No students found for major: " + major);
            return;
        }

        System.out.println(
                "Students majoring in: " + major);

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

        for (Student student : result) {
            System.out.println(student);
        }

        System.out.println(
        "----------------------------------------------------------------------------");

        System.out.println(
                "Total students: " + result.size());
    }

    // 10. Sort students by GPA in ascending order
    // 10. Sort a copy of students by GPA in ascending order
public ArrayList<Student> sortStudentsByGpa() {

    // Create a copy of the original student list
    ArrayList<Student> sortedList =
            new ArrayList<>(studentList);

    // Sort the copy by GPA from low to high
    Collections.sort(
            sortedList,
            Comparator.comparingDouble(Student::getGpa));

    // Return the sorted copy
    return sortedList;
}

// 11. Display students sorted by GPA
public void displayStudentsSortedByGpa() {

    if (studentList.isEmpty()) {

        System.out.println("No students found.");
        return;
    }

    // Get the sorted copy without changing studentList
    ArrayList<Student> sortedList =
            sortStudentsByGpa();

    System.out.println(
            "================ STUDENTS SORTED BY GPA ================");

    System.out.println(
            "------------------------------------------------------------------------------------");

    System.out.printf(
            "%-10s | %-25s | %-28s | %s%n",
            "Student ID",
            "Full Name",
            "Major",
            "GPA");

    System.out.println(
            "------------------------------------------------------------------------------------");

    // Display the sorted copy
    for (Student student : sortedList) {
        System.out.println(student);
    }

    System.out.println(
            "------------------------------------------------------------------------------------");

    System.out.println(
            "Total students: " + sortedList.size());
}

    // 12. Remove a student by ID
    public boolean removeStudentById(
            String studentId,
            ArrayList<Course> courseList) {

        Student student = findStudentById(studentId);

        if (student == null) {
            System.out.println("Student ID does not exist!");
            return false;
        }

        if (courseList != null) {

            for (Course course : courseList) {

                if (course.getStudentId()
                        .equalsIgnoreCase(studentId.trim())) {

                    System.out.println(
                            "Cannot delete: Student is assigned to Courses.");

                    return false;
                }
            }
        }

        studentList.remove(student);

        System.out.println("Student removed successfully.");
        return true;
    }

    // 13. Load students from a text file
    public void loadData(String fileName) {

        studentList = studentTextFile.load(fileName);

        if (studentList == null) {
            studentList = new ArrayList<>();
        }
    }

    // 14. Save students to a text file
    public boolean saveData(String fileName) {

        return studentTextFile.save(
                fileName,
                studentList);
    }

    // 15. Get total number of students
    public int size() {
        return studentList.size();
    }

    // 16. Check whether the student list is empty
    public boolean isEmpty() {
        return studentList.isEmpty();
    }
}
