
import java.util.Scanner;

public class SmartHospitalApp {

    static String[] patientNames = new String[100];
    static int[] patientSeverity = new int[100];
    static int patientCount = 0;

    static int[] resourceIDs = {101, 102, 103, 104, 105};

    static Scanner sc = new Scanner(System.in);

    // Add a patient to the priority queue
    static void addPatient() {
        if (patientCount == patientNames.length) {
            System.out.println("Patient queue is full!");
            return;
        }

        System.out.print("Enter patient name: ");
        String name = sc.nextLine();

        System.out.print("Enter severity (1 = Low, 2 = Moderate, 3 = High, 4 = Critical): ");
        int severity = sc.nextInt();
        sc.nextLine();

        if (severity < 1 || severity > 4) {
            System.out.println("Invalid severity! Enter a value from 1 to 4.");
            return;
        }

        int i = patientCount;

        while (i > 0 && patientSeverity[i - 1] < severity) {
            patientNames[i] = patientNames[i - 1];
            patientSeverity[i] = patientSeverity[i - 1];
            i--;
        }

        patientNames[i] = name;
        patientSeverity[i] = severity;
        patientCount++;

        System.out.println("Patient added successfully!");
    }

    // Display patients according to emergency priority
    static void displayPatients() {
        if (patientCount == 0) {
            System.out.println("No patients in the queue.");
            return;
        }

        System.out.println("\n--- Emergency Patient Queue ---");

        for (int i = 0; i < patientCount; i++) {
            System.out.println(
                (i + 1) + ". Name: " + patientNames[i]
                + " | Severity: " + patientSeverity[i]
            );
        }
    }

    // Treat the highest-priority patient
    static void treatPatient() {
        if (patientCount == 0) {
            System.out.println("No patients waiting for treatment.");
            return;
        }

        System.out.println("\nTreating patient: " + patientNames[0]);
        System.out.println("Severity: " + patientSeverity[0]);

        for (int i = 0; i < patientCount - 1; i++) {
            patientNames[i] = patientNames[i + 1];
            patientSeverity[i] = patientSeverity[i + 1];
        }

        patientCount--;
        System.out.println("Patient removed from the queue.");
    }

    // Search for a resource using Binary Search
    static void searchResource() {
        System.out.print("Enter resource ID to search: ");
        int target = sc.nextInt();
        sc.nextLine();

        int low = 0;
        int high = resourceIDs.length - 1;
        int result = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (resourceIDs[mid] == target) {
                result = mid;
                break;
            } else if (resourceIDs[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (result != -1) {
            System.out.println("Resource found!");
            System.out.println("Resource ID: " + resourceIDs[result]);
            System.out.println("Resource: " + getResourceName(resourceIDs[result]));
        } else {
            System.out.println("Resource ID not found.");
        }
    }

    // Get the name of a medical resource
    static String getResourceName(int id) {
        switch (id) {
            case 101:
                return "ICU Bed";
            case 102:
                return "Ventilator";
            case 103:
                return "Oxygen Cylinder";
            case 104:
                return "Heart Monitor";
            case 105:
                return "Emergency Equipment";
            default:
                return "Unknown Resource";
        }
    }

    // Display available medical resources
    static void displayResources() {
        System.out.println("\n--- Medical Resource List ---");

        for (int id : resourceIDs) {
            System.out.println(
                "ID: " + id + " | Resource: " + getResourceName(id)
            );
        }
    }

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n========================================");
            System.out.println(" SMART HOSPITAL EMERGENCY RESOURCE SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Add Emergency Patient");
            System.out.println("2. Display Patient Queue");
            System.out.println("3. Treat Highest-Priority Patient");
            System.out.println("4. Search Medical Resource");
            System.out.println("5. Display Medical Resources");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                sc.nextLine();
                System.out.print("Enter your choice: ");
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addPatient();
                    break;
                case 2:
                    displayPatients();
                    break;
                case 3:
                    treatPatient();
                    break;
                case 4:
                    searchResource();
                    break;
                case 5:
                    displayResources();
                    break;
                case 6:
                    System.out.println("Thank you for using the system!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }

        } while (choice != 6);

        sc.close();
    }
}
