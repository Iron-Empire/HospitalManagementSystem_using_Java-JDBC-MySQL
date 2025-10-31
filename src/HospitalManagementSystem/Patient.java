package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Patient {

    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    //Function to Add Patient to the DataBase
    public void addPatient(){

        //Taking Data from the User
        System.out.print("Enter Patient Name: ");
        String name = scanner.next();
        System.out.print("Enter Patient Age: ");
        int age = scanner.nextInt();
        System.out.print("Enter Patient Gender: ");
        String gender = scanner.next();

        try{
            String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)"; //SQL Query
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);

            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows>0){
                System.out.println("Patient Added Successfully !!!");
            }else{
                System.out.println("Failed to Add Patient !!!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //Function to View Patients from the DataBase
    public void viewPatient(){
        String query = "SELECT * FROM patients"; //SQL Query
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Patients: ");
            System.out.println("+------------+--------------------+--------+----------+");
            System.out.println("| Patient ID | Name               | Age    | Gender   |");
            System.out.println("+------------+--------------------+--------+----------+");

            //Printing Data of Patients fetched from the DataBase
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");

                System.out.printf("| %-10s | %-18s | %-6s | %-8s |\n", id, name, age, gender);
                System.out.println("+------------+--------------------+--------+----------+");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //Function to Check Patient By ID in the DataBase
    public boolean getPatientByID(int id){
        String query = "SELECT * FROM patients WHERE id = ?"; //SQL Query
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return false;
    }

}
