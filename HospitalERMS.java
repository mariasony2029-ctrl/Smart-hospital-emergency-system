import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Smart Hospital Emergency Resource Management System (ERMS)
 * Console-based Java demonstration using a priority queue concept and binary search.
 * This file is separate from the HTML/CSS dashboard and does not automatically connect to it.
 */
public class HospitalERMS {
    static class Patient {
        String id;
        String name;
        int age;
        int severity;
        int registrationOrder;

        Patient(String id, String name, int age, int severity, int registrationOrder) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.severity = severity;
            this.registrationOrder = registrationOrder;
        }

        String severityName() {
            switch (severity) {
                case 1: return "Critical";
                case 2: return "Urgent";
                default: return "Stable";
            }
        }

        @Override
        public String toString() {
            return String.format("%-10s %-20s %-5d %-10s %d",
                    id, name, age, severityName(), severity);
        }
    }

    private static final List<Patient> patients = new ArrayList<>();
    private static int nextOrder = 1;
    private static int availableICUBeds = 4;
    private static int availableVentilators = 3;

    public static void main(String[] args) {
        seedDemoPatients();
        try (Scanner input = new Scanner(System.in)) {
            int choice;
            do {
                System.out.println("\\n=== SMART HOSPITAL ERMS ===");
                System.out.println("1. Register emergency patient");
                System.out.println("2. Display priority queue");
                System.out.println("3. Search patient by ID (Binary Search)");
                System.out.println("4. Update medical resources");
                System.out.println("5. View resource availability");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");

                while (!input.hasNextInt()) {
                    System.out.print("Please enter a number: ");
                    input.next();
                }
                choice = input.nextInt();
                input.nextLine();

                switch (choice) {
                    case 1: registerPatient(input); break;
                    case 2: displayQueue(); break;
                    case 3: searchPatient(input); break;
                    case 4: updateResources(input); break;
                    case 5: displayResources(); break;
                    case 0: System.out.println("Exiting ERMS. Stay safe."); break;
                    default: System.out.println("Invalid choice. Try again.");
                }
            } while (choice != 0);
        }
    }

    private static void seedDemoPatients() {
        patients.add(new Patient("P101", "Anjali Thomas", 42, 1, nextOrder++));
        patients.add(new Patient("P102", "Rahul Mathew", 29, 2, nextOrder++));
        patients.add(new Patient("P103", "Meera Joseph", 65, 3, nextOrder++));
    }

    private static void registerPatient(Scanner input) {
        System.out.print("Patient ID: ");
        String id = input.nextLine().trim();
        if (findByIdLinear(id) != null) {
            System.out.println("That patient ID already exists.");
            return;
        }

        System.out.print("Patient name: ");
        String name = input.nextLine().trim();
        System.out.print("Age: ");
        int age = readInt(input, 0, 120);
        System.out.println("Severity: 1=Critical, 2=Urgent, 3=Stable");
        System.out.print("Enter severity: ");
        int severity = readInt(input, 1, 3);

        if (id.isEmpty() || name.isEmpty()) {
            System.out.println("Patient ID and name cannot be empty.");
            return;
        }
        patients.add(new Patient(id, name, age, severity, nextOrder++));
        System.out.println("Patient registered successfully.");
    }

    private static int readInt(Scanner input, int min, int max) {
        while (true) {
            while (!input.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                input.next();
            }
            int value = input.nextInt();
            input.nextLine();
            if (value >= min && value <= max) return value;
            System.out.print("Enter a value from " + min + " to " + max + ": ");
        }
    }

    private static void displayQueue() {
        List<Patient> queue = new ArrayList<>(patients);
        queue.sort(Comparator.comparingInt((Patient p) -> p.severity)
                .thenComparingInt(p -> p.registrationOrder));

        System.out.println("\\n--- EMERGENCY QUEUE (highest priority first) ---");
        System.out.printf("%-10s %-20s %-5s %-10s %s%n",
                "Patient ID", "Name", "Age", "Severity", "Priority");
        for (Patient patient : queue) System.out.println(patient);
        if (queue.isEmpty()) System.out.println("No patients registered.");
    }

    private static void searchPatient(Scanner input) {
        System.out.print("Enter patient ID to search: ");
        String target = input.nextLine().trim();

        // Binary search needs the records sorted by ID first.
        List<Patient> sorted = new ArrayList<>(patients);
        sorted.sort(Comparator.comparing(p -> p.id.toLowerCase()));

        int low = 0, high = sorted.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = sorted.get(mid).id.compareToIgnoreCase(target);
            if (comparison == 0) {
                System.out.println("Patient found: " + sorted.get(mid));
                return;
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        System.out.println("No patient found with ID " + target + ".");
    }

    private static Patient findByIdLinear(String id) {
        for (Patient p : patients) {
            if (p.id.equalsIgnoreCase(id)) return p;
        }
        return null;
    }

    private static void updateResources(Scanner input) {
        System.out.print("Available ICU beds (0-12): ");
        availableICUBeds = readInt(input, 0, 12);
        System.out.print("Available ventilators (0-20): ");
        availableVentilators = readInt(input, 0, 20);
        System.out.println("Resource counts updated.");
    }

    private static void displayResources() {
        System.out.println("\\n--- RESOURCE AVAILABILITY ---");
        System.out.println("Available ICU beds: " + availableICUBeds);
        System.out.println("Available ventilators: " + availableVentilators);
    }
}
