import java.sql.*;
import java.util.Scanner;

public class CategoryService {
    public void addCategory() {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO categories(category_name)VALUES(?)");) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter category name: ");
            ps.setString(1, sc.nextLine());
            ps.executeUpdate();
            System.out.println("Category added successfully!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteCategory() {
        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT * FROM categories");
            while (rs.next()) {
                System.out.println(rs.getInt("category_id") + " - " + rs.getString("category_name"));
            }
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter category ID to delete: ");
            int id = sc.nextInt();
            PreparedStatement ps = con.prepareStatement("DELETE FROM categories WHERE category_id=?");
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Category deleted successfully!");
            } else {
                System.out.println("Category not found!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
