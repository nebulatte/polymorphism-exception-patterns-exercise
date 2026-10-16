import java.util.Scanner;

public enum ParcelType {
    
    DOCUMENT("Document"),
    BOX("Box"),
    FRAGILE("Fragile");

    private final String description;

    private ParcelType(String description) {
        this.description = description;
    }

    public String getParcelDescription() {
        return description;
    }

    public static ParcelType fromString(Scanner scanner) {
        printChoices();
        String input;
        boolean isValid = false;
        while(!isValid) {
            input = Utils.readNonEmptyString(scanner, "Enter your choice: ");
            if(input.equalsIgnoreCase("Document") || input.equals("1")) {
                return DOCUMENT;
            }
            if(input.equalsIgnoreCase("Box") || input.equals("2")) {
                return BOX;
            }
            if(input.equalsIgnoreCase("FRAGILE") || input.equals("3")) {
                return FRAGILE;
            }
            System.out.printf("\"%s\" is not a valid option!%n", input);
        }
        return null;
    }

    private static void printChoices() {
        System.out.println("Parcel Types:");
        System.out.println("1 - Document");
        System.out.println("2 - Box");
        System.out.println("3 - Fragile");
    }
}
