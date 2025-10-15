import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public long open_account(String email) {
        if (!account_exists(email)) {
            String query = "INSERT INTO account(account_number, full_name, email, balance, security_pin) VALUES (?, ?, ?, ?, ?)";
            System.out.println("Full Name: ");
            String full_name = scanner.next();
            System.out.println("Initial Balance: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Security Pin (4 digits): ");
            String security_pin = scanner.nextLine();

            try {
                long account_number = generateAccountNumber();
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setLong(1, account_number);
                ps.setString(2, full_name);
                ps.setString(3, email);
                ps.setDouble(4, balance);
                ps.setString(5, security_pin);
                int rows = ps.executeUpdate();

                if (rows > 0) return account_number;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("❌ Account Already Exists!");
    }

    public long getAccount_number(String email) {
        String query = "SELECT account_number FROM account WHERE email = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getLong("account_number");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("❌ Account Not Found!");
    }

    private long generateAccountNumber() {
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT account_number FROM account ORDER BY account_number DESC LIMIT 1");
            if (rs.next()) return rs.getLong("account_number") + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 51001000;
    }

    public boolean account_exists(String email) {
        String query = "SELECT account_number FROM account WHERE email = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
