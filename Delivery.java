import java.util.Scanner;

public interface Delivery {
    float getDeliveryFee(float baseFee);
    String getDescription();
    boolean isAllowed(float chargeableWeight);

    // Moved here from DeliveryService: the user picks a strategy object directly
    static Delivery fromString(Scanner scanner) {
        printChoices();
        while(true) {
            String input = Utils.readNonEmptyString(scanner, "Enter your choice: ");
            if(input.equalsIgnoreCase("Standard") || input.equals("1")) {
                return new Standard();
            }
            if(input.equalsIgnoreCase("Express") || input.equals("2")) {
                return new Express();
            }
            if(input.equalsIgnoreCase("Same Day") || input.equals("3")) {
                return new SameDay();
            }
            System.out.printf("\"%s\" is not a valid option!%n", input);
        }
    }

    private static void printChoices() {
        System.out.println("Delivery Services:");
        System.out.println("1 - Standard");
        System.out.println("2 - Express");
        System.out.println("3 - Same Day");
    }
}

class Standard implements Delivery {
    @Override
    public float getDeliveryFee(float baseFee) {
        return baseFee;
    }
    @Override
    public String getDescription() {
        return "Standard";
    }
    @Override
    public boolean isAllowed(float chargeableWeight) {
        return true;
    }
}

class Express implements Delivery {
    @Override
    public float getDeliveryFee(float baseFee) {
        return baseFee * 1.5f;
    }
    @Override
    public String getDescription() {
        return "Express";
    }
    @Override
    public boolean isAllowed(float chargeableWeight) {
        return true;
    }
}

class SameDay implements Delivery {
    @Override
    public float getDeliveryFee(float baseFee) {
        return (baseFee * 2.0f) + 100;
    }
    @Override
    public String getDescription() {
        return "Same Day";
    }
    @Override
    public boolean isAllowed(float chargeableWeight) {
        return chargeableWeight <= 10.0f;
    }
}