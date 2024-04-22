// UserUtil.java
package library_management;

import java.sql.*;

public class UserUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/db";

    public static String[] login(String username, String password) {
        try (Connection connection = DriverManager.getConnection(URL, "root", "root")) {
            // Authenticate the user
            password = String.valueOf(password.hashCode());
            String query = "SELECT UserID, Role FROM Users WHERE Username = ? AND Password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Return the user ID and role
                        System.out.println(resultSet.getString("Role"));
                        return new String[] { Integer.toString(resultSet.getInt("UserID")),
                                resultSet.getString("Role") };
                    } else {
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
                System.out.println(Library.RED + "Username already exists. Please try again." + Library.RESET);
                return;
            }
            String query = "INSERT INTO Users (Username, Password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, String.valueOf(password.hashCode()));
                preparedStatement.executeUpdate();
                System.out.println(Library.GREEN + "Signup successful. Please login to continue." + Library.RESET);
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
