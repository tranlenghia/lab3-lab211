package fileio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import model.Course;
import validation.DateUtils;

public class CourseTextFile implements IFileHelper<Course> {

    // Create a DateUtils object to process dates
    private final DateUtils dateUtils = new DateUtils();

    // Check duplicate registration by Course ID and Student ID
//    private boolean registrationExists(
//            ArrayList<Course> courses,
//            String courseId,
//            String studentId) {
//
//        for (Course course : courses) {
//
//            if (course.getCourseId().equalsIgnoreCase(courseId)
//                    && course.getStudentId().equalsIgnoreCase(studentId)) {
//
//                return true;
//            }
//        }
//
//        return false;
//    }
    // Check whether Course ID already exists
    private boolean courseIdExists(
            ArrayList<Course> courses,
            String courseId) {

        for (Course course : courses) {
            if (course.getCourseId().equalsIgnoreCase(courseId.trim())) {
                return true;
            }
        }

        return false;
    }

    // 2. Load courses from a text file
    @Override
    public ArrayList<Course> load(String fileName) {

        ArrayList<Course> courses = new ArrayList<>();

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

                // Each valid row must contain 5 fields
                String[] fields = line.split(",", -1);

                if (fields.length != 5) {

                    System.out.println(
                            "Skipping invalid course data at line "
                            + lineNumber
                            + ": expected 5 comma-separated fields.");

                    continue;
                }

                try {

                    String courseId = fields[0].trim();
                    String studentId = fields[1].trim();
                    String courseName = fields[2].trim();

                    int duration = Integer.parseInt(
                            fields[3].trim());

                    // Parse multiple date formats using DateUtils
                    LocalDate startDate
                            = dateUtils.parseDate(fields[4].trim());

                    // Validate required fields
                    if (courseId.isEmpty()
                            || !studentId.matches(
                                    "(?i)^STU\\d{4}$")
                            || courseName.isEmpty()
                            || duration < 1) {

                        System.out.println(
                                "Skipping invalid course at line "
                                + lineNumber + ".");

                        continue;
                    }

                    // Check duplicate registration by the same student
//                    if (registrationExists(courses, courseId, studentId)) {
//
//                        System.out.println(
//                                "Skipping duplicate registration at line "
//                                + lineNumber + ": "
//                                + courseId + " - " + studentId);
//
//                        continue;
//                    }
// Course ID must be unique
                    if (courseIdExists(courses, courseId)) {
                        System.out.println(
                                "Skipping duplicate Course ID at line "
                                + lineNumber + ": " + courseId);

                        continue;
                    }

                    Course course = new Course(
                            courseId,
                            studentId,
                            courseName,
                            duration,
                            startDate);

                    courses.add(course);

                } catch (NumberFormatException
                        | DateTimeParseException e) {

                    System.out.println(
                            "Skipping invalid course data at line "
                            + lineNumber + ".");
                }
            }

            System.out.println(
                    "Loaded " + courses.size()
                    + " courses from " + fileName + ".");

        } catch (IOException e) {

            System.out.println(
                    "Cannot read file: " + fileName);
        }

        return courses;
    }

    // 3. Save courses to a text file
    @Override
    public boolean save(
            String fileName,
            ArrayList<Course> data) {

        if (data == null) {

            System.out.println("Course data is null.");
            return false;
        }

        // Validate before opening the file for writing
        for (Course course : data) {

            if (course == null
                    || course.getCourseId() == null
                    || course.getCourseId().trim().isEmpty()
                    || course.getStudentId() == null
                    || !course.getStudentId().trim()
                            .matches("(?i)^STU\\d{4}$")
                    || course.getCourseName() == null
                    || course.getCourseName().trim().isEmpty()
                    || course.getStartDate() == null
                    || course.getDuration() < 1) {

                System.out.println(
                        "Cannot save invalid course data.");

                return false;
            }
        }

        try ( BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(fileName),
                StandardCharsets.UTF_8)) {

            for (Course course : data) {

                writer.write(
                        course.getCourseId() + ", "
                        + course.getStudentId() + ", "
                        + course.getCourseName() + ", "
                        + course.getDuration() + ", "
                        + dateUtils.formatDate(
                                course.getStartDate()));

                writer.newLine();
            }

            System.out.println(
                    "Course data saved successfully.");

            return true;

        } catch (IOException e) {

            System.out.println(
                    "Cannot save file: " + fileName);

            return false;
        }
    }
}
