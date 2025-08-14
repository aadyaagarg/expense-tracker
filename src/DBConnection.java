import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DBConnection {
    private static final String url = "jdbc:mysql://localhost:3306/expensedb";
    private static final String user = "root";
    private static final String password = "@9368Aadya";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}