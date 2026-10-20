
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SmartHospitalGUI extends JFrame {

    // ---------- COLOURS ----------
    private final Color BLACK = new Color(18, 18, 18);
    private final Color PANEL = new Color(32, 32, 32);
    private final Color GREEN = new Color(0, 170, 90);
    private final Color WHITE = Color.WHITE;

    // ---------- PATIENT PRIORITY QUEUE ----------
    private final String[] patientNames = new String[100];
    private final int[] patientSeverity = new int[100];
    private int patientCount = 0;

    // ---------- MEDICAL RESOURCE DATA ----------
    private final int[] resourceIDs = {101, 102, 103, 104, 105};

    private final String[] resourceNames = {
        "ICU Bed",
        "Ventilator",
        "Oxygen Cylinder",
        "Heart Monitor",
        "Emergency Equipment"
    };

    private final String[] resourceLocations = {
        "ICU Ward",
        "Emergency Ward",
        "Emergency Ward",
        "ICU Ward",
        "Emergency Room"
    };

    private final int[] totalQuantities = {10, 5, 20, 8, 15};
    private final int[] availableQuantities = {3, 2, 0, 4, 9};

    // ---------- TABLE MODELS ----------
    private final DefaultTableModel patientModel =
        new DefaultTableModel(
            new String[]{"Patient Name", "Severity", "Priority"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

    private final DefaultTableModel resourceModel =
        new DefaultTableModel(
            new String[]{
                "Resource ID", "Resource Name", "Location",
                "Total Quantity", "Available Quantity", "Status"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

    private final JTable patientTable = new JTable(patientModel);
    private final JTable resourceTable = new JTable(resourceModel);

    private final JTextField nameField = new JTextField(15);
    private final JTextField resourceIDField = new JTextField(10);

    private final JComboBox<String> severityBox =
        new JComboBox<>(new String[]{
            "1 - Low",
            "2 - Moderate",
            "3 - High",
            "4 - Critical"
        });

    // ---------- CONSTRUCTOR ----------
    public SmartHospitalGUI() {
        setTitle("Smart Hospital Emergency Resource Management System");
        setSize(1100, 650);
        setMinimumSize(new Dimension(850, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BLACK);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(PANEL);
        tabs.setForeground(WHITE);

        tabs.addTab("Emergency Patients", createPatientPanel());
        tabs.addTab("Medical Resources", createResourcePanel());

        add(tabs, BorderLayout.CENTER);

        loadResources();
        styleTable(patientTable);
        styleTable(resourceTable);
    }

    // ---------- GENERAL STYLING ----------
    private void styleTable(JTable table) {
        table.setBackground(PANEL);
        table.setForeground(WHITE);
        table.setGridColor(Color.GRAY);
        table.setSelectionBackground(GREEN);
        table.setSelectionForeground(Color.BLACK);
        table.setRowHeight(28);
        table.getTableHeader().setBackground(GREEN);
        table.getTableHeader().setForeground(Color.BLACK);
        table.setFillsViewportHeight(true);
    }

    private JButton makeButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(GREEN);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(WHITE);
        return label;
    }

    private void stylePanel(JPanel panel) {
        panel.setBackground(BLACK);
        panel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );
    }

    private JScrollPane makeScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(PANEL);
        scrollPane.setBorder(
            BorderFactory.createLineBorder(Color.GRAY)
        );
        return scrollPane;
    }

    // ---------- PATIENT MANAGEMENT ----------
    private JPanel createPatientPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        stylePanel(panel);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBackground(BLACK);

        inputPanel.add(makeLabel("Patient Name:"));
        inputPanel.add(nameField);
        inputPanel.add(makeLabel("Medical Severity:"));
        inputPanel.add(severityBox);

        JButton addButton = makeButton("Add Patient");
        JButton treatButton = makeButton("Treat Highest Priority");
        JButton refreshButton = makeButton("Refresh Queue");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(BLACK);
        buttonPanel.add(addButton);
        buttonPanel.add(treatButton);
        buttonPanel.add(refreshButton);

        JPanel topPanel = new JPanel(new BorderLayout(5, 10));
        topPanel.setBackground(BLACK);
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(makeScrollPane(patientTable), BorderLayout.CENTER);

        addButton.addActionListener(e -> addPatient());
        treatButton.addActionListener(e -> treatPatient());
        refreshButton.addActionListener(e -> refreshPatients());

        return panel;
    }

    // Higher severity patients are placed first.
    private void addPatient() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please enter the patient name."
            );
            return;
        }

        if (patientCount >= patientNames.length) {
            JOptionPane.showMessageDialog(
                this, "The patient queue is full."
            );
            return;
        }

        int severity = severityBox.getSelectedIndex() + 1;
        int i = patientCount;

        // Array-based priority queue insertion
        while (i > 0 && patientSeverity[i - 1] < severity) {
            patientNames[i] = patientNames[i - 1];
            patientSeverity[i] = patientSeverity[i - 1];
            i--;
        }

        patientNames[i] = name;
        patientSeverity[i] = severity;
        patientCount++;

        nameField.setText("");
        refreshPatients();

        JOptionPane.showMessageDialog(
            this, "Patient added successfully!"
        );
    }

    private void refreshPatients() {
        patientModel.setRowCount(0);

        for (int i = 0; i < patientCount; i++) {
            patientModel.addRow(new Object[]{
                patientNames[i],
                severityLabel(patientSeverity[i]),
                patientSeverity[i]
            });
        }
    }

    private void treatPatient() {
        if (patientCount == 0) {
            JOptionPane.showMessageDialog(
                this, "No patients are waiting for treatment."
            );
            return;
        }

        String name = patientNames[0];
        int severity = patientSeverity[0];

        // Remove the highest-priority patient
        for (int i = 0; i < patientCount - 1; i++) {
            patientNames[i] = patientNames[i + 1];
            patientSeverity[i] = patientSeverity[i + 1];
        }

        patientCount--;
        refreshPatients();

        JOptionPane.showMessageDialog(
            this,
            "Patient selected for treatment: " + name
            + "\nSeverity: " + severityLabel(severity)
            + "\nPriority: " + severity
        );
    }

    private String severityLabel(int severity) {
        switch (severity) {
            case 4: return "Critical";
            case 3: return "High";
            case 2: return "Moderate";
            default: return "Low";
        }
    }

    // ---------- MEDICAL RESOURCE MANAGEMENT ----------
    private JPanel createResourcePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        stylePanel(panel);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(BLACK);

        searchPanel.add(makeLabel("Enter Resource ID:"));
        searchPanel.add(resourceIDField);

        JButton searchButton = makeButton("Search Resource");
        JButton showAllButton = makeButton("Show All Resources");
        JButton restockButton = makeButton("Restock Selected Resource");

        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);
        searchPanel.add(restockButton);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(makeScrollPane(resourceTable), BorderLayout.CENTER);

        searchButton.addActionListener(e -> searchResource());
        showAllButton.addActionListener(e -> loadResources());
        restockButton.addActionListener(e -> restockResource());

        return panel;
    }

    private String resourceStatus(int index) {
        if (availableQuantities[index] == 0) {
            return "OUT OF STOCK";
        } else if (availableQuantities[index] < totalQuantities[index] * 0.25) {
            return "LOW STOCK";
        } else {
            return "AVAILABLE";
        }
    }

    // Display all resource details in the table
    private void loadResources() {
        resourceModel.setRowCount(0);

        for (int i = 0; i < resourceIDs.length; i++) {
            resourceModel.addRow(new Object[]{
                resourceIDs[i],
                resourceNames[i],
                resourceLocations[i],
                totalQuantities[i],
                availableQuantities[i],
                resourceStatus(i)
            });
        }
    }

    // Binary Search using sorted resource IDs
    private void searchResource() {
        String input = resourceIDField.getText().trim();

        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please enter a resource ID."
            );
            return;
        }

        int target;

        try {
            target = Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this, "Resource ID must be a number."
            );
            return;
        }

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
            resourceTable.setRowSelectionInterval(result, result);
            resourceTable.scrollRectToVisible(
                resourceTable.getCellRect(result, 0, true)
            );

            String message =
                "RESOURCE DETAILS"
                + "\n\nResource ID: " + resourceIDs[result]
                + "\nName: " + resourceNames[result]
                + "\nLocation: " + resourceLocations[result]
                + "\nTotal Quantity: " + totalQuantities[result]
                + "\nAvailable Quantity: " + availableQuantities[result]
                + "\nStatus: " + resourceStatus(result);

            JOptionPane.showMessageDialog(
                this, message, "Resource Search Result",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Resource ID " + target + " was not found.",
                "Search Result",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // Restock the selected resource with a sample quantity.
    private void restockResource() {
        int row = resourceTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Please search for a resource or select a row first."
            );
            return;
        }

        String input = JOptionPane.showInputDialog(
            this, "Enter quantity to add:"
        );

        if (input == null) {
            return;
        }

        int quantity;

        try {
            quantity = Integer.parseInt(input.trim());

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(
                    this, "Quantity must be greater than zero."
                );
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this, "Please enter a valid whole number."
            );
            return;
        }

        int newQuantity = availableQuantities[row] + quantity;

        if (newQuantity > totalQuantities[row]) {
            JOptionPane.showMessageDialog(
                this,
                "Cannot add that quantity. The total capacity is "
                + totalQuantities[row] + "."
            );
            return;
        }

        availableQuantities[row] = newQuantity;
        loadResources();

        resourceTable.setRowSelectionInterval(row, row);

        JOptionPane.showMessageDialog(
            this,
            "Resource restocked successfully!\n"
            + "Resource: " + resourceNames[row]
            + "\nAvailable Quantity: " + availableQuantities[row]
            + "\nStatus: " + resourceStatus(row)
        );
    }

    // ---------- MAIN METHOD ----------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SmartHospitalGUI app = new SmartHospitalGUI();
            app.setVisible(true);
        });
    }
}
