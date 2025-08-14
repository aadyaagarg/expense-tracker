import java.sql.*;
import java.util.Scanner;

public class ExpenseTracker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Expense Tracker ---");
            System.out.println("1. Add User");
            System.out.println("2. Add Category");
            System.out.println("3. Add Expense");
            System.out.println("4. View Expenses");
            System.out.println("5. View total by user and category");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> addUser();
                case 2 -> addCategory();
                case 3 -> addExpense();
                case 4 -> viewExpenses();
                case 5 -> totalByUserCategory();
                case 6 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    static void addUser() {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO users(name,email)VALUES(?,?)");) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter the name: ");
            ps.setString(1, sc.nextLine());
            System.out.print("Enter the email: ");
            ps.setString(2, sc.nextLine());
            ps.executeUpdate();
            System.out.println("User added successfully!");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void addCategory() {
        try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement("INSERT INTO categories(category_name)VALUES(?)");) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter the name of the category: ");
            ps.setString(1, sc.nextLine());
            ps.executeUpdate();
            System.out.println("Category added successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void addExpense() {
        try (Connection con = DBConnection.getConnection();) {
            Scanner sc = new Scanner(System.in);
            System.out.println("\nAvailable Users: ");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                System.out.println(rs.getInt("user_id") + " - " + rs.getString("name"));
            }
            System.out.print("Enter User ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.println("\nAvailable Categories: ");
            Statement cst = con.createStatement();
            ResultSet crs = cst.executeQuery("SELECT * FROM categories");
            while (crs.next()) {
                System.out.println(crs.getInt("category_id") + " - " + crs.getString("category_name"));
            }
            System.out.print("Enter Category ID: ");
            int cid = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter Description: ");
            String des = sc.nextLine();
            System.out.print("Enter Amount: ");
            double amt = sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter Date(YYYY-MM-DD): ");
            String date = sc.nextLine();
            PreparedStatement ps = con.prepareStatement("INSERT INTO expenses(user_id,category_id,description,amount,date)VALUES(?,?,?,?,?)");
            ps.setInt(1, id);
            ps.setInt(2, cid);
            ps.setString(3, des);
            ps.setDouble(4, amt);
            ps.setString(5, date);
            ps.executeUpdate();
            System.out.println("Expense added successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void viewExpenses() {
        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT e.expense_id, u.name, c.category_name, e.description, e.amount, e.date FROM expenses e JOIN users u ON e.user_id = u.user_id JOIN categories c ON e.category_id = c.category_id ORDER BY e.date DESC");) {
            System.out.println("\nID | User | Category | Description | Amount | Date");
            while (rs.next()) {
                System.out.printf("%d | %s | %s | %s | %.2f | %s%n", rs.getInt("expense_id"), rs.getString("name"), rs.getString("category_name"), rs.getString("description"), rs.getDouble("amount"), rs.getDate("date").toString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void totalByUserCategory() {
        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT u.name, c.category_name, SUM(e.amount) AS total " + "FROM expenses e " + "JOIN users u ON e.user_id = u.user_id " + "JOIN categories c ON e.category_id = c.category_id " + "GROUP BY u.name, c.category_name")) {

            System.out.println("\nUser | Category | Total Amount");
            while (rs.next()) {
                System.out.printf("%s | %s | %.2f%n", rs.getString("name"), rs.getString("category_name"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
