package src;
import java.sql.*;
public class JDBC {
    public static void closeResources(Connection conn, Statement stmt) {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();  // Properly close connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() {
        try {
            final  String URL = "jdbc:mysql://127.0.0.1:3306/sample_schema";
            final  String USER = "root";
            final  String PASSWORD = "wpc";
            return DriverManager.getConnection(URL,USER,PASSWORD);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void main(String[] args) {
        getConnection();


    }

}
