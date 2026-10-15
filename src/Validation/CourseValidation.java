package validation;

import java.time.LocalDate;

public class CourseValidation {

    // 1. Validate Course ID
    public boolean isValidCourseId(String courseId) {

        return courseId != null
                && !courseId.trim().isEmpty();
    }

    // 2. Validate Student ID format
    public boolean isValidStudentId(String studentId) {

        if (studentId == null
                || studentId.trim().isEmpty()) {
            return false;
        }

        return studentId.trim()
                .matches("(?i)^STU\\d{4}$");
    }

    // 3. Validate Course Name
    public boolean isValidCourseName(String courseName) {

        return courseName != null
                && !courseName.trim().isEmpty();
    }

    // 4. Validate Course Duration
    public boolean isValidDuration(int duration) {

        return duration >= 1;
    }

    // 5. Validate Start Date
    public boolean isValidStartDate(LocalDate startDate) {

        return startDate != null
                && startDate.isAfter(LocalDate.now());
    }
}