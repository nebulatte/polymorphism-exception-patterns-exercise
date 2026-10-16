public abstract class Parcel {
    
    private final String trackingNumber;
    private final String receiverName;
    private final float weight;
    private Delivery delivery;
    private boolean isDelivered;

    public static final String getTrackingNumberFormat() {
        return "^ABC[0-9]{4}$";
    }
    public static float getMinWeight() {
        return 0.01f; // weight must be > 0
    }

    public Parcel(String trackingNumber, String receiverName, float weight) {
        this.trackingNumber = trackingNumber;
        this.receiverName = receiverName;
        this.weight = weight;
        this.isDelivered = false;
    }

    public abstract ParcelType getParcelType();
    public abstract float getChargeableWeight();
    public abstract float getBaseFee();

    public String getTrackingNumber() {
        return trackingNumber;
    }
    public String getReceiverName() {
        return receiverName;
    }
    public float getWeight() {
        return weight;
    }
    public Delivery getDelivery() {
        return delivery; 
    }
    public float getDeliveryFee() {
        return delivery.getDeliveryFee(getBaseFee());
    }
    public boolean getIsDelivered() {
        return isDelivered;
    }
    public String getIsDeliveredDescription() {
        if(isDelivered) {
            return "Delivered";
        } else {
            return "Pending";
        }
    }

    // Throws if the chosen service is not allowed; the parcel keeps its old service in that case
    public void setDelivery(Delivery delivery) throws InvalidDeliveryException {
        if(!delivery.isAllowed(getChargeableWeight())) {
            throw new InvalidDeliveryException("Same Day delivery is only for parcels up to 10 kg!");
        }
        this.delivery = delivery;
    }
    public void setIsDelivered() {
        this.isDelivered = true;
    }

}

class Document extends Parcel {

    private static final float MAX_WEIGHT = 0.5f; // in kg
    private static final int MAX_PAGES = 100;

    private final int numOfPages;
    private final float chargeableWeight;
    private final float baseFee = 50.0f; // in peso

    public Document(String trackingNumber, String receiverName, float weight, int numOfPages) {
        super(trackingNumber, receiverName, weight);
        this.numOfPages = numOfPages;
        this.chargeableWeight = weight;
    }

    @Override
    public ParcelType getParcelType() {
        return ParcelType.DOCUMENT;
    }
    @Override
    public float getChargeableWeight() {
        return chargeableWeight;
    }
    @Override
    public float getBaseFee() {
        return baseFee;
    }

    public static float getMaxWeight() {
        return MAX_WEIGHT;
    }
    public static int getMaxPages() {
        return MAX_PAGES;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

}

class Box extends Parcel {

    private static final float MAX_WEIGHT = 50.0f; // in kg

    private final int length;
    private final int width;
    private final int height;
    private final float volumetricWeight;
    private final float chargeableWeight;
    private final float baseFee; // in peso

    public Box(String trackingNumber, String receiverName, float weight, 
        int length, int width, int height) {
        super(trackingNumber, receiverName, weight);
        this.length = length;
        this.width = width;
        this.height = height;
        this.volumetricWeight = findVolumetricWeight();
        this.chargeableWeight = findChargeableWeight(weight, volumetricWeight);
        this.baseFee = findBaseFee(chargeableWeight);
    }

    @Override
    public ParcelType getParcelType() {
        return ParcelType.BOX;
    }
    @Override
    public float getChargeableWeight() {
        return chargeableWeight;
    }
    @Override
    public float getBaseFee() {
        return baseFee;
    }
    
    public static float getMaxWeight() {
        return MAX_WEIGHT;
    }
    public static int getMinDimension() {
        return 1;
    }
    public static int getMaxDimension() {
        return 1000; // the spec has no limit; this is just a sanity cap
    }

    // Calculation Methods
    private float findVolumetricWeight() {
        return ((float) length * width * height) / 3500f;
    }
    // bigger of actual and volumetric weight, rounded UP to the next whole kg
    private float findChargeableWeight(float weight, float volumetricWeight) {
        return (float) Math.ceil(Math.max(weight, volumetricWeight));
    }
    private float findBaseFee(float chargeableWeight) {
        return 40 * chargeableWeight;
    }

}

class Fragile extends Box {

    private static final float MAX_WEIGHT = 30.0f; // in kg

    private final String handlingNote;

    public Fragile(String trackingNumber, String receiverName, float weight,
        int length, int width, int height,
        String handlingNote) {
        super(trackingNumber, receiverName, weight, length, width, height);
        this.handlingNote = handlingNote;
    }

    @Override
    public ParcelType getParcelType() {
        return ParcelType.FRAGILE;
    }
    // BOX base fee + Php 100 handling fee
    @Override
    public float getBaseFee() {
        return super.getBaseFee() + 100;
    }
    
    public static float getMaxWeight() {
        return MAX_WEIGHT;
    }

    public String getHandlingNote() {
        return handlingNote;
    }

}