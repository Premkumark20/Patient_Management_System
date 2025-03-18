# Patient Management System

A comprehensive Java-based hospital management system that helps healthcare providers manage patient information, appointments, medical records, billing, and discharge processes efficiently.

## Project Overview

The Patient Management System is a desktop application built using Java Swing for the UI and MySQL for database management. It provides an intuitive interface for healthcare staff to manage various aspects of patient care and hospital administration.

## Features

- **Patient Registration**: Register new patients with personal details and medical issues
- **Appointment Management**: Schedule and track patient appointments
- **Medical Records**: Maintain detailed medical records including medicines prescribed
- **Billing System**: Process payments with multiple payment methods and track balances
- **Discharge Management**: Handle patient discharge based on payment status
- **Patient Records**: Access comprehensive patient history and visit information

## Technologies Used

- Java
- Java Swing for GUI
- MySQL Database
- JDBC for database connectivity

## System Requirements

- Java Development Kit (JDK) 8 or higher
- MySQL Server 5.7 or higher
- MySQL Connector/J (JDBC Driver)

## Database Setup

1. Create a MySQL database named `patient_management_java`
2. Update the database connection details in `com.db.DBConnect` class:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/patient_management_java";
   private static final String USER = "root";
   private static final String PASSWORD = "your_password";
   ```

## How to Run

1. Clone this repository
2. Set up the MySQL database as described above
3. Compile and run the `com.main.Main` class
4. The application will initialize the database tables automatically on first run

## Project Structure

- `com.main`: Contains the main class to launch the application
- `com.db`: Contains database connectivity and initialization code
- `com.ui`: Contains all UI components and business logic

## Key Components

### Patient Registration
- Create new patient profiles with unique Patient IDs
- Store patient demographics, contact information, and medical problems

### Appointment Scheduling
- Register appointments for existing patients
- Automatically generate appointment dates and times
- Track appointment history

### Medical Records
- Record prescribed medicines with quantity and amount
- Maintain a complete medical history for each patient

### Billing System
- Process payments using various methods (Cash, Credit Card, Debit Card, UPI)
- Calculate and display remaining balances
- Link payments to patient records

### Discharge Management
- Verify payment clearance before discharge
- Provide detailed balance information for pending payments

### Patient Records
- View comprehensive patient information
- Access medical and appointment history
- Review visit dates and details

## Project Demo
View a demonstration of this project on LinkedIn: [Patient Management System Demo](https://www.linkedin.com/posts/premkumar-k-506922299_im-thrilled-to-share-that-ive-successfully-activity-7291000400785149952-r0dT?utm_source=share&utm_medium=member_android&rcm=ACoAAEg3rlYBHOTV_lA8UxkMNp2QNbWMsH5Ud6s)  
View this project on LinkedIn: [Premkumar K](https://www.linkedin.com/posts/premkumar-k-506922299)

## Future Enhancements

- User authentication and role-based access
- Enhanced reporting capabilities
- Prescription management
- Inventory management for medicines
- Integration with laboratory systems

## Developer
### Development Team  

- **[Premkumar K](https://github.com/premkumark20)** – Develope & Architecture
- **[Sairam L](https://github.com/sairam5566)** – Development & Features  
- **[Monishkumar US]()** – Optimization & Documentation

## Acknowledgment
We sincerely appreciate the contributions and support of all developers, testers, and stakeholders
who have helped make this project a success. Special thanks to our authorized developers for their 
dedication and hard work.
