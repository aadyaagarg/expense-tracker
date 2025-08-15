import java.util.Scanner;

public class ExpenseTracker {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        CategoryService categoryService = new CategoryService();
        ExpenseService expenseService = new ExpenseService();
        while (true) {
            System.out.println("\n--- Expense Tracker ---");
            System.out.println("1. Add User");
            System.out.println("2. Update User");
            System.out.println("3. Add Category");
            System.out.println("4. Add Expense");
            System.out.println("5. View All Expenses");
            System.out.println("6. View All Expenses of a User");
            System.out.println("7. Monthly Summary of a User");
            System.out.println("8. Update Expense");
            System.out.println("9. Delete User");
            System.out.println("10. Delete Category");
            System.out.println("11. Delete Expense");
            System.out.println("12. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> userService.addUser();
                case 2 -> userService.updateUser();
                case 3 -> categoryService.addCategory();
                case 4 -> expenseService.addExpense();
                case 5 -> expenseService.viewAllExpenses();
                case 6 -> expenseService.viewExpensesForUser();
                case 7 -> expenseService.monthlySummaryForUser();
                case 8 -> expenseService.updateExpense();
                case 9 -> userService.deleteUser();
                case 10 -> categoryService.deleteCategory();
                case 11 -> expenseService.deleteExpense();
                case 12 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}
