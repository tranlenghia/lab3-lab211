package fileio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import model.Student;
import validation.StudentValidation;
import java.util.Locale;

public class StudentTextFile
        implements IFileHelper<Student> {

    private final StudentValidation studentValidation
            = new StudentValidation();

    private boolean studentIdExistsIgnoreCase(
            ArrayList<Student> students,
            String studentId) {

        for (Student student : students) {

            if (student.getStudentId() != null
                    && student.getStudentId()
                            .equalsIgnoreCase(studentId)) {
                return true;
            }
        }

        return false;
    }

    // 1. Read students from a text file
    @Override
    public ArrayList<Student> load(String fileName) {

        ArrayList<Student> students = new ArrayList<>();

        try ( BufferedReader reader = Files.newBufferedReader(
                Paths.get(fileName),
                StandardCharsets.UTF_8)) {

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {

                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(",", -1);

                if (fields.length != 4) {

                    System.out.println(
                            "Invalid student data at line "
                            + lineNumber + ".");

                    continue;
                }

                try {

                    // Read data from the four columns
                    String studentId
                            = fields[0].trim().toUpperCase(Locale.ROOT);

                    String fullName = fields[1].trim();
                    String major = fields[2].trim();

                    double gpa
                            = Double.parseDouble(fields[3].trim());

                    // Validate all student information
                    if (!studentValidation.isValidStudentId(studentId)
                            || !studentValidation.isValidFullName(fullName)
                            || !studentValidation.isValidMajor(major)
                            || !Double.isFinite(gpa)
                            || !studentValidation.isValidGpa(gpa)) {

                        System.out.println(
                                "Invalid student data at line "
                                + lineNumber + ".");

                        continue;
                    }

                    // Check duplicate Student ID
                    if (studentIdExistsIgnoreCase(students, studentId)) {

                        System.out.println(
                                "Duplicate Student ID at line "
                                + lineNumber + ": " + studentId);

                        continue;
                    }

                    // Add a valid student
                    Student student = new Student(
                            studentId,
                            fullName,
                            major,
                            gpa);

                    students.add(student);

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Invalid GPA at line "
                            + lineNumber + ".");

                
            }
        }

        System.out.println(
                "Loaded " + students.size()
                + " students from " + fileName + ".");

    }
    catch (IOException e

    
        ) {

            System.out.println(
                "Cannot read file: " + fileName);
    }

    return students ;
}

// 2. Save students to a text file
@Override
public boolean save(
        String fileName,
        ArrayList<Student> data) {

    if (data == null) {

        System.out.println("Student data is null.");
        return false;
    }

    // Validate all students before opening the file
    ArrayList<String> studentIds = new ArrayList<>();

    for (int i = 0; i < data.size(); i++) {

        Student student = data.get(i);

        if (student == null) {

            System.out.println(
                    "Invalid student at position " + (i + 1) + ".");

            return false;
        }

        String studentId = student.getStudentId();
        String fullName = student.getFullName();
        String major = student.getMajor();
        double gpa = student.getGpa();

        // Check Student ID, Full Name, Major and GPA
        if (studentId == null
                || !studentValidation.isValidStudentId(studentId)
                || fullName == null
                || !studentValidation.isValidFullName(fullName)
                || major == null
                || !studentValidation.isValidMajor(major)
                || !Double.isFinite(gpa)
                || !studentValidation.isValidGpa(gpa)) {

            System.out.println(
                    "Invalid student data at position "
                    + (i + 1) + ".");

            return false;
        }

        // Check duplicate Student ID
        String normalizedId =
                studentId.trim().toUpperCase(Locale.ROOT);

        if (studentIds.contains(normalizedId)) {

            System.out.println(
                    "Duplicate Student ID: " + studentId);

            return false;
        }

        studentIds.add(normalizedId);
    }

    // Write only after the entire list has passed validation
    try (BufferedWriter writer = Files.newBufferedWriter(
            Paths.get(fileName),
            StandardCharsets.UTF_8)) {

        for (Student student : data) {

            writer.write(
                    student.getStudentId().trim()
                    .toUpperCase(Locale.ROOT)
                    + ", "
                    + student.getFullName().trim()
                    + ", "
                    + student.getMajor().trim()
                    + ", "
                    + Double.toString(student.getGpa()));

            writer.newLine();
        }

        System.out.println(
                "Student data saved successfully.");

        return true;

    } catch (IOException e) {

        System.out.println(
                "Cannot save file: " + fileName);

        return false;
    }
}}
