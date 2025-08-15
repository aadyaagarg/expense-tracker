import java.sql.*;
import java.util.Scanner;

public class ExpenseService {
    public void addExpense() {
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

    public void viewAllExpenses() {
        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement();) {
            ResultSet rs = st.executeQuery("SELECT e.expense_id, u.name, c.category_name, e.description, e.amount, e.date FROM expenses e JOIN users u ON e.user_id = u.user_id JOIN categories c ON e.category_id = c.category_id ORDER BY e.date DESC");
            System.out.println("\nID | User | Category | Description | Amount | Date");
            while (rs.next()) {
                System.out.printf("%d | %s | %s | %s | %.2f | %s%n", rs.getInt("expense_id"), rs.getString("name"), rs.getString("category_name"), rs.getString("description"), rs.getDouble("amount"), rs.getDate("date").toString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void viewExpensesForUser() {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();) {
            Scanner sc = new Scanner(System.in);
            ResultSet rs = st.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " - " + rs.getString(2));
            }
            System.out.print("Enter user ID: ");
            int id = sc.nextInt();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT e.expense_id, c.category_name, e.description, e.amount, e.date " +
                            "FROM expenses e " +
                            "JOIN categories c ON e.category_id = c.category_id " +
                            "WHERE e.user_id = ? ORDER BY e.date DESC");
            ps.setInt(1, id);
            ResultSet urs = ps.executeQuery();

            while (urs.next()) {
                System.out.printf("%d | %s | %s | %.2f | %s%n",
                        urs.getInt(1), urs.getString(2), urs.getString(3),
                        urs.getDouble(4), urs.getDate(5));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void monthlySummaryForUser() {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {
            Scanner sc = new Scanner(System.in);
            ResultSet rs = st.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                System.out.printf("%d - %s%n", rs.getInt(1), rs.getString(2));
            }
            System.out.print("Enter user ID: ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter month (YYYY-MM): ");
            String month = sc.nextLine();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT c.category_name, SUM(e.amount) AS total " +
                            "FROM expenses e " +
                            "JOIN categories c ON e.category_id = c.category_id " +
                            "WHERE e.user_id = ? AND e.date LIKE ? " +
                            "GROUP BY c.category_name");
            ps.setInt(1, id);
            ps.setString(2, month + "%");

            ResultSet urs = ps.executeQuery();
            double grandTotal = 0;
            System.out.println("\nCategory | Total");
            while (urs.next()) {
                System.out.printf("%s - %.2f%n", urs.getString(1), urs.getDouble(2));
                grandTotal += urs.getDouble(2);
            }
            System.out.println("Grand Total: " + grandTotal);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateExpense() {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(
                    "SELECT e.expense_id, u.name, c.category_name, e.description, e.amount, e.date " +
                            "FROM expenses e " +
                            "JOIN users u ON e.user_id = u.user_id " +
                            "JOIN categories c ON e.category_id = c.category_id " +
                            "ORDER BY e.date DESC");
            while (rs.next()) {
                System.out.printf("%d | %s | %s | %s | %.2f | %s%n",
                        rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getDouble(5), rs.getDate(6));
            }

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter Expense ID to update: ");
            int eid = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter new description: ");
            String desc = sc.nextLine();
            System.out.print("Enter new amount: ");
            double amount = sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter new date (YYYY-MM-DD): ");
            String date = sc.nextLine();
            ResultSet crs = st.executeQuery("SELECT * FROM categories");
            while (crs.next()) {
                System.out.printf("%d - %s%n", crs.getInt(1), crs.getString(2));
            }
            System.out.print("Enter new category ID: ");
            int cid = sc.nextInt();

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE expenses SET description=?, amount=?, date=?, category_id=? WHERE expense_id=?");
            ps.setString(1, desc);
            ps.setDouble(2, amount);
            ps.setDate(3, Date.valueOf(date));
            ps.setInt(4, cid);
            ps.setInt(5, eid);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Expense updated successfully!");
            } else {
                System.out.println("Invalid Expense ID or Category ID!");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void deleteExpense() {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(
                    "SELECT e.expense_id, u.name, c.category_name, e.description, e.amount, e.date " +
                            "FROM expenses e " +
                            "JOIN users u ON e.user_id = u.user_id " +
                            "JOIN categories c ON e.category_id = c.category_id " +
                            "ORDER BY e.date DESC");
            while (rs.next()) {
                System.out.printf("%d | %s | %s | %s | %.2f | %s%n",
                        rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getDouble(5), rs.getDate(6));
            }

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter Expense ID to delete: ");
            int id = sc.nextInt();

            PreparedStatement ps = con.prepareStatement("DELETE FROM expenses WHERE expense_id=?");
            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Expense deleted successfully!");
            } else {
                System.out.println("Expense ID not found!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


//    public void totalByUserCategory() {
//        try (Connection con = DBConnection.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT u.name, c.category_name, SUM(e.amount) AS total " + "FROM expenses e " + "JOIN users u ON e.user_id = u.user_id " + "JOIN categories c ON e.category_id = c.category_id " + "GROUP BY u.name, c.category_name")) {
//
//            System.out.println("\nUser | Category | Total Amount");
//            while (rs.next()) {
//                System.out.printf("%s | %s | %.2f%n", rs.getString("name"), rs.getString("category_name"), rs.getDouble("total"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
