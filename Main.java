import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        
        try (Scanner scanner = new Scanner(System.in)) {

            Warehouse warehouse = Warehouse.getWarehouse();

            String choice;
            boolean running = true;
            
            while(running) {

                printMenu();
                System.out.print("Enter your choice: ");
                choice = scanner.nextLine().trim();
                System.out.println();
                
                switch (choice) {
                    case "1" -> addParcel(scanner, warehouse);
                    case "2" -> viewAllParcels(warehouse);
                    case "3" -> changeDeliveryService(scanner, warehouse);
                    case "4" -> markAsDelivered(scanner, warehouse);
                    case "5" -> {
                                System.out.print("Closing the program...");
                                running = false;
                                }
                    default -> System.out.println("\"" + choice + "\" is not a valid option!");
                }

                System.out.println();

            }
            
        }
    }

    private static void printMenu() {
        System.out.println("MAIN MENU:");
        System.out.println("1 - Add Parcel");
        System.out.println("2 - View All Parcel");
        System.out.println("3 - Change Delivery Service");
        System.out.println("4 - Mark as Delivered");
        System.out.println("5 - Exit");
    }

    // =========================================================================================================================

    private static String readUniqueTrackingNumber(Scanner scanner, Warehouse warehouse) {
        while(true) {
            String trackingNumber = Utils.readValidString(scanner, "Enter the tracking number: ", Parcel.getTrackingNumberFormat(), 
                "Invalid tracking number. Follow the following format \"ABC1234\".");
            if(!warehouse.trackingNumberExists(trackingNumber)) {
                return trackingNumber;
            }
            System.out.println("Tracking number already exists!");
        }
    }

    private static float readWeight(Scanner scanner, float maxWeight) {
        return Utils.readValidFloat(scanner, "Enter the weight (kg): ", Parcel.getMinWeight(), maxWeight, 
            String.format("Invalid input. Enter a valid weight [%.2f-%.2f].", Parcel.getMinWeight(), maxWeight));
    }

    private static int readDimension(Scanner scanner, String label) {
        return Utils.readValidInt(scanner, "Enter the box " + label + " (cm): ", Box.getMinDimension(), Box.getMaxDimension(), 
            String.format("Invalid input. Enter a valid whole number [%d-%d].", Box.getMinDimension(), Box.getMaxDimension()));
    }

    private static Parcel createDocument(Scanner scanner, String trackingNumber, String receiverName) {
        float weight = readWeight(scanner, Document.getMaxWeight());
        int numOfPages = Utils.readValidInt(scanner, "Enter the number of pages: ",
            1, Document.getMaxPages(), "Invalid input. Enter a valid number [1-" + Document.getMaxPages() + "].");
        return new Document(trackingNumber, receiverName, weight, numOfPages);
    }

    private static Parcel createBox(Scanner scanner, String trackingNumber, String receiverName) {
        float weight = readWeight(scanner, Box.getMaxWeight());
        int length = readDimension(scanner, "length");
        int width = readDimension(scanner, "width");
        int height = readDimension(scanner, "height");
        return new Box(trackingNumber, receiverName, weight, length, width, height);
    }

    private static Parcel createFragile(Scanner scanner, String trackingNumber, String receiverName) {
        float weight = readWeight(scanner, Fragile.getMaxWeight());
        int length = readDimension(scanner, "length");
        int width = readDimension(scanner, "width");
        int height = readDimension(scanner, "height");
        String handlingNote = Utils.readNonEmptyString(scanner, "Enter the handling note: ", 100);
        return new Fragile(trackingNumber, receiverName, weight, length, width, height, handlingNote);
    }

    private static void addParcel(Scanner scanner, Warehouse warehouse) {
        ParcelType chosenType = ParcelType.fromString(scanner);
        String trackingNumber = readUniqueTrackingNumber(scanner, warehouse);
        String receiverName = Utils.readNonEmptyString(scanner, "Enter the receiver's name: ", 30);

        Parcel parcel = switch(chosenType) {
            case DOCUMENT -> createDocument(scanner, trackingNumber, receiverName);
            case BOX -> createBox(scanner, trackingNumber, receiverName);
            case FRAGILE -> createFragile(scanner, trackingNumber, receiverName);
        };

        // Keep asking until the chosen service is allowed for this parcel
        while(true) {
            Delivery delivery = Delivery.fromString(scanner);
            try {
                parcel.setDelivery(delivery);
                break;
            } catch (InvalidDeliveryException e) {
                System.out.println(e.getMessage());
            }
        }

        warehouse.addParcel(parcel);
        System.out.printf("Parcel added successfully! Delivery fee: Php %.2f%n", parcel.getDeliveryFee());
    }

    // =========================================================================================================================

    private static void viewAllParcels(Warehouse warehouse) {
        Utils.printHeader("VIEWING ALL PARCELS");

        if(warehouse.isEmpty()) {
            System.out.println("There is currrently nothing to display.");
            return;
        }

        System.out.printf(Utils.tableFormat(), "Tracking Number", "Type", "Receiver", 
            "Chargeable Weight", "Service", "Delivery Fee", "Status");

        ArrayList<Parcel> parcels = warehouse.getAllParcels();

        for(Parcel currentParcel : parcels) {
            System.out.printf(Utils.tableFormat(), currentParcel.getTrackingNumber(), currentParcel.getParcelType().getParcelDescription(),
                currentParcel.getReceiverName(), String.format("%.2f kg", currentParcel.getChargeableWeight()), 
                currentParcel.getDelivery().getDescription(), String.format("Php %.2f", currentParcel.getDeliveryFee()), 
                currentParcel.getIsDeliveredDescription());
        }
        
    }

    // =========================================================================================================================

    private static void changeDeliveryService(Scanner scanner, Warehouse warehouse) {
        Utils.printHeader("CHANGING DELIVERY SERVICE");
        
        String trackingNumber = Utils.readValidString(scanner, "Enter the tracking number: ", Parcel.getTrackingNumberFormat(), 
            "Invalid tracking number. Follow the following format \"ABC1234\".");
        Parcel parcel = warehouse.findByTrackingNumber(trackingNumber);

        if(parcel == null) {
            System.out.println("Parcel not found!");
            return;
        }
        if(parcel.getIsDelivered()) {
            System.out.println("Parcel already delivered!");
            return;
        }

        Delivery newDelivery = Delivery.fromString(scanner);
        try {
            parcel.setDelivery(newDelivery);
            System.out.printf("Delivery service changed! New fee: Php %.2f%n", parcel.getDeliveryFee());
        } catch (InvalidDeliveryException e) {
            // the parcel keeps its old service
            System.out.println(e.getMessage());
        }
    }

    // =========================================================================================================================

    private static void markAsDelivered(Scanner scanner, Warehouse warehouse) {
        Utils.printHeader("MARKING AS DELIVERED");
        
        String trackingNumber = Utils.readValidString(scanner, "Enter the tracking number: ", Parcel.getTrackingNumberFormat(), 
            "Invalid tracking number. Follow the following format \"ABC1234\".");
        Parcel parcel = warehouse.findByTrackingNumber(trackingNumber);

        if(parcel == null) {
            System.out.println("Parcel not found!");
            return;
        }
        if(parcel.getIsDelivered()) {
            System.out.println("Parcel was already delivered.");
            return;
        }

        parcel.setIsDelivered();
        System.out.println("Parcel delivered successfully!");
    }
}