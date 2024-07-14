import java.util.LinkedList;
import java.util.List;

public class Customer {
    private String customerID;
    private String name;
    private String contactNumber;
    private List<Rental> rentalHistory;

    public Customer(String customerID, String name, String contactNumber) {
        this.customerID = customerID;
        this.name = name;
        this.contactNumber = contactNumber;
        this.rentalHistory = new LinkedList<>();
    }

    // Getters and setters
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public List<Rental> getRentalHistory() {
        return rentalHistory;
    }

    public void setRentalHistory(List<Rental> rentalHistory) {
        this.rentalHistory = rentalHistory;
    }
}
