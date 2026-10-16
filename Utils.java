import java.util.Scanner;

public class Utils {

    public static String readNonEmptyString(Scanner scanner, String prompt) {

        boolean isValid = false;
        String input = "";
        while(!isValid) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if(input.isBlank()) {
                System.out.println("Input cannot be empty!");
                continue;
            }
            isValid = true;
        }
        return input;

    }

    public static String readNonEmptyString(Scanner scanner, String prompt, int maxLength) {

        boolean isValid = false;
        String input = "";
        while(!isValid) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if(input.isBlank()) {
                System.out.println("Input cannot be empty!");
                continue;
            }
            if(input.length() > maxLength) {
                System.out.println("Input cannot be longer than " + maxLength + " characters!");
                continue;
            }
            isValid = true;
        }
        return input;

    }

    public static String readValidString(Scanner scanner, String prompt, String format, String errorMessage) {

        boolean isValid = false;
        String input = "";
        while(!isValid) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if(input.isBlank()) {
                System.out.println("Input cannot be empty!");
                continue;
            }
            if(!input.matches(format)) {
                System.out.println(errorMessage);
                continue;
            }
            isValid = true;
        }
        return input;

    }

    public static int readValidInt(Scanner scanner, String prompt, int min, int max, String errorMessage) {

        boolean isValid = false;
        String input;
        int value = -1;
        while(!isValid) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            try {
                value = Integer.parseInt(input);
                if(value >= min && value <= max) {
                    isValid = true;
                } else {
                    System.out.println(errorMessage);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number!");
            }
            
        }
        return value;

    }

    public static float readValidFloat(Scanner scanner, String prompt, float min, float max, String errorMessage) {

        boolean isValid = false;
        String input;
        float value = -1.0f;
        while(!isValid) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            try {
                value = Float.parseFloat(input);
                if(value >= min && value <= max) {
                    isValid = true;
                } else {
                    System.out.println(errorMessage);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number!");
            }
            
        }
        return value;

    }

    public static void printHeader(String label) {
        System.out.println("========== " + label + " ==========");
    }

    public static String tableFormat() {
        // tracking number, type, receiver, chargeable weight, service, delivery fee, and status
        return "%-25s%-15s%-30s%-25s%-15s%-20s%-10s%n";
    }

}