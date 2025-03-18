package com.main;

import com.db.DBConnect;
import com.ui.PatientManagementUI;

public class Main {
    public static void main(String[] args) {
        // Initialize the database and tables
        DBConnect.initializeDatabase();
        
        // Launch the UI
        new PatientManagementUI();
    }
}
