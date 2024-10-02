import java.io.Serializable;

public class Vehicle implements Serializable {
    private String vehicleID;
    private String type;
    private String brand;
    private double rentalRate;
    private boolean available;

    public Vehicle(String vehicleID, String type, String brand, double rentalRate) {
        this.vehicleID = vehicleID;
        this.type = type;
        this.brand = brand;
        this.rentalRate = rentalRate;
        this.available = true;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public double getRentalRate() {
        return rentalRate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return vehicleID + "," + type + "," + brand + "," + rentalRate + "," + available;
    }

    public static Vehicle fromString(String line) {
        String[] parts = line.split(",");
        Vehicle vehicle = new Vehicle(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
        vehicle.setAvailable(Boolean.parseBoolean(parts[4]));
        return vehicle;
    }
}
