import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner= scanner;
    }

    public long open_account(String email){

        if(!account_exists(email)){
            String open_account_query ="insert into Account(account_number, full_name,email,balance , security_pin)values(?,?,?,?)";
            scanner.nextLine();
            System.out.println("Enter Full Name: ");
            String full_name = scanner.nextLine();
            System.out.println("Enter initial Amount: ");
            String balance = scanner.nextLine();
            System.out.println("Enter Security Pin(4 Digits) : ");
            String security_pin = scanner.nextLine();

            try{
                long account_number = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, full_name);
                preparedStatement.setString(3,email);
                preparedStatement.setString(4,balance);
                preparedStatement.setString(5, security_pin);

                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected> 0){
                    return  account_number;
                }
                else{
                    throw new RuntimeException("Account Creation Failed");
                }

            }
            catch (SQLException e){
                  e.printStackTrace();
            }
        }

    throw new RuntimeException("Account Already Exists");



    }

   public long getAccount_number(String email){
      String query = "Sekect * from account where email = ?";
      try {
          PreparedStatement preparedStatement = connection.prepareStatement(query);
          preparedStatement.setString(1, email);
          ResultSet resultSet = preparedStatement.executeQuery();

          if (resultSet.next()) {
              return resultSet.getLong("account_number");
          }

      } catch (SQLException e){
          e.printStackTrace();
      }
      throw new RuntimeException("Account Number Doesnot Exists .. !!");

   }

    private long generateAccountNumber(){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select account_number from account order by account_number desc limit 1 ");
            if(resultSet.next()){
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number + 1;
            } else {
                return 51001000;

            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 51001000;

    }
    public boolean account_exists(String email){
      String query = "Select account_number from account where email = ?";
      try{
          PreparedStatement preparedStatement = connection.prepareStatement(query);
          preparedStatement.setString(1, email);
          ResultSet resultSet = preparedStatement.executeQuery();
          if(resultSet.next()){
              return true;
          }
          else {
              return false;
          }

       }catch (SQLException e){
          e.printStackTrace();
      }
       return false;
    }


}
