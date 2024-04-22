package library_management;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class BookUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/db";
    private static final Scanner scanner = new Scanner(System.in);
    

    private static boolean checkAvailableBooks(int bookId) {
        try (Connection connection = DriverManager.getConnection(URL, "root", "root");
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT (SELECT QuantityAvailable FROM Books WHERE BookID = ?) - (SELECT COUNT(*) FROM Reservations WHERE BookID = ? AND Status = 'Pending') AS RemainingBooks")) {
            preparedStatement.setInt(1, bookId);
            preparedStatement.setInt(2, bookId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int remainingBooks = resultSet.getInt("RemainingBooks");
                    return remainingBooks > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void createTransaction(int userId, int bookId, String transactionType, Date dueDate) {
        try (Connection connection = DriverManager.getConnection(URL, "root", "root");
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Transactions (UserID, BookID, TransactionDate, DueDate,reservation_available_date) VALUES (?, ?,?, ?, ?)")) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, bookId);
            // preparedStatement.setDate(3, new java.sql.Date(Library.currentDate.getTime()));
            preparedStatement.setTimestamp(3, new java.sql.Timestamp(Library.currentDate.getTime()));
            preparedStatement.setDate(4, new java.sql.Date(dueDate.getTime()));
            preparedStatement.setDate(5, new java.sql.Date(dueDate.getTime()));
            preparedStatement.executeUpdate();
            System.out.println("Transaction record created successfully!");
            
            try (PreparedStatement updateStatement = connection.prepareStatement(
                    "UPDATE Books SET QuantityAvailable = QuantityAvailable - 1 WHERE BookID = ?")) {
                updateStatement.setInt(1, bookId);
                updateStatement.executeUpdate();
                System.out.println("Book quantity updated successfully!");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createReservation(int userId, int bookId) {
        try (Connection connection = DriverManager.getConnection(URL, "root", "root")) {
            Date pickupDate = getNearestReturnDate(connection);
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Reservations (UserID, BookID, ReservationDate, PickupDate, Status) VALUES (?, ?, ?, ?, 'Pending')")) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, bookId);
                preparedStatement.setTimestamp(3, new java.sql.Timestamp(Library.currentDate.getTime()));
                preparedStatement.setTimestamp(4, new java.sql.Timestamp(pickupDate.getTime()));
                preparedStatement.executeUpdate();
                System.out.println("Reservation record created successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static Date getNearestReturnDate(Connection connection) throws SQLException {
        Date nearestReturnDate = null;
        String query = "SELECT reservation_available_date, TransactionID FROM Transactions WHERE reservation_available_date = (SELECT MIN(reservation_available_date) FROM Transactions WHERE ReturnDate IS NULL)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                nearestReturnDate = resultSet.getDate(1);
                try (PreparedStatement updateStatement = connection.prepareStatement(
                        "UPDATE Transactions SET reservation_available_date = ? WHERE TransactionID = ?")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(nearestReturnDate);
                    calendar.add(Calendar.DATE, 14);
                    updateStatement.setDate(1, new java.sql.Date(calendar.getTime().getTime()));
                    updateStatement.setInt(2, resultSet.getInt(2));
                    updateStatement.executeUpdate();
                    System.out.println("Reservation date updated successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return nearestReturnDate;
    }

    public static void borrowBook(int userId, int bookId) {
        try (Connection connection = DriverManager.getConnection(URL, "root", "root");
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Transactions SET ReturnDate = ? WHERE UserID = ? AND BookID = ? AND ReturnDate IS NULL")) {
            preparedStatement.setTimestamp(1, new java.sql.Timestamp(Library.currentDate.getTime()));
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3, bookId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Book returned successfully!");
                try (PreparedStatement updateStatement = connection.prepareStatement(
                        "UPDATE Books SET QuantityAvailable = QuantityAvailable + 1 WHERE BookID = ?")) {
                    updateStatement.setInt(1, bookId);
                    updateStatement.executeUpdate();
                    System.out.println("Book quantity updated successfully!");
                }
            } else {
                System.out.println("No book found to return.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    private static Date calculateDueDate(int borrowingDuration) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Library.currentDate);
        calendar.add(Calendar.DATE, borrowingDuration);
        return calendar.getTime();
    }
    public static void buyBook(int userId, int bookId) {
        if (checkAvailableBooks(bookId)) {
            System.out.println("Book is available for borrowing.");
            createTransaction(userId, bookId, "Borrow", calculateDueDate(14));  
        } else {
            createReservation(userId, bookId);
        }
    }
    public static void reserveBook(int userId, int bookId) {
        createReservation(userId, bookId);
    }
}
