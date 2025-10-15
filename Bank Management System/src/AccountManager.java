import java.sql.*;
import java.util.Scanner;

public class AccountManager {
    private Connection connection;
    private Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // CREDIT
    public void credit_money(long account_number) throws SQLException {
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Security Pin: ");
        String pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM account WHERE account_number = ? AND security_pin = ?");
            ps.setLong(1, account_number);
            ps.setString(2, pin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PreparedStatement update = connection.prepareStatement("UPDATE account SET balance = balance + ? WHERE account_number = ?");
                update.setDouble(1, amount);
                update.setLong(2, account_number);
                int rows = update.executeUpdate();

                if (rows > 0) {
                    connection.commit();
                    System.out.println("✅ ₹" + amount + " credited successfully!");
                } else {
                    connection.rollback();
                }
            } else System.out.println("❌ Invalid Pin!");

        } finally {
            connection.setAutoCommit(true);
        }
    }

    // DEBIT
    public void debit_money(long account_number) throws SQLException {
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Security Pin: ");
        String pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement("SELECT balance FROM account WHERE account_number = ? AND security_pin = ?");
            ps.setLong(1, account_number);
            ps.setString(2, pin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance >= amount) {
                    PreparedStatement update = connection.prepareStatement("UPDATE account SET balance = balance - ? WHERE account_number = ?");
                    update.setDouble(1, amount);
                    update.setLong(2, account_number);
                    update.executeUpdate();
                    connection.commit();
                    System.out.println("✅ ₹" + amount + " debited successfully!");
                } else System.out.println("❌ Insufficient balance!");
            } else System.out.println("❌ Invalid Pin!");

        } finally {
            connection.setAutoCommit(true);
        }
    }

    // CHECK BALANCE
    public void getbalance(long account_number) {
        System.out.print("Security Pin: ");
        int pin = scanner.nextInt();
        String query = "SELECT balance FROM account WHERE account_number = ? AND security_pin = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, account_number);
            ps.setInt(2, pin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) System.out.println("💰 Current Balance: ₹" + rs.getDouble("balance"));
            else System.out.println("❌ Invalid Pin!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TRANSFER
    public void transfer_money(long sender_account) throws SQLException {
        System.out.print("Receiver Account Number: ");
        long receiver = scanner.nextLong();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Security Pin: ");
        String pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);

            PreparedStatement ps = connection.prepareStatement("SELECT balance FROM account WHERE account_number = ? AND security_pin = ?");
            ps.setLong(1, sender_account);
            ps.setString(2, pin);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance >= amount) {
                    PreparedStatement debit = connection.prepareStatement("UPDATE account SET balance = balance - ? WHERE account_number = ?");
                    debit.setDouble(1, amount);
                    debit.setLong(2, sender_account);
                    PreparedStatement credit = connection.prepareStatement("UPDATE account SET balance = balance + ? WHERE account_number = ?");
                    credit.setDouble(1, amount);
                    credit.setLong(2, receiver);

                    debit.executeUpdate();
                    credit.executeUpdate();
                    connection.commit();
                    System.out.println("✅ ₹" + amount + " transferred successfully!");
                } else System.out.println("❌ Insufficient balance!");
            } else System.out.println("❌ Invalid Pin!");

        } finally {
            connection.setAutoCommit(true);
        }
    }
}
