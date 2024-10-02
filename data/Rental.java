import java.time.LocalDate;

public class Rental {
    private String rentalID;
    private Vehicle vehicle;
    private Customer customer;
    private LocalDate rentalDate;
    private LocalDate returnDate;

    public Rental(String rentalID, Vehicle vehicle, Customer customer, LocalDate rentalDate) {
        this.rentalID = rentalID;
        this.vehicle = vehicle;
        this.customer = customer;
        this.rentalDate = rentalDate;
        this.returnDate = null;
    }

    // Getters and setters
    public String getRentalID() {
        return rentalID;
    }

    public void setRentalID(String rentalID) {
        this.rentalID = rentalID;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDate getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(LocalDate rentalDate) {
        this.rentalDate = rentalDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
