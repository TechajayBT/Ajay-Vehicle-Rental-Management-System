import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class VehicleRentalSystem {
    private List<Vehicle> vehicles;
    private List<Customer> customers;
    private List<Rental> rentals;
    private Map<String, Vehicle> vehicleMap;
    private Map<String, Customer> customerMap;

    // File paths for saving and loading data
    private static final String VEHICLES_FILE = "vehicles.txt";
    private static final String CUSTOMERS_FILE = "customers.txt";
    private static final String RENTALS_FILE = "rentals.txt";

    public VehicleRentalSystem() {
        this.vehicles = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.rentals = new ArrayList<>();
        this.vehicleMap = new HashMap<>();
        this.customerMap = new HashMap<>();
    }

    // Add a vehicle to the system
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicleMap.put(vehicle.getVehicleID(), vehicle);
        System.out.println("Vehicle added successfully: " + vehicle.getVehicleID() + " - " + vehicle.getBrand() + " " + vehicle.getType());
    }

    // Remove a vehicle from the system
    public void removeVehicle(String vehicleID) {
        Vehicle vehicle = vehicleMap.get(vehicleID);
        if (vehicle != null) {
            vehicles.remove(vehicle);
            vehicleMap.remove(vehicleID);
            System.out.println("Vehicle removed successfully: " + vehicleID);
        } else {
            System.out.println("Vehicle not found with ID: " + vehicleID);
        }
    }

    // Add a customer to the system
    public void addCustomer(Customer customer) {
        customers.add(customer);
        customerMap.put(customer.getCustomerID(), customer);
        System.out.println("Customer added successfully: " + customer.getName());
    }

    // Rent a vehicle
    // Rent a vehicle
    public String rentVehicle(String vehicleID, String customerID, LocalDate rentalDate) throws Exception {
        Vehicle vehicle = vehicleMap.get(vehicleID);
        Customer customer = customerMap.get(customerID);
        if (vehicle == null) {
            throw new Exception("Vehicle not found with ID: " + vehicleID);
        }
        if (customer == null) {
            throw new Exception("Customer not found with ID: " + customerID);
        }
        if (!vehicle.isAvailable()) {
            throw new Exception("Vehicle is not available for rent: " + vehicleID);
        }
        Rental rental = new Rental(UUID.randomUUID().toString(), vehicle, customer, rentalDate);
        rentals.add(rental);
        customer.getRentalHistory().add(rental);
        vehicle.setAvailable(false);
        System.out.println("Vehicle rented successfully: " + vehicleID + " to " + customer.getName());
        return rental.getRentalID();
    }


    // Return a rented vehicle
    public void returnVehicle(String rentalID, LocalDate returnDate) throws Exception {
        Rental rental = null;
        for (Rental r : rentals) {
            if (r.getRentalID().equals(rentalID)) {
                rental = r;
                break;
            }
        }
        if (rental == null) {
            System.out.println("Rental not found with ID: " + rentalID);
            return;
        }
        rental.setReturnDate(returnDate);
        rental.getVehicle().setAvailable(true);

        long rentalDays = ChronoUnit.DAYS.between(rental.getRentalDate(), returnDate);
        double totalCost = rentalDays * rental.getVehicle().getRentalRate();

        System.out.println("Vehicle returned successfully: " + rental.getVehicle().getVehicleID());
        System.out.println("Total rental cost: Rs. " + totalCost);
    }

    // View all vehicles in the system
    public void viewAllVehicles() {
        System.out.println("Viewing all vehicles:");
        for (Vehicle vehicle : vehicles) {
            System.out.println("ID: " + vehicle.getVehicleID() + ", Type: " + vehicle.getType() + ", Brand: " + vehicle.getBrand() + ", Rental Rate: " + vehicle.getRentalRate() + ", Available: " + (vehicle.isAvailable() ? "Yes" : "No"));
        }
    }

    // Search for a vehicle by its ID
    public void searchVehicle(String vehicleID) {
        Vehicle vehicle = vehicleMap.get(vehicleID);
        if (vehicle != null) {
            System.out.println("Vehicle found:");
            System.out.println("ID: " + vehicle.getVehicleID() + ", Type: " + vehicle.getType() + ", Brand: " + vehicle.getBrand() + ", Rental Rate: " + vehicle.getRentalRate() + ", Available: " + (vehicle.isAvailable() ? "Yes" : "No"));
        } else {
            System.out.println("Vehicle not found with ID: " + vehicleID);
        }
    }

    // Sort vehicles by rental rate
    public void sortVehiclesByRentalRate() {
        vehicles.sort(Comparator.comparingDouble(Vehicle::getRentalRate));
        System.out.println("Vehicles sorted by rental rate:");
        displayVehicles(vehicles);
    }


    // Sort vehicles by type
    public void sortVehiclesByType() {
        vehicles.sort(Comparator.comparing(Vehicle::getType));
        System.out.println("Vehicles sorted by type:");
        displayVehicles(vehicles);
    }


    // Sort vehicles by availability
    public void sortVehiclesByAvailability() {
        vehicles.sort(Comparator.comparing(Vehicle::isAvailable).reversed());
        System.out.println("Vehicles sorted by availability:");
        displayVehicles(vehicles);
    }


    // Helper method to display a list of vehicles
    public void displayVehicles(List<Vehicle> vehicles) {
        for (Vehicle vehicle : vehicles) {
            System.out.println("ID: " + vehicle.getVehicleID() + ", Type: " + vehicle.getType() + ", Brand: " + vehicle.getBrand() + ", Rental Rate: " + vehicle.getRentalRate() + ", Available: " + (vehicle.isAvailable() ? "Yes" : "No"));
        }
    }

    // Save vehicles to file
    public void saveVehicles() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VEHICLES_FILE))) {
            for (Vehicle vehicle : vehicles) {
                writer.write(vehicle.getVehicleID() + "," + vehicle.getType() + "," + vehicle.getBrand() + "," + vehicle.getRentalRate() + "," + vehicle.isAvailable());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving vehicles: " + e.getMessage());
        }
    }

    // Load vehicles from file
    public void loadVehicles() {
        try (BufferedReader reader = new BufferedReader(new FileReader(VEHICLES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Vehicle vehicle = new Vehicle(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
                vehicle.setAvailable(Boolean.parseBoolean(parts[4]));
                vehicles.add(vehicle);
                vehicleMap.put(vehicle.getVehicleID(), vehicle);
            }
        } catch (IOException e) {
            System.out.println("Error loading vehicles: " + e.getMessage());
        }
    }

    // Save customers to file
    public void saveCustomers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer customer : customers) {
                writer.write(customer.getCustomerID() + "," + customer.getName() + "," + customer.getContactNumber());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

    // Load customers from file
    public void loadCustomers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Customer customer = new Customer(parts[0], parts[1], parts[2]);
                customers.add(customer);
                customerMap.put(customer.getCustomerID(), customer);
            }
        } catch (IOException e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }
    }

    //View all customers
    public void viewAllCustomers() {
        System.out.println("Viewing all customers:");
        for (Customer customer : customers) {
            System.out.println("ID: " + customer.getCustomerID() + ", Name: " + customer.getName() + ", Contact: " + customer.getContactNumber());
        }
    }

    // Remove a customer from the system
    public void deleteCustomer(String customerID) {
        Customer customer = customerMap.get(customerID);
        if (customer != null) {
            customers.remove(customer);
            customerMap.remove(customerID);
            System.out.println("Customer removed successfully: " + customer.getName());
        } else {
            System.out.println("Customer not found with ID: " + customerID);
        }
    }


    // Save rentals to file
    public void saveRentals() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RENTALS_FILE))) {
            for (Rental rental : rentals) {
                writer.write(rental.getRentalID() + "," + rental.getVehicle().getVehicleID() + "," + rental.getCustomer().getCustomerID() + "," + rental.getRentalDate() + "," + (rental.getReturnDate() != null ? rental.getReturnDate() : ""));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving rentals: " + e.getMessage());
        }
    }

    // Load rentals from file
    public void loadRentals() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RENTALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Vehicle vehicle = vehicleMap.get(parts[1]);
                Customer customer = customerMap.get(parts[2]);
                LocalDate rentalDate = LocalDate.parse(parts[3]);
                LocalDate returnDate = parts.length > 4 && !parts[4].isEmpty() ? LocalDate.parse(parts[4]) : null;
                Rental rental = new Rental(parts[0], vehicle, customer, rentalDate);
                rental.setReturnDate(returnDate);
                rentals.add(rental);
                customer.getRentalHistory().add(rental);
                vehicle.setAvailable(returnDate != null);
            }
        } catch (IOException e) {
            System.out.println("Error loading rentals: " + e.getMessage());
        }
    }

    // Save all data to files
    public void saveAllData() {
        saveVehicles();
        saveCustomers();
        saveRentals();
    }

    // Load all data from files
    public void loadAllData() {
        loadVehicles();
        loadCustomers();
        loadRentals();
    }

    // Display the main menu
    public void displayMenu() {
        System.out.println("\nWelcome to the Vehicle Rental Management System");
        System.out.println("Please choose an option from the menu below:");
        System.out.println("1. Add Vehicle");
        System.out.println("2. Remove Vehicle");
        System.out.println("3. Add Customer");
        System.out.println("4. Rent Vehicle");
        System.out.println("5. Return Vehicle");
        System.out.println("6. View All Vehicles");
        System.out.println("7. Search Vehicle");
        System.out.println("8. Sort Vehicles by Rental Rate");
        System.out.println("9. Sort Vehicles by Type");
        System.out.println("10. Sort Vehicles by Availability");
        System.out.println("11. Save Data");
        System.out.println("12. Load Data");
        System.out.println("13. See all customers");
        System.out.println("14. Delete customer");
        System.out.println("0. Exit");
    }

    // Main method to run the system
    public void run() {
        VehicleRentalSystem system = new VehicleRentalSystem();
        Scanner scanner = new Scanner(System.in);
        system.loadAllData(); // Load data from files at startup
        try {
            while (true) {
                system.displayMenu();
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter vehicle ID: ");
                        String vehicleID = scanner.nextLine();
                        System.out.print("Enter vehicle type: ");
                        String type = scanner.nextLine();
                        System.out.print("Enter vehicle brand: ");
                        String brand = scanner.nextLine();
                        System.out.print("Enter rental rate per day: Rs. ");
                        double rentalRate = scanner.nextDouble();
                        scanner.nextLine(); // Consume newline
                        system.addVehicle(new Vehicle(vehicleID, type, brand, rentalRate));
                        break;
                    case 2:
                        System.out.print("Enter vehicle ID to remove: ");
                        vehicleID = scanner.nextLine();
                        system.removeVehicle(vehicleID);
                        break;
                    case 3:
                        System.out.print("Enter customer ID: ");
                        String customerID = scanner.nextLine();
                        System.out.print("Enter customer name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter contact number: ");
                        String contactNumber = scanner.nextLine();
                        system.addCustomer(new Customer(customerID, name, contactNumber));
                        break;
                    case 4:
                        System.out.print("Enter vehicle ID to rent: ");
                        vehicleID = scanner.nextLine();
                        System.out.print("Enter customer ID: ");
                        customerID = scanner.nextLine();
                        System.out.print("Enter rental date (YYYY-MM-DD): ");
                        LocalDate rentalDate = LocalDate.parse(scanner.nextLine());
                        try {
                            String rentalID = system.rentVehicle(vehicleID, customerID, rentalDate);
                            System.out.println("Rental ID: " + rentalID);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 5:
                        System.out.print("Enter rental ID to return: ");
                        String rentalID = scanner.nextLine();
                        System.out.print("Enter return date (YYYY-MM-DD): ");
                        LocalDate returnDate = LocalDate.parse(scanner.nextLine());
                        try {
                            system.returnVehicle(rentalID, returnDate);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 6:
                        system.viewAllVehicles();
                        break;
                    case 7:
                        System.out.print("Enter vehicle ID to search: ");
                        vehicleID = scanner.nextLine();
                        system.searchVehicle(vehicleID);
                        break;
                    case 8:
                        system.sortVehiclesByRentalRate();
                        break;
                    case 9:
                        system.sortVehiclesByType();
                        break;
                    case 10:
                        system.sortVehiclesByAvailability();
                        break;
                    case 11:
                        system.saveAllData();
                        System.out.println("Data saved successfully.");
                        break;
                    case 12:
                        system.loadAllData();
                        System.out.println("Data loaded successfully.");
                        break;
                    case 13:
                        system.viewAllCustomers();
                        break;
                    case 14:
                        System.out.print("Enter customer ID to delete: ");
                        customerID = scanner.nextLine();
                        system.deleteCustomer(customerID);
                        break;
                    case 0:
                        system.saveAllData(); // Save data before exiting
                        System.out.println("Thank you for using the Vehicle Rental Management System. Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
