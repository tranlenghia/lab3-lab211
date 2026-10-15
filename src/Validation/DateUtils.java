
package validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

public class DateUtils {

    private final DateTimeFormatter dayFirst =
            DateTimeFormatter.ofPattern("d/M/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private final DateTimeFormatter monthFirst =
            DateTimeFormatter.ofPattern("M/d/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private final DateTimeFormatter dayMonthName =
            DateTimeFormatter.ofPattern(
                    "d MMM, uuuu", Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);

    private final DateTimeFormatter monthNameDay =
            DateTimeFormatter.ofPattern(
                    "MMM d, uuuu", Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.STRICT);

    private final DateTimeFormatter outputFormat =
            DateTimeFormatter.ofPattern("dd/MM/uuuu");

    // Constructor
    public DateUtils() {
    }

    // Parse supported date formats
    public LocalDate parseDate(String value) {

        if (value == null || value.trim().isEmpty()) {
            throw new DateTimeParseException(
                    "Date cannot be empty.",
                    String.valueOf(value), 0);
        }

        String text = value.trim();

        text = text.replaceAll(
                "(?i)(\\d{1,2})(st|nd|rd|th)", "$1");

        try {
            return LocalDate.parse(text, dayFirst);
        } catch (DateTimeParseException e) {
            // Try another format
        }

        try {
            return LocalDate.parse(text, monthFirst);
        } catch (DateTimeParseException e) {
            // Try another format
        }

        try {
            return LocalDate.parse(text, dayMonthName);
        } catch (DateTimeParseException e) {
            // Try another format
        }

        try {
            return LocalDate.parse(text, monthNameDay);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException(
                    "Unsupported date format.",
                    text, 0, e);
        }
    }

    // Format date as dd/MM/yyyy
    public String formatDate(LocalDate date) {

        if (date == null) {
            return "";
        }

        return date.format(outputFormat);
    }
}
