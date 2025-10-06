import java.util.*;
import java.sql.*;

public class BankingApp {
    private static final String url ="jdbc:mysql://localhost:3306/bankproject";
    private static final String username = "root";
    private static final String password = "Sam0311";

    public static void main (String[] args) throws ClassNotFoundException,SQLException{
         try {
             Class.forName("com.mysql.jdbc.Driver");
             System.out.println("Connected successfully");
         }
         catch (ClassNotFoundException e){
             System.out.println(e.getMessage());
         }

         try{
             Connection connection = DriverManager.getConnection(url,username,password);
             Scanner scanner = new Scanner(System.in);
             USER user = new USER (connection,scanner);
             Accounts accounts = new Accounts(connection,scanner);
              AccountManager accountManager = new AccountManager(connection,scanner);

             String email = "";
             long account_number = 0L;


             while (true){
                 System.out.println("**** Welcome To Banking System");
                 System.out.println();
                 System.out.println("1.Register");
                 System.out.println("2. Login");
                 System.out.println("3. Exit");
                 System.out.println("Enter Your Choice :");

                 int choice1 = scanner.nextInt();
                 switch (choice1){
                     case 1:
                          user.register();
                          System.out.println();
                          System.out.flush();
                          break;
                     case 2:

                         user.login();
                         if(email!=null){
                             System.out.println();
                             System.out.println("User Logged IN .!");
                             if(!accounts.account_exists(email)){
                                 System.out.println();
                                 System.out.println("1. Open a new Bank Account");
                                 System.out.println("2.Exit");
                                 if (scanner.nextInt() == 1){
                                     account_number= accounts.open_account(email);
                                     System.out.println("Account Created Successfully");
                                     System.out.println("Your Account Number is: " + account_number);

                                 } else{
                                     break;
                                 }
                             }
                             account_number = accounts.getAccount_number(email);
                             int choice2 = 0;
                             while (choice2 != 5){
                                 System.out.println();
                                 System.out.println("1.Debit Money");
                                 System.out.println("2. Credit Money");
                                 System.out.println("3. Transfer Money");
                                 System.out.println("4. Check Balance");
                                 System.out.println("5. Log Out");
                                 System.out.println("Enter your choice : ");
                                 choice2= scanner.nextInt();
                                 switch (choice2){
                                     case 1:
                                         accountManager.debit_money(account_number);
                                         break;
                                     case 2:
                                         accountManager.credit_money(account_number);
                                         break;
                                     case 3:
                                         accountManager.transfer_money(account_number);
                                         break;
                                     case 4:
                                         accountManager.getbalance(account_number);
                                         break;
                                     case 5:
                                         break;
                                     default:
                                         System.out.println("Enter Valid Choice !!");



                                 }
                             }

                         }
                         else{
                             System.out.println("Incorrect Email or Password");
                         }
                     case 3:
                         System.out.println("Thank you for using banking System");
                         System.out.println("Exiting System");
                         return;
                     default:
                         System.out.println("Enter Valid Choice");
                         break;
                 }
             }

         }catch (SQLException e){
             e.printStackTrace();
         }





    }

}
