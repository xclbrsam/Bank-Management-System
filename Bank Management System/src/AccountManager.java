import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {

   private Connection connection;
   private Scanner scanner;

   AccountManager(Connection connection, Scanner scanner){
       this.connection = connection;
       this.scanner = scanner;
   }


   public void credit_money(long account_number) throws SQLException{
       scanner.nextLine();
       System.out.println("Enter Amount: ");
       double amount= scanner.nextDouble();
       System.out.println("Enter Security pin ");
       String security_pin = scanner.nextLine();

       try{
           connection.setAutoCommit(false);
           if(account_number !=0 ){
               PreparedStatement preparedStatement = connection.prepareStatement("Select * from account_number where security_pin = ?");
               preparedStatement.setString(1, security_pin);
               ResultSet resultSet = preparedStatement.executeQuery();

               if(resultSet.next()){
                   String credit_query = "update account set balance = balance + ? where account_number = ?";
                   PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                   preparedStatement.setDouble(1,amount);
                   preparedStatement1.setLong(2, account_number);
                   int rowsAffected = preparedStatement1.executeUpdate();
                   if(rowsAffected>0){
                       System.out.println("Rs."+amount+"Credited Successfully");
                       connection.commit();
                       connection.setAutoCommit(true);
                       return;
                   }else {
                       System.out.println("Transaction failed..!!");
                   }
               }else{
                   System.out.println("Invalid Security Pin ..!!");
               }
           }
       } catch (SQLException e){
           e.printStackTrace();
       }
    connection.setAutoCommit(true);
   }



    public void debit_money(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.println("Enter Amount: ");
        double amount= scanner.nextDouble();
        System.out.println("Enter Security pin ");
        String security_pin = scanner.nextLine();

        try{
            connection.setAutoCommit(false);
            if(account_number !=0 ){
                PreparedStatement preparedStatement = connection.prepareStatement("Select * from account_number where security_pin = ?");
                preparedStatement.setString(1, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    String debit_query = "update account set balance = balance - ? where account_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                    preparedStatement.setDouble(1,amount);
                    preparedStatement1.setLong(2, account_number);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Rs."+amount+"Debited Successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    }else {
                        System.out.println("Transaction failed..!!");
                    }
                }else{
                    System.out.println("Invalid Security Pin ..!!");
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }



    public void getbalance(long account_number){
       scanner.nextLine();
       System.out.println("Enter Security Pin: ");
       String security_pin = scanner.nextLine();
       String getbalquery = "Select * from account where account_number = ? AND security_pin = ?";
       try{
           PreparedStatement preparedStatement = connection.prepareStatement(getbalquery);
           preparedStatement.setLong(1,account_number);
           preparedStatement.setString(2,security_pin);
           ResultSet resultSet = preparedStatement.executeQuery();
           if(resultSet.next()){
               double balance = resultSet.getDouble("balance");
               System.out.println("Balance "+balance);
           }else{
               System.out.println("Invalid Pin ..!!");
           }
       } catch (SQLException e){
           e.printStackTrace();
       }
    }


    public void transfer_money(long sender_account_number) throws SQLException{
     scanner.nextLine();
     System.out.println("Enter Reciever Account NUmber: ");
     long reciever_account_number = scanner.nextLong();
     System.out.println("Enter Amount");
     Double amount = scanner.nextDouble();

     scanner.nextLine();
     System.out.println("Enter Security Pin: ");
     String security_pin = scanner.nextLine();
     try{
         connection.setAutoCommit(false);
         if (sender_account_number !=0 && reciever_account_number !=0){
             PreparedStatement preparedStatement = connection.prepareStatement("Select * from account where account_number =? and security_pin = ");
             preparedStatement.setLong(1, sender_account_number);
             preparedStatement.setString(2, security_pin);
             ResultSet resultSet = preparedStatement.executeQuery();

             if (resultSet.next()){
                 double current_balance = resultSet.getDouble("balance");
                 if(amount<=current_balance){
                     String debit_query = "update account set balance = balance - ? where account_number = ?";
                     String credit_query = "update account set balance = balance + ? where account_number = ?";
                     PreparedStatement creditpreparedStatement = connection.prepareStatement(credit_query);
                     PreparedStatement debitpreparedStatement = connection.prepareStatement(debit_query);
                     creditpreparedStatement.setDouble(1, amount);
                     creditpreparedStatement.setLong(2, reciever_account_number);
                     debitpreparedStatement.setDouble(1, amount);
                     debitpreparedStatement.setLong(2,sender_account_number);

                     int rowsaffected1 = debitpreparedStatement.executeUpdate();
                     int rowsaffected2 = creditpreparedStatement.executeUpdate();
                     if(rowsaffected1 > 0 && rowsaffected2> 0){
                         System.out.println("Transaction Successful");
                         System.out.println("Rs."+amount+" Transferred Successfully");
                         connection.commit();
                         connection.setAutoCommit(true);
                         return;
                     }else {
                         System.out.println("Transaction Failed");
                         connection.commit();
                         connection.setAutoCommit(true);
                     }
                 } else{
                     System.out.println("Insufficient balance");
                 }
                 } else{
                 System.out.println("Invaild Security Pin");
                 }
             }  else{
             System.out.println("Invalid Account NUmber");
         }
     } catch (SQLException e){
         e.printStackTrace();
     }
     connection.setAutoCommit(true);
    }




}
