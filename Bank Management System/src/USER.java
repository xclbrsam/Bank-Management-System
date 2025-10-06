import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class  USER{

    private Connection connection;
    private Scanner scanner;
    public USER(Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    //Register  New User Method

    public void register()
    {
        scanner.nextLine();
        System.out.println("Full Name:");
        String full_name = scanner.nextLine();

        System.out.println("email:");
        String email = scanner.nextLine();

        System.out.println("Password");
        String Password = scanner.nextLine();

        if (user_exists(email))
        {
            System.out.println("User Already Exists");
            System.out.println("User Different Email to Register");
            return;
        }
        String register_query = "insert into user(full_name,email,password) values(?,?,?)";

        try
        {
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1, full_name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, Password);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0) {
                System.out.println("Registration Successful");

            }
            else {
                System.out.println("Registration Unsuccessful");
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
// User Login

    public String login(){
        scanner.nextLine();
        System.out.println("email:");
        String email = scanner.nextLine();

        System.out.println("Password:");
        String Password = scanner.nextLine();


        String login_query = "select * form user where email = ? AND password = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(login_query);

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, Password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return email;
            } else {
                return null;
            }
        }
            catch (SQLException e) {
                e.printStackTrace();
            }
            return null;

    }

        public boolean user_exists(String email){
            String query = "SELECT * from user where email = ?;";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return true;
                } else {
                    return false;
                }
            }
                catch(SQLException e){
                    e.printStackTrace();
                }
                return false;

        }





  }

