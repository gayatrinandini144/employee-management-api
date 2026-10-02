/**
 * Beginner Task 2: OOP concepts demo — class, object, constructors, methods,
 * encapsulation, and inheritance.
 */
public class OOPDemo {
    public static void main(String[] args) {
        Vehicle car = new Car("Toyota", "Corolla", 4);
        Vehicle bike = new Motorbike("Yamaha", "MT-15");

        car.displayInfo();
        car.start();

        bike.displayInfo();
        bike.start();
    }
}

// Base class demonstrating encapsulation via private fields + getters/setters
class Vehicle {
    private String brand;
    private String model;

    // Default constructor
    public Vehicle() {
        this("Unknown", "Unknown");
    }

    // Parameterized constructor
    public Vehicle(String brand, String model) {
        this.brand = brand;
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void start() {
        System.out.println(brand + " " + model + " is starting.");
    }

    public void displayInfo() {
        System.out.println("Vehicle -> Brand: " + brand + ", Model: " + model);
    }
}

// Inheritance: Car extends Vehicle and adds its own field + overrides methods
class Car extends Vehicle {
    private int numberOfDoors;

    public Car(String brand, String model, int numberOfDoors) {
        super(brand, model);
        this.numberOfDoors = numberOfDoors;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Car -> Doors: " + numberOfDoors);
    }

    @Override
    public void start() {
        System.out.println(getBrand() + " " + getModel() + " (car) engine started with a key/button.");
    }
}

// Another subclass showing polymorphism
class Motorbike extends Vehicle {
    public Motorbike(String brand, String model) {
        super(brand, model);
    }

    @Override
    public void start() {
        System.out.println(getBrand() + " " + getModel() + " (motorbike) engine started with a kick/self-start.");
    }
}
