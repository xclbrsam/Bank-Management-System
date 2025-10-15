import java.util.*;
import java.sql.*;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/bankproject";
    private static final String username = "root";
    private static final String password = "Sam0311";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL Driver Loaded Successfully!");

            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Connected to Database Successfully!");

            Scanner scanner = new Scanner(System.in);
            USER user = new USER(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            String email = "";
            long account_number = 0L;

            while (true) {
                System.out.println("\n===== WELCOME TO BANKING SYSTEM =====");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter Your Choice: ");

                int choice1 = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice1) {
                    case 1:
                        user.register();
                        break;

                    case 2:
                        email = user.login();

                        if (email != null) {
                            System.out.println("\n✅ Login Successful!");

                            if (!accounts.account_exists(email)) {
                                System.out.println("1. Open a New Bank Account");
                                System.out.println("2. Exit");
                                System.out.print("Enter Choice: ");
                                if (scanner.nextInt() == 1) {
                                    account_number = accounts.open_account(email);
                                    System.out.println("✅ Account Created Successfully!");
                                    System.out.println("Your Account Number: " + account_number);
                                } else {
                                    break;
                                }
                            }

                            account_number = accounts.getAccount_number(email);
                            int choice2 = 0;

                            while (choice2 != 5) {
                                System.out.println("\n===== ACCOUNT MENU =====");
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Logout");
                                System.out.print("Enter Your Choice: ");
                                choice2 = scanner.nextInt();

                                switch (choice2) {
                                    case 1 -> accountManager.debit_money(account_number);
                                    case 2 -> accountManager.credit_money(account_number);
                                    case 3 -> accountManager.transfer_money(account_number);
                                    case 4 -> accountManager.getbalance(account_number);
                                    case 5 -> System.out.println("👋 Logged Out Successfully!");
                                    default -> System.out.println("❌ Invalid Choice!");
                                }
                            }

                        } else {
                            System.out.println("❌ Incorrect Email or Password!");
                        }
                        break;

                    case 3:
                        System.out.println("\n🙏 Thank You for Using Banking System!");
                        connection.close();
                        return;

                    default:
                        System.out.println("❌ Invalid Choice!");
                }
            }

        } catch (ClassNotFoundException e) {
            System.out.println("⚠️ MySQL Driver Not Found!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
