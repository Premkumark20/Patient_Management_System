package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnect {
    private static final String URL = "jdbc:mysql://localhost:3306/patient_management_java";
    private static final String USER = "root";
    private static final String PASSWORD = "20050430";

    // Method to establish a database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Method to initialize the database and create tables
    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Create Patients Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS patients (" +
                    "patient_id VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "age INT NOT NULL, " +
                    "gender VARCHAR(10) NOT NULL, " +
                    "dob DATE NOT NULL, " +
                    "contact VARCHAR(15) NOT NULL, " +
                    "address TEXT NOT NULL, " +
                    "problem TEXT NOT NULL, " +
                    "registration_date DATETIME NOT NULL)");
            
            //create Patient_records Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS patient_records (" +
                    "record_id VARCHAR(10) NOT NULL PRIMARY KEY, " +
                    "patient_id VARCHAR(10) NOT NULL, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "age INT NOT NULL, " +
                    "gender VARCHAR(10) NOT NULL, " +
                    "dob DATE NOT NULL, " +
                    "contact VARCHAR(15) NOT NULL, " +
                    "address TEXT NOT NULL, " +
                    "problem TEXT NOT NULL, " +
                    "registration_date DATETIME NOT NULL UNIQUE, " +
                    "FOREIGN KEY (patient_id) REFERENCES patients(patient_id))");

            // Create Appointments Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS appointments (" +
                    "appointment_id VARCHAR(10) PRIMARY KEY, " +
                    "registration_date DATETIME NOT NULL," +
                    "patient_id VARCHAR(10) NOT NULL, " +
                    "problem TEXT NOT NULL, " +
                    "appointment_date DATETIME NOT NULL, " +
                    "FOREIGN KEY (patient_id) REFERENCES patients(patient_id))");

            // Create Medical Records Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS medical_records (" +
                    "medical_id VARCHAR(10) PRIMARY KEY, " +
                    "patient_id VARCHAR(10) NOT NULL, " +
                    "medicine_name VARCHAR(100) NOT NULL, " +
                    "quantity INT NOT NULL, " +
                    "amount DOUBLE NOT NULL, " +
                    "registration_date DATETIME NOT NULL, " +
                    "FOREIGN KEY (patient_id) REFERENCES patients(patient_id))");

            // Create Billing Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS billing (" +
                    "billing_id VARCHAR(10) PRIMARY KEY NOT NULL, " +
                    "patient_id VARCHAR(10) NOT NULL, " +
                    "payment_method VARCHAR(20) NOT NULL, " +
                    "amount_paid DOUBLE NOT NULL, " +
                    "payment_date DATETIME NOT NULL, " +
                    "FOREIGN KEY (patient_id) REFERENCES patients(patient_id))");

            System.out.println("Database and tables initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
