package com.ui;

import javax.swing.*;
import javax.swing.text.JTextComponent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import com.db.*;
import java.util.Random;

public class PatientManagementUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel patientRegisterPanel, appointmentRegisterPanel, medicalRegisterPanel, dischargeRecordPanel, patientRecordPanel;
	private Container billingPanel;
	private JPanel medicalPanel;
	private Object currentPatientID;
	
	public PatientManagementUI() {
        setTitle("Patient Management System");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize the database tables before attempting to establish a connection
        DBConnect.initializeDatabase();

        // Establish the database connection
        if (!initializeDatabaseConnection()) {
            JOptionPane.showMessageDialog(null, "Failed to connect to the database. The application will exit.");
            System.exit(1); // Exit the application if connection fails
        }

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize the panels for different functionalities
        initializeMainPanel();
        initializePatientRegisterPanel();
        initializeAppointmentRegisterPanel();
        initializeMedicalRegisterPanel();
        initializeMedicalPanel();
        initializeBillingPanel();
        initializeDischargeRecordPanel();
        initializePatientRecordPanel();

        add(mainPanel);
        setVisible(true);
    }

    // Define the method to initialize the database connection
    private boolean initializeDatabaseConnection() {
        try {
            DBConnect.getConnection();
            return true; // Connection successful
        } catch (SQLException ex) {
            return false; // Connection failed
        }
    }

    private void initializeMainPanel() {
        JPanel mainMenuPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        JButton btnPatientRegister = new JButton("Patient Register");
        JButton btnAppointmentRegister = new JButton("Appointment Register");
        JButton btnMedicalRegister = new JButton("Medical Register");
        JButton btnDischargeRecord = new JButton("Discharge Record");
        JButton btnPatientRecord = new JButton("Patient Record");

        mainMenuPanel.add(btnPatientRegister);
        mainMenuPanel.add(btnAppointmentRegister);
        mainMenuPanel.add(btnMedicalRegister);
        mainMenuPanel.add(btnDischargeRecord);
        mainMenuPanel.add(btnPatientRecord);

        btnPatientRegister.addActionListener(e -> cardLayout.show(mainPanel, "PatientRegister"));
        btnAppointmentRegister.addActionListener(e -> cardLayout.show(mainPanel, "AppointmentRegister"));
        btnMedicalRegister.addActionListener(e -> cardLayout.show(mainPanel, "MedicalRegister"));
        btnDischargeRecord.addActionListener(e -> cardLayout.show(mainPanel, "DischargeRecord"));
        btnPatientRecord.addActionListener(e -> cardLayout.show(mainPanel, "PatientRecord"));

        mainPanel.add(mainMenuPanel, "MainMenu");
        cardLayout.show(mainPanel, "MainMenu");
    }

    private void initializePatientRegisterPanel() {
        patientRegisterPanel = new JPanel(new CardLayout());
        CardLayout cardLayout = (CardLayout) patientRegisterPanel.getLayout();
        CardLayout mainCardLayout = (CardLayout) mainPanel.getLayout();

        // Button Panel for New Registration and Login
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
        JButton btnNewRegistration = new JButton("New Registration");
        JButton btnLoginRegister = new JButton("Login/Register");
        JButton btnBackToMain = new JButton("Back");

        btnNewRegistration.addActionListener(e -> cardLayout.show(patientRegisterPanel, "NewRegistration"));
        btnLoginRegister.addActionListener(e -> cardLayout.show(patientRegisterPanel, "LoginRegister"));
        btnBackToMain.addActionListener(e -> mainCardLayout.show(mainPanel, "MainMenu"));

        buttonPanel.add(btnNewRegistration);
        buttonPanel.add(btnLoginRegister);
        buttonPanel.add(btnBackToMain);

        // New Registration Panel
        JPanel newRegistrationPanel = new JPanel(new GridLayout(8, 2));
        JTextField txtNewName = new JTextField();
        JTextField txtNewAge = new JTextField();
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextField txtNewDOB = new JTextField();
        JTextField txtNewContact = new JTextField();
        JTextField txtNewAddress = new JTextField();
        JTextField txtNewProblem = new JTextField();
        JButton btnSubmitNew = new JButton("Submit");
        JButton btnBackNew = new JButton("Back");

        newRegistrationPanel.add(new JLabel("Name:"));
        newRegistrationPanel.add(txtNewName);
        newRegistrationPanel.add(new JLabel("Age:"));
        newRegistrationPanel.add(txtNewAge);
        newRegistrationPanel.add(new JLabel("Gender:"));
        newRegistrationPanel.add(genderCombo);
        newRegistrationPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        newRegistrationPanel.add(txtNewDOB);
        newRegistrationPanel.add(new JLabel("Contact:"));
        newRegistrationPanel.add(txtNewContact);
        newRegistrationPanel.add(new JLabel("Address:"));
        newRegistrationPanel.add(txtNewAddress);
        newRegistrationPanel.add(new JLabel("Problem:"));
        newRegistrationPanel.add(txtNewProblem);
        newRegistrationPanel.add(btnSubmitNew);
        newRegistrationPanel.add(btnBackNew);

        btnSubmitNew.addActionListener(e -> {
            String name = txtNewName.getText();
            String ageText = txtNewAge.getText();
            String dob = txtNewDOB.getText();
            String contact = txtNewContact.getText();
            String address = txtNewAddress.getText();
            String problem = txtNewProblem.getText();

            // Validate inputs
            if (name.isEmpty() || ageText.isEmpty() || dob.isEmpty() || contact.isEmpty() || address.isEmpty() || problem.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields must be filled out.");
                return;
            }

            try {
                int age = Integer.parseInt(ageText);
                try (Connection conn = DBConnect.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(
                             "INSERT INTO patients (patient_id, name, age, gender, dob, contact, address, problem, registration_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())")) {
                    String patientID = generatePatientID(conn); // Unique ID generation
                    stmt.setString(1, patientID);
                    stmt.setString(2, name);
                    stmt.setInt(3, age);
                    stmt.setString(4, (String) genderCombo.getSelectedItem());
                    stmt.setString(5, dob);
                    stmt.setString(6, contact);
                    stmt.setString(7, address);
                    stmt.setString(8, problem);
                    stmt.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Patient registered successfully! Generated Patient ID: " + patientID);
                    mainCardLayout.show(mainPanel, "MainMenu");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error during patient registration: " + ex.getMessage());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number for age.");
            }
        });

        btnBackNew.addActionListener(e -> cardLayout.show(patientRegisterPanel, "ButtonPanel"));

        // Login Panel
        JPanel loginPanel = new JPanel(new GridLayout(6, 2));
        JTextField txtPatientID = new JTextField();
        JTextField txtAge = new JTextField();
        JTextField txtProblem = new JTextField();
        JLabel lblPatientInfo = new JLabel("Patient Info:");
        JButton btnFetch = new JButton("Fetch");
        JButton btnLoginSubmit = new JButton("Submit");
        JButton btnLoginBack = new JButton("Back");

        loginPanel.add(new JLabel("Patient ID:"));
        loginPanel.add(txtPatientID);
        loginPanel.add(btnFetch);
        loginPanel.add(lblPatientInfo);
        loginPanel.add(new JLabel("Age:"));
        loginPanel.add(txtAge);
        loginPanel.add(new JLabel("Problem:"));
        loginPanel.add(txtProblem);
        loginPanel.add(btnLoginSubmit);
        loginPanel.add(btnLoginBack);

        txtAge.setEnabled(false);
        txtProblem.setEnabled(false);

        btnFetch.addActionListener(e -> fetchPatientDetails(txtPatientID, txtAge, txtProblem, lblPatientInfo, cardLayout));
        btnLoginSubmit.addActionListener(e -> updatePatientDetails(txtPatientID, txtAge, txtProblem, cardLayout));
        btnLoginBack.addActionListener(e -> cardLayout.show(patientRegisterPanel, "ButtonPanel"));

        patientRegisterPanel.add(buttonPanel, "ButtonPanel");
        patientRegisterPanel.add(newRegistrationPanel, "NewRegistration");
        patientRegisterPanel.add(loginPanel, "LoginRegister");

        mainPanel.add(patientRegisterPanel, "PatientRegister");
        cardLayout.show(patientRegisterPanel, "ButtonPanel");
    }

    // Fetch patient details based on Patient ID
    private void fetchPatientDetails(JTextField txtPatientID, JTextField txtAge, JTextField txtProblem, JLabel lblPatientInfo, CardLayout cardLayout) {
        String patientID = txtPatientID.getText();
        if (patientID.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a Patient ID.");
            return;
        }

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM patients WHERE patient_id = ?")) {
            stmt.setString(1, patientID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String age = rs.getString("age");
                lblPatientInfo.setText("Patient Name: " + name);
                txtAge.setText(age);
                txtProblem.setEnabled(true);
                txtAge.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(null, "Patient ID not found. Redirecting to New Registration.");
                cardLayout.show(patientRegisterPanel, "NewRegistration");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error fetching patient details: " + ex.getMessage());
        }
    }

    // Update patient details in the database
    private void updatePatientDetails(JTextField txtPatientID, JTextField txtAge, JTextField txtProblem, CardLayout cardLayout) {
        String patientID = txtPatientID.getText();
        String ageText = txtAge.getText();
        String problem = txtProblem.getText();

        if (ageText.isEmpty() || problem.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Age and Problem fields must be filled.");
            return;
        }

        try {
            int age = Integer.parseInt(ageText);

            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement fetchStmt = conn.prepareStatement("SELECT * FROM patients WHERE patient_id = ?")) {
                fetchStmt.setString(1, patientID);
                ResultSet rs = fetchStmt.executeQuery();

                if (rs.next()) {
                    String name = rs.getString("name");
                    String gender = rs.getString("gender");
                    String dob = rs.getString("dob");
                    String contact = rs.getString("contact");
                    String address = rs.getString("address");

                    // Ensure record ID is correctly generated
                    try (PreparedStatement insertStmt = conn.prepareStatement(
                            "INSERT INTO patient_records (record_id, patient_id, name, age, gender, dob, contact, address, problem, registration_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())")) {
                        String newRecordID = generateRecordID(conn);
                        insertStmt.setString(1, newRecordID);
                        insertStmt.setString(2, patientID);
                        insertStmt.setString(3, name);
                        insertStmt.setInt(4, age);
                        insertStmt.setString(5, gender);
                        insertStmt.setString(6, dob);
                        insertStmt.setString(7, contact);
                        insertStmt.setString(8, address);
                        insertStmt.setString(9, problem);
                        insertStmt.executeUpdate();

                        JOptionPane.showMessageDialog(null, "New entry added successfully for Patient ID: " + patientID);
                        cardLayout.show(patientRegisterPanel, "ButtonPanel");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Patient ID not found. Cannot add new record.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error adding new entry: " + ex.getMessage());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number for age.");
        }
    }

    // Method to generate unique Patient ID
    private String generatePatientID(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM patients")) {
            int count = rs.next() ? rs.getInt(1) + 1 : 1; // Increment count for new ID
            return "pid" + String.format("%02d", count); // Format to PID01, PID02, etc.
        }
    }

    // Method to generate unique Record ID
    private String generateRecordID(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM patient_records")) {
            int count = rs.next() ? rs.getInt(1) + 1 : 1; // Increment count for new ID
            return "rid" + String.format("%02d", count); // Format to RID01, RID02, etc.
        }
    }

    private void initializeAppointmentRegisterPanel() {
        appointmentRegisterPanel = new JPanel(new GridLayout(4, 2));
        JTextField txtPatientID = new JTextField();
        JButton btnFetch = new JButton("Fetch");
        JLabel lblPatientInfo = new JLabel("Patient details will appear here");
        JTextField txtProblem = new JTextField();
        JButton btnSubmit = new JButton("Submit");
        JButton btnBack = new JButton("Back");

        appointmentRegisterPanel.add(new JLabel("Patient ID:"));
        appointmentRegisterPanel.add(txtPatientID);
        appointmentRegisterPanel.add(btnFetch);
        appointmentRegisterPanel.add(lblPatientInfo);
        appointmentRegisterPanel.add(new JLabel("Problem:"));
        appointmentRegisterPanel.add(txtProblem);
        appointmentRegisterPanel.add(btnSubmit);
        appointmentRegisterPanel.add(btnBack);

        btnFetch.addActionListener(e -> {
            String patientId = txtPatientID.getText().trim();
            if (patientId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a Patient ID!");
                return;
            }

            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement stmt = conn.prepareStatement("SELECT name, age, contact, address FROM patients WHERE patient_id = ?")) {
                stmt.setString(1, patientId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String details = "Name: " + rs.getString("name") + ", Age: " + rs.getInt("age") +
                                     ", Contact: " + rs.getString("contact") + ", Address: " + rs.getString("address");
                    lblPatientInfo.setText(details);
                } else {
                    JOptionPane.showMessageDialog(null, "Patient ID not found! Redirecting to Patient Register.");
                    cardLayout.show(mainPanel, "PatientRegister");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error fetching patient details: " + ex.getMessage());
            }
        });

        btnSubmit.addActionListener(e -> {
            String patientId = txtPatientID.getText().trim();
            String problem = txtProblem.getText().trim();
            
            if (patientId.isEmpty() || problem.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Patient ID and Problem cannot be empty!");
                return;
            }

            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "INSERT INTO appointments (appointment_id, registration_date, patient_id, problem, appointment_date) VALUES (?, NOW(), ?, ?, ?)")) {
                String appointmentID = generateAppointmentID(conn);
                String appointmentDate = generateRandomDateTime();

                stmt.setString(1, appointmentID);
                stmt.setString(2, patientId);
                stmt.setString(3, problem);
                stmt.setString(4, appointmentDate);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Appointment registered successfully! Generated Appointment ID: " + appointmentID + "\n\nAppointment Date: " + appointmentDate);
                clearFields(txtPatientID, txtProblem, lblPatientInfo); // Clear fields after submission
                cardLayout.show(mainPanel, "MainMenu");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error registering appointment: " + ex.getMessage());
            }
        });

        btnBack.addActionListener(e -> {
            clearFields(txtPatientID, txtProblem, lblPatientInfo); // Clear fields when going back
            cardLayout.show(mainPanel, "MainMenu");
        });

        mainPanel.add(appointmentRegisterPanel, "AppointmentRegister");
    }

    private void clearFields(JTextField patientIdField, JTextField problemField, JLabel patientInfoLabel) {
        patientIdField.setText("");
        problemField.setText("");
        patientInfoLabel.setText("Patient details will appear here");
    }

    private String generateAppointmentID(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM appointments")) {
            int count = rs.next() ? rs.getInt(1) + 1 : 1;
            return "apid" + String.format("%02d", count);
        }
    }

    private String generateRandomDateTime() {
        Random random = new Random();
        int year = 2024;
        int month = random.nextInt(12) + 1;
        int day = random.nextInt(28) + 1; // Ensure a valid day
        int hour = random.nextInt(24);
        int minute = random.nextInt(60);
        return String.format("%04d-%02d-%02d %02d:%02d:00", year, month, day, hour, minute);
    }

    private void initializeMedicalRegisterPanel() {
        medicalRegisterPanel = new JPanel(new GridLayout(3, 2));
        JTextField txtPatientID = new JTextField();
        JButton btnFetch = new JButton("Fetch");
        JButton btnMedicalPanel = new JButton("Medical Panel");
        JButton btnBillingPanel = new JButton("Billing Panel");
        JButton btnBack = new JButton("Back");

        // Adding components to the medical register panel
        medicalRegisterPanel.add(new JLabel("Patient ID:"));
        medicalRegisterPanel.add(txtPatientID);
        medicalRegisterPanel.add(btnFetch);
        medicalRegisterPanel.add(btnMedicalPanel);
        medicalRegisterPanel.add(btnBillingPanel);
        medicalRegisterPanel.add(btnBack);

        btnFetch.addActionListener(e -> {
            String patientID = txtPatientID.getText();
            if (patientID.isEmpty() || !patientExists(patientID)) {
                JOptionPane.showMessageDialog(null, "Patient ID not found!");
            } else {
                currentPatientID = patientID; // Store the current patient ID
                JOptionPane.showMessageDialog(null, "Patient found! Proceed to Medical or Billing Panel.");
            }
        });

        btnMedicalPanel.addActionListener(e -> {
            if (currentPatientID != null) {
                cardLayout.show(mainPanel, "MedicalPanel");
            } else {
                JOptionPane.showMessageDialog(null, "Please fetch a valid Patient ID first.");
            }
        });

        btnBillingPanel.addActionListener(e -> {
            if (currentPatientID != null) {
                cardLayout.show(mainPanel, "BillingPanel");
            } else {
                JOptionPane.showMessageDialog(null, "Please fetch a valid Patient ID first.");
            }
        });

        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        mainPanel.add(medicalRegisterPanel, "MedicalRegister");
    }

    private void initializeMedicalPanel() {
        medicalPanel = new JPanel(new GridLayout(5, 2));
        JTextField txtMedicineName = new JTextField();
        JTextField txtQuantity = new JTextField();
        JTextField txtAmount = new JTextField();
        JButton btnSubmit = new JButton("Submit Medical Record");
        JButton btnBack = new JButton("Back");

        medicalPanel.add(new JLabel("Medicine Name:"));
        medicalPanel.add(txtMedicineName);
        medicalPanel.add(new JLabel("Quantity:"));
        medicalPanel.add(txtQuantity);
        medicalPanel.add(new JLabel("Amount:"));
        medicalPanel.add(txtAmount);
        medicalPanel.add(btnSubmit);
        medicalPanel.add(btnBack);

        btnSubmit.addActionListener(e -> {
            String medicineName = txtMedicineName.getText();
            String quantity = txtQuantity.getText();
            String amount = txtAmount.getText();

            if (medicineName.isEmpty() || quantity.isEmpty() || amount.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            } else {
                try {
                    int qty = Integer.parseInt(quantity);
                    double amt = Double.parseDouble(amount);
                    if (qty <= 0 || amt <= 0) {
                        JOptionPane.showMessageDialog(null, "Quantity and Amount must be positive values.");
                    } else {
                        registerMedicalRecord((String) currentPatientID, medicineName, quantity, amt);
                        txtMedicineName.setText("");
                        txtQuantity.setText("");
                        txtAmount.setText("");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format for Quantity or Amount.");
                }
            }
        });

        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "MedicalRegister"));
        mainPanel.add(medicalPanel, "MedicalPanel");
    }

    private void registerMedicalRecord(String patientID, String medicineName, String quantity, double billAmount) {
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO medical_records (medical_id, patient_id, medicine_name, quantity, amount, registration_date) VALUES (?, ?, ?, ?, ?, NOW())")) {
            String medicalID = generateMedicalID(conn);
            stmt.setString(1, medicalID);
            stmt.setString(2, patientID);
            stmt.setString(3, medicineName);
            stmt.setString(4, quantity);
            stmt.setDouble(5, billAmount);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Medical record registered successfully! Generated Medical ID: " + medicalID);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error while registering medical record: " + ex.getMessage());
        }
    }

    private void initializeBillingPanel() {
        billingPanel = new JPanel(new GridLayout(5, 2));
        JTextField txtBillingAmount = new JTextField();
        JComboBox<String> cmbPaymentMethod = new JComboBox<>(new String[]{"Cash", "Credit Card", "Debit Card", "UPI"});
        JButton btnSubmitBilling = new JButton("Submit Billing");
        JButton btnBack = new JButton("Back");

        billingPanel.add(new JLabel("Billing Amount:"));
        billingPanel.add(txtBillingAmount);
        billingPanel.add(new JLabel("Payment Method:"));
        billingPanel.add(cmbPaymentMethod);
        billingPanel.add(btnSubmitBilling);
        billingPanel.add(btnBack);

        btnSubmitBilling.addActionListener(e -> {
            String amount = txtBillingAmount.getText();
            if (amount.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            } else {
                try {
                    double amt = Double.parseDouble(amount);
                    if (amt <= 0) {
                        JOptionPane.showMessageDialog(null, "Billing amount must be a positive value.");
                    } else {
                        submitBilling((String) currentPatientID, amt, cmbPaymentMethod.getSelectedItem().toString());
                        txtBillingAmount.setText(""); // Clear the input field after submission
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format for Billing Amount.");
                }
            }
        });

        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "MedicalRegister"));
        mainPanel.add(billingPanel, "BillingPanel");
    }

    private void submitBilling(String patientID, double amount, String paymentMethod) {
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO billing (billing_id, patient_id, amount_paid, payment_method, payment_date) VALUES (?, ?, ?, ?, NOW())")) {
            String billingID = generateBillingID(conn);
            stmt.setString(1, billingID);
            stmt.setString(2, patientID);
            stmt.setDouble(3, amount);
            stmt.setString(4, paymentMethod);
            stmt.executeUpdate();

            double remainingBalance = calculateBalance(conn, patientID);
            JOptionPane.showMessageDialog(null, "Billing registered successfully! Remaining balance: " + remainingBalance);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error while submitting billing: " + ex.getMessage());
        }
    }

    private double calculateBalance(Connection conn, String patientID) throws SQLException {
        double totalMedicalAmount = 0;
        double totalPaidAmount = 0;

        // Query to get the total amount from medical_records for the patient
        String medicalRecordQuery = "SELECT SUM(amount) AS total_amount FROM medical_records WHERE patient_id = ?";
        try (PreparedStatement medicalStmt = conn.prepareStatement(medicalRecordQuery)) {
            medicalStmt.setString(1, patientID);
            ResultSet rs = medicalStmt.executeQuery();
            if (rs.next()) {
                totalMedicalAmount = rs.getDouble("total_amount");
            }
        }

        // Query to get the total amount paid from billing for the patient
        String billingQuery = "SELECT SUM(amount_paid) AS total_paid FROM billing WHERE patient_id = ?";
        try (PreparedStatement billingStmt = conn.prepareStatement(billingQuery)) {
            billingStmt.setString(1, patientID);
            ResultSet rs = billingStmt.executeQuery();
            if (rs.next()) {
                totalPaidAmount = rs.getDouble("total_paid");
            }
        }

        // Calculate the remaining balance
        return totalMedicalAmount - totalPaidAmount;
    }

    private boolean patientExists(String patientID) {
        // This method checks if a patient exists in the database
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM patients WHERE patient_id = ?")) {
            stmt.setString(1, patientID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error checking patient existence: " + ex.getMessage());
        }
        return false;
    }

    private String generateMedicalID(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM medical_records");
        int count = rs.next() ? rs.getInt(1) + 1 : 1;
        return "mid" + String.format("%02d", count);
    }

    private String generateBillingID(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM billing");
        int count = rs.next() ? rs.getInt(1) + 1 : 1;
        return "bid" + String.format("%02d", count);
    }

    private void initializeDischargeRecordPanel() {
        dischargeRecordPanel = new JPanel(new GridLayout(4, 2)); // Added an extra row for layout adjustment
        JTextField txtPatientID = new JTextField();
        JButton btnFetch = new JButton("Fetch");
        JLabel lblDischargeStatus = new JLabel("Discharge status will appear here");
        JButton btnBack = new JButton("Back");

        dischargeRecordPanel.add(new JLabel("Patient ID:"));
        dischargeRecordPanel.add(txtPatientID);
        dischargeRecordPanel.add(btnFetch);
        dischargeRecordPanel.add(lblDischargeStatus);
        dischargeRecordPanel.add(btnBack);

        btnFetch.addActionListener(e -> {
            String patientID = txtPatientID.getText().trim(); // Trim input to avoid leading/trailing spaces
            if (patientID.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid Patient ID.");
                return;
            }

            // Disable the fetch button to prevent multiple submissions
            btnFetch.setEnabled(false);
            lblDischargeStatus.setText("Fetching done"); // Indicate that a fetch is in progress

            try (Connection conn = DBConnect.getConnection()) {
                // Fetch the total amount from medical records
                double totalAmount = 0;
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT SUM(amount) AS total_amount FROM medical_records WHERE patient_id = ?")) {
                    stmt.setString(1, patientID);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        totalAmount = rs.getDouble("total_amount");
                    }
                }

                // Fetch the total payments made from billing
                double totalPayments = 0;
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT SUM(amount_paid) AS total_paid FROM billing WHERE patient_id = ?")) {
                    stmt.setString(1, patientID);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        totalPayments = rs.getDouble("total_paid");
                    }
                }

                // Calculate the balance
                double balance = totalAmount - totalPayments;

                // Update the discharge status based on the balance
                if (balance <= 0) {
                    lblDischargeStatus.setText("Patient Discharged Done");
                } else {
                	String balanceDetail = "Payment not cleared. Balance: " + balance + "\n" + 
 						                   "Patient cannot Discharge";
                	JOptionPane.showMessageDialog(null, balanceDetail, "Balance Details", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                lblDischargeStatus.setText("Error fetching data: " + ex.getMessage());
            } finally {
                // Re-enable the button and clear the patient ID field
                btnFetch.setEnabled(true);
                txtPatientID.setText("");
            }
        });

        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));
        mainPanel.add(dischargeRecordPanel, "DischargeRecord");
    }

    private void initializePatientRecordPanel() {
        patientRecordPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        JTextField txtPatientID = new JTextField();
        JButton btnFetch = new JButton("Fetch");
        JTextArea txtPatientDetails = new JTextArea(10, 30);
        txtPatientDetails.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtPatientDetails);
        JButton btnBack = new JButton("Back");

        // Panel to hold appointment and medical record buttons
        JPanel recordsPanel = new JPanel();
        recordsPanel.setLayout(new BoxLayout(recordsPanel, BoxLayout.Y_AXIS));
        
        // Panel to display visiting dates
        JPanel visitingDatesPanel = new JPanel();
        visitingDatesPanel.setLayout(new BoxLayout(visitingDatesPanel, BoxLayout.Y_AXIS));

        // Add components to the input panel
        inputPanel.add(new JLabel("Patient ID:"));
        inputPanel.add(txtPatientID);
        inputPanel.add(btnFetch);
        inputPanel.add(btnBack);

        // Add input panel, scroll pane, and visiting dates panel to the main patient record panel
        patientRecordPanel.add(inputPanel, BorderLayout.NORTH);
        patientRecordPanel.add(scrollPane, BorderLayout.CENTER);
        patientRecordPanel.add(recordsPanel, BorderLayout.SOUTH);
        patientRecordPanel.add(visitingDatesPanel, BorderLayout.EAST); // Adjust this as needed

        // Fetch button action listener
        btnFetch.addActionListener(e -> {
            String patientID = txtPatientID.getText().trim(); // Trim input to avoid leading/trailing spaces
            if (patientID.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a valid Patient ID.");
                return;
            }

            txtPatientDetails.setText("");
            recordsPanel.removeAll(); // Clear previous records
            visitingDatesPanel.removeAll(); // Clear previous visiting dates
            
            // Disable button during fetch
            btnFetch.setEnabled(false);
            try (Connection conn = DBConnect.getConnection()) {
                // Fetch patient details
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM patients WHERE patient_id = ?");
                stmt.setString(1, patientID);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    StringBuilder details = new StringBuilder();
                    details.append("Patient ID: ").append(rs.getString("patient_id")).append("\n");
                    details.append("Name: ").append(rs.getString("name")).append("\n");
                    details.append("Age: ").append(rs.getInt("age")).append("\n");
                    details.append("Gender: ").append(rs.getString("gender")).append("\n");
                    details.append("DOB: ").append(rs.getString("dob")).append("\n");
                    details.append("Contact: ").append(rs.getString("contact")).append("\n");
                    details.append("Address: ").append(rs.getString("address")).append("\n");
                    details.append("Problem: ").append(rs.getString("problem")).append("\n");
                    details.append("Registration Time: ").append(rs.getString("registration_date")).append("\n");
                    txtPatientDetails.setText(details.toString());
                } else {
                    JOptionPane.showMessageDialog(null, "Patient ID not found!");
                    return;
                }

                // Fetch appointment records
                PreparedStatement stmtAppointments = conn.prepareStatement(
                        "SELECT appointment_id, registration_date, problem, appointment_date FROM appointments WHERE patient_id = ?");
                stmtAppointments.setString(1, patientID);
                ResultSet rsAppointments = stmtAppointments.executeQuery();
                while (rsAppointments.next()) {
                    String appointmentID = rsAppointments.getString("appointment_id");
                    String registrationDate = rsAppointments.getString("registration_date");
                    String problem = rsAppointments.getString("problem");
                    String appointmentDateTime = rsAppointments.getString("appointment_date");

                    JButton btnAppointment = new JButton("Appointment");
                    btnAppointment.addActionListener(evt -> {
                        String appointmentDetails = "Appointment ID: " + appointmentID + "\n" +
                                                    "Registration Date: " + registrationDate + "\n" +
                                                    "Problem: " + problem + "\n" +
                                                    "Appointment DateTime: " + appointmentDateTime + "\n";
                        JOptionPane.showMessageDialog(null, appointmentDetails, "Appointment Details", JOptionPane.INFORMATION_MESSAGE);
                    });
                    recordsPanel.add(btnAppointment);

                    // Add the visiting date to the visiting dates panel
                    visitingDatesPanel.add(new JLabel("Visiting Appointment Dates"));
                    visitingDatesPanel.add(new JLabel("Visiting Date: " + appointmentDateTime));
                }

                // Fetch medical records
                PreparedStatement stmtMedicalRecords = conn.prepareStatement(
                        "SELECT medical_id, registration_date, medicine_name, quantity FROM medical_records WHERE patient_id = ?");
                stmtMedicalRecords.setString(1, patientID);
                ResultSet rsMedicalRecords = stmtMedicalRecords.executeQuery();
                while (rsMedicalRecords.next()) {
                    String medicalID = rsMedicalRecords.getString("medical_id");
                    String registrationDate = rsMedicalRecords.getString("registration_date");
                    String medicineName = rsMedicalRecords.getString("medicine_name");
                    int quantity = rsMedicalRecords.getInt("quantity");

                    JButton btnMedical = new JButton("Medical");
                    btnMedical.addActionListener(evt -> {
                        String medicalDetails = "Medical ID: " + medicalID + "\n" +
                                                "Registration Date: " + registrationDate + "\n" +
                                                "Medicine Name: " + medicineName + "\n" +
                                                "Quantity: " + quantity + "\n";
                        JOptionPane.showMessageDialog(null, medicalDetails, "Medical Details", JOptionPane.INFORMATION_MESSAGE);
                    });
                    recordsPanel.add(btnMedical);

                    // Add the visiting date to the visiting dates panel
                    visitingDatesPanel.add(new JLabel("Visited Registration Dates"));
                    visitingDatesPanel.add(new JLabel("Visiting Date: " + registrationDate));
                }

                // Refresh the records panel to display the buttons
                recordsPanel.revalidate();
                recordsPanel.repaint();
                visitingDatesPanel.revalidate();
                visitingDatesPanel.repaint();
                
                // Provide feedback that fetching is completed
                if (recordsPanel.getComponentCount() == 0) {
                    JOptionPane.showMessageDialog(null, "No records found for the specified Patient ID.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error fetching patient details: " + ex.getMessage());
            } finally {
                // Re-enable the fetch button and clear input field
                btnFetch.setEnabled(true);
                txtPatientID.setText(""); // Reset the Patient ID field
            }
        });

        // Back button action listener
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "MainMenu"));

        mainPanel.add(patientRecordPanel, "PatientRecord");
    }

    public static void main(String[] args) {
        new PatientManagementUI();
    }
}
