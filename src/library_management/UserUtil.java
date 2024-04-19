// UserUtil.java
package library_management;

import java.sql.*;

public class UserUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/db";

    public static String login(String username, String password) {
        try (Connection connection = DriverManager.getConnection(URL, "root", "root")) {
            password = String.valueOf(password.hashCode());
            String query = "SELECT Role FROM Users WHERE Username = ? AND Password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("Role");
                    } else {
                        // User not found
                        return null;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void signup(String username, String password) {
        try (Connection connection = DriverManager.getConnection(URL, "root", "root")) {
            
            if (usernameExists(connection, username)) {
                System.out.println(Library.ANSI_RED + "Username already exists. Please try again." + Library.ANSI_RESET);
                return;
            }
            String query = "INSERT INTO Users (Username, Password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, String.valueOf(password.hashCode()));
                preparedStatement.executeUpdate();
                System.out.println(Library.ANSI_GREEN + "Signup successful. Please login to continue." + Library.ANSI_RESET);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean usernameExists(Connection connection, String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM Users WHERE Username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; 
                }
            }
        }
        return false;
    }
    
}
