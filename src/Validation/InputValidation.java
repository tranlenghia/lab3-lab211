
package validation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputValidation {

    private final Scanner scanner;
    private final DateUtils dateUtils;

    // 1. Constructor
    public InputValidation() {
    scanner = new Scanner(System.in);
    dateUtils = new DateUtils();
}

    // 2. Input a string
    public String inputString(String message) {

        System.out.print(message);

        return scanner.nextLine().trim();
    }

    // 3. Input an integer
    public int inputInt(String message) {

        while (true) {

            String input = inputString(message);

            try {
                return Integer.parseInt(input);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter an integer.");
            }
        }
    }

    // 4. Input a double
    public double inputDouble(String message) {

        while (true) {

            String input = inputString(message);

            try {

                double value = Double.parseDouble(input);

                if (Double.isFinite(value)) {
                    return value;
                }

                System.out.println(
                        "Invalid input. Please enter a finite number.");

            } catch (NumberFormatException e) {

                System.out.println(
                        "Invalid input. Please enter a number.");
            }
        }
    }

    // 5. Input a date using DateUtils
    public LocalDate inputDate(String message) {

        while (true) {

            String input = inputString(
                    message
                    + " (dd/MM/yyyy, MM/dd/yyyy, "
                    + "9th Sep, 2026, Sep 9, 2026): ");

            try {

                return dateUtils.parseDate(input);

            } catch (DateTimeParseException e) {

                System.out.println(
                        "Invalid date. Please try another format.");
            }
        }
    }
}
