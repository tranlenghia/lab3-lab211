package validation;

public class StudentValidation {

    // 1. Validate Student ID
    public boolean isValidStudentId(String studentId) {

        if (studentId == null || studentId.trim().isEmpty()) {
            return false;
        }

        return studentId.trim().matches("(?i)^STU\\d{4}$");
    }

    // 2. Validate Full Name
    public boolean isValidFullName(String fullName) {

        if (fullName == null || fullName.trim().isEmpty()) {
            return false;
        }

        String[] words = fullName.trim().split("\\s+");

        return words.length >= 2;
    }

    // 3. Validate Major
    public boolean isValidMajor(String major) {

        return major != null && !major.trim().isEmpty();
    }

    // 4. Validate GPA
    public boolean isValidGpa(double gpa) {

        return gpa >= 0.0 && gpa <= 4.0;
    }
}