import java.sql.*;
import java.util.Scanner;

public class USER {

    private Connection connection;
    private Scanner scanner;

    public USER(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // REGISTER NEW USER
    public void register() {
        System.out.print("Full Name: ");
        String full_name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (user_exists(email)) {
            System.out.println("❌ User Already Exists! Use Different Email.");
            return;
        }

        String query = "INSERT INTO user(full_name, email, password) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, full_name);
            ps.setString(2, email);
            ps.setString(3, password);
            int rows = ps.executeUpdate();

            if (rows > 0) System.out.println("✅ Registration Successful!");
            else System.out.println("❌ Registration Failed!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // LOGIN USER
    public String login() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return email;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // CHECK IF USER EXISTS
    public boolean user_exists(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
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
