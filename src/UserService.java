import java.sql.*;
import java.util.Scanner;

public class UserService {
    public void addUser() {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO users(name,email)VALUES(?,?)");) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter the name: ");
            ps.setString(1, sc.nextLine());
            System.out.print("Enter the email: ");
            ps.setString(2, sc.nextLine());
            ps.executeUpdate();
            System.out.println("User added successfully!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteUser() {
        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                System.out.println(rs.getInt("user_id") + " - " + rs.getString("name"));
            }
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter user ID to delete: ");
            int id = sc.nextInt();
            PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE user_id=?");
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("User not found!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateUser() {
        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " - " + rs.getString(2) + " - " + rs.getString(3));
            }
            System.out.print("Enter user ID to update: ");
            Scanner sc = new Scanner(System.in);
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter the updated name of the user: ");
            String name = sc.nextLine();
            System.out.print("Enter the updated email: ");
            String email = sc.nextLine();
            PreparedStatement ps = con.prepareStatement("UPDATE users SET name=?,email=? WHERE user_id=?");
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setInt(3, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("User updated successfully!");
            } else {
                System.out.println("User not found!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
