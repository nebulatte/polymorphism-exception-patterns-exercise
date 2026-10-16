import java.util.ArrayList;

public class Warehouse {
    
    private static Warehouse warehouse;
    private final ArrayList<Parcel> parcels;

    private Warehouse () {
        parcels = new ArrayList<>();
    }

    public static Warehouse getWarehouse() {
        if(warehouse == null) {
            warehouse = new Warehouse();
        }
        return warehouse;
    }

    public boolean isEmpty() {
        return parcels.isEmpty();
    }

    public void addParcel(Parcel parcel) {
        parcels.add(parcel);
    }

    public Parcel findByTrackingNumber(String trackingNumber) {
        Parcel currentParcel;
        for(int i = 0; i < parcels.size(); i++) {
            currentParcel = parcels.get(i);
            if(currentParcel.getTrackingNumber().equals(trackingNumber)) {
                return currentParcel;
            }
        }
        return null;
    }

    public boolean trackingNumberExists(String trackingNumber) {
        return findByTrackingNumber(trackingNumber) != null;
    }

    public ArrayList<Parcel> getAllParcels() {
        return new ArrayList<>(parcels);
    }

}