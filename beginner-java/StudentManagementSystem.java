import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Beginner Task 3: Console-based Student Management System.
 * Supports add, view all, search by id, update, and delete.
 */
public class StudentManagementSystem {

    private static final List<Student> students = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static int nextId = 1;

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> addStudent();
                case "2" -> viewAllStudents();
                case "3" -> searchStudent();
                case "4" -> updateStudent();
                case "5" -> deleteStudent();
                case "6" -> {
                    running = false;
                    System.out.println("Exiting. Goodbye!");
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n=== Student Management System ===");
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. Search Student by ID");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
    }

    private static void addStudent() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Course: ");
        String course = scanner.nextLine();

        Student student = new Student(nextId++, name, age, course);
        students.add(student);
        System.out.println("Added: " + student);
    }

    private static void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        students.forEach(System.out::println);
    }

    private static void searchStudent() {
        System.out.print("Enter ID to search: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        findById(id).ifPresentOrElse(
                s -> System.out.println("Found: " + s),
                () -> System.out.println("No student with id " + id)
        );
    }

    private static void updateStudent() {
        System.out.print("Enter ID to update: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        findById(id).ifPresentOrElse(s -> {
            System.out.print("New name (" + s.getName() + "): ");
            String name = scanner.nextLine();
            System.out.print("New age (" + s.getAge() + "): ");
            String ageInput = scanner.nextLine().trim();
            System.out.print("New course (" + s.getCourse() + "): ");
            String course = scanner.nextLine();

            if (!name.isBlank()) s.setName(name);
            if (!ageInput.isBlank()) s.setAge(Integer.parseInt(ageInput));
            if (!course.isBlank()) s.setCourse(course);

            System.out.println("Updated: " + s);
        }, () -> System.out.println("No student with id " + id));
    }

    private static void deleteStudent() {
        System.out.print("Enter ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine().trim());
        boolean removed = students.removeIf(s -> s.getId() == id);
        System.out.println(removed ? "Deleted student " + id : "No student with id " + id);
    }

    private static java.util.Optional<Student> findById(int id) {
        return students.stream().filter(s -> s.getId() == id).findFirst();
    }
}

class Student {
    private final int id;
    private String name;
    private int age;
    private String course;

    public Student(int id, String name, int age, String course) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.course = course;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name='" + name + "', age=" + age + ", course='" + course + "'}";
    }
}
