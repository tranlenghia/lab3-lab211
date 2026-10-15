package model;

import java.util.Objects;

public class Student {

    // 1. Attributes
    private String studentId;
    private String fullName;
    private String major;
    private double gpa;

    // 2. Default constructor
    public Student() {
    }

    // 3. Constructor with parameters
    public Student(String studentId, String fullName,
                   String major, double gpa) {

        this.studentId = studentId;
        this.fullName = fullName;
        this.major = major;
        this.gpa = gpa;
    }

    // 4. Getters and setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    // 5. Display student information
    @Override
    public String toString() {
        return String.format(
                "%-10s | %-25s | %-28s | %.2f",
                studentId,
                fullName,
                major,
                gpa
        );
    }

    // 6. Compare students by student ID
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Student student = (Student) obj;

        return Objects.equals(
                studentId,
                student.studentId
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }
}
