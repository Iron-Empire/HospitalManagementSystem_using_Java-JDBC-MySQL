package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {

    private Connection connection;

    public Doctor(Connection connection){
        this.connection = connection;
    }

    //Function to View Doctors from the DataBase
    public void viewDoctor(){
        String query = "SELECT * FROM doctors"; //SQL Query
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Doctors: ");
            System.out.println("+-----------+---------------------+------------------+");
            System.out.println("| Doctor ID | Name                | Specialization   |");
            System.out.println("+-----------+---------------------+------------------+");

            //Printing Data of Doctors fetched from the DataBase
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");

                System.out.printf("| %-9s | %-19s | %-16s |\n", id, name, specialization);
                System.out.println("+-----------+---------------------+------------------+");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    //Function to Check Doctor by ID in the DataBase
    public boolean getDoctorByID(int id){
        String query = "SELECT * FROM doctors WHERE id = ?"; //SQL Query
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
