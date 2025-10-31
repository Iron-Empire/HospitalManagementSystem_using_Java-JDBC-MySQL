package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {

    //Declaring the Necessary Variables for the Java DataBase Connectivity (JDBC)
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Iron@880";

    //Main Function
    public static void main(String[] args){

        //Loading the MySQL JDBC Driver
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        try{
            //Establishing Connection using DriverManager Class
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection, scanner); //Making Object of Patient Class
            Doctor doctor = new Doctor(connection); //Making Object of Doctor Class

            while(true){
                System.out.println("\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("~~HOSPITAL MANAGEMENT SYSTEM~~");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctor");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

                //Taking Choice from the User
                System.out.println("\nEnter your choice: ");
                int choice = scanner.nextInt();

                switch(choice){
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        doctor.viewDoctor();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointment(connection, scanner, patient, doctor);
                        System.out.println();
                        break;
                    case 5:
                        System.out.println("\nThank You for using HOSPITAL MANAGEMENT SYSTEM !!!");
                        return;
                    default:
                        System.out.println("\nEnter Valid Choice !!!");
                        break;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    //Function to Book Appointment
    public static void bookAppointment(Connection connection, Scanner scanner, Patient patient, Doctor doctor){

        //Taking Data from the User
        System.out.println("Enter Patient ID: ");
        int patientID = scanner.nextInt();
        System.out.println("Enter Doctor ID: ");
        int doctorID = scanner.nextInt();
        System.out.println("Enter the appointment date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();

        if(patient.getPatientByID(patientID) && doctor.getDoctorByID(doctorID)){
            if(checkDoctorAvailability(doctorID, appointmentDate, connection)){
                String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)"; //SQL Query
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientID);
                    preparedStatement.setInt(2, doctorID);
                    preparedStatement.setString(3, appointmentDate);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if(rowsAffected>0){
                        System.out.println("Appointment Booked !!!");
                    }else{
                        System.out.println("Failed to Book Appointment !!!");
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }else{
                System.out.println("Doctor Not Available on this Date !!!\nTry Booking for Another Date !!!");
            }
        }else{
            System.out.println("Either Doctor or Patient doesn't exist !!!");
        }
    }

    //Function to Check Availability of Doctor on the required Date
    public static boolean checkDoctorAvailability(int doctorID, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?"; //SQL Query
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorID);
            preparedStatement.setString(2, appointmentDate);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count == 0){
                    return true;
                }else{
                    return false;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }
}
