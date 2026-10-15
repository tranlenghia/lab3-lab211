package model;

import java.time.LocalDate;
import java.util.Objects;
import java.time.format.DateTimeFormatter;

public class Course {

    // 1. Attributes
    private String courseId;
    private String studentId;
    private String courseName;
    private int duration;
    private LocalDate startDate;
    private final DateTimeFormatter dateFormat =
        DateTimeFormatter.ofPattern("dd/MM/uuuu");

    // 2. Default constructor
    public Course() {
    }

    // 3. Constructor with parameters
    public Course(String courseId, String studentId,
                  String courseName, int duration,
                  LocalDate startDate) {

        this.courseId = courseId;
        this.studentId = studentId;
        this.courseName = courseName;
        this.duration = duration;
        this.startDate = startDate;
    }

    // 4. Getters and setters
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    // 5. Display course information
    @Override
    public String toString() {
        return String.format(
                "| %-10s | %-10s | %-28s | %7d | %-12s |",
                courseId,
                studentId,
                courseName,
                duration,
                startDate == null ? "" : startDate.format(dateFormat)
        );
    }

    // 6. Compare courses by course ID
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Course course = (Course) obj;

        return Objects.equals(
                courseId,
                course.courseId
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId);
    }
}