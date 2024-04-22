package library_management;

import java.sql.*;
import java.util.*;

public class AdminLogics{

    public static final String URL = "jdbc:mysql://localhost:3306/db";
    static Scanner scanner = new Scanner(System.in);

    public static void addBook() throws InterruptedException{
        Library.animateWelcomeMessage("Adding a new Book...", 25, Library.Orange, 5);
        System.out.print(Library.GREY);
        System.out.print("Enter title: "+Library.RESET);
        String title = scanner.nextLine();
        System.out.print("Enter author: "+Library.RESET);
        String author = scanner.nextLine();
        System.out.print("Enter genre: "+Library.RESET);
        String genre = scanner.nextLine();
        System.out.print("Enter publisher: "+   Library.RESET);
        String publisher = scanner.nextLine();
        System.out.print("Enter quantity available: "+Library.RESET);
        int quantityAvailable = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter shelf location: "+Library.RESET);
        String shelfLocation = scanner.nextLine();
        System.out.print(Library.RESET);
        try (Connection connection = DriverManager.getConnection(URL, "root", "root");
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Books (Title, Author, Genre, Publisher, QuantityAvailable, ShelfLocation) VALUES (?, ?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            preparedStatement.setString(3, genre);
            preparedStatement.setString(4, publisher);
            preparedStatement.setInt(5, quantityAvailable);
            preparedStatement.setString(6, shelfLocation);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(Library.GREEN + "Book added successfully!" + Library.RESET);
            } else {
                System.out.println(Library.RED + "Failed to add the book. Please try again." + Library.RESET);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeBook() {
       
        System.out.print(Library.GREY+"Enter the title of the book to remove: "+Library.RESET);
        String title = scanner.nextLine();
        try (Connection connection = DriverManager.getConnection(URL, "root", "root");
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT * FROM Books WHERE Title LIKE ?")) {
            preparedStatement.setString(1, "%" + title + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    System.out.println(Library.RED+"No books found with the given title."+Library.RESET);
                    return;
                }
                System.out.println(Library.BOLD+"Found books:");
                DisplayTable.dispalyResultSet(resultSet,
                        new String[] { "ID", "Title", "Author", "Available", "Shelf" },
                        new String[] { "BookId", "Title", "Author", "QuantityAvailable", "ShelfLocation" },
                        new int[] { 5, 50, 28, 15, 8 });
    
                System.out.print("1.Remove Book\n2.Exit\nEnter your choice:");
                int choice = scanner.nextInt();
                scanner.nextLine(); 
                if (choice == 2) {
                    return;
                }
    
                System.out.print("Enter the book ID: ");
                int selectedBookId = scanner.nextInt();
                scanner.nextLine(); 
                if (isBookInTransactions(connection, selectedBookId)) {
                    System.out.println(Library.RED+"This book is currently involved in transactions and cannot be removed."+Library.RESET);
                    return;
                }
    
                try (PreparedStatement deleteStatement = connection
                        .prepareStatement("DELETE FROM Books WHERE BookID = ?")) {
                    deleteStatement.setInt(1, selectedBookId);
                    int rowsAffected = deleteStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println(Library.GREEN + "Book removed successfully!" + Library.RESET);
                    } else {
                        System.out.println(Library.RED + "Failed to remove the book. Please try again." + Library.RESET);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean isBookInTransactions(Connection connection, int bookId) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Transactions WHERE BookID = ?")) {
            preparedStatement.setInt(1, bookId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
    
    public static void editBook() {
        System.out.println("Editing a book...");
        System.out.print("Enter the title of the book to edit: ");
        String title = scanner.nextLine();
        try (Connection connection = DriverManager.getConnection(URL, "root", "root");
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT * FROM Books WHERE Title LIKE ?")) {
            preparedStatement.setString(1, "%" + title + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    System.out.println("No books found with the given title.");
                    return;
                }
    
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
    
                System.out.println("Found books:");
                DisplayTable.dispalyResultSet(resultSet,
                        new String[] { "ID", "Title", "Author", "Available", "Shelf" },
                        new String[] { "BookId", "Title", "Author", "QuantityAvailable", "ShelfLocation" },
                        new int[] { 5, 50, 28, 15, 8 });
                System.out.println("Enter ID of the book you want to edit:");
                int id = scanner.nextInt();
                scanner.nextLine(); // Consume newline
    
                System.out.println("Select the column to edit:");
                for (int i = 2; i <= columnCount; i++) { // Start from 2 to skip BookID column
                    System.out.println(i - 1 + ". " + metaData.getColumnName(i));
                }
    
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
    
                if (choice < 1 || choice >= columnCount) {
                    System.out.println("Invalid choice.");
                    return;
                }
    
                System.out.print("Enter new value for " + metaData.getColumnName(choice + 1) + ": ");
                String newValue = scanner.nextLine();
    
                String columnName = metaData.getColumnName(choice + 1);
                String updateQuery = "UPDATE Books SET " + columnName + " = ? WHERE BookId = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setString(1, newValue);
                    updateStatement.setInt(2, id);
                    int rowsAffected = updateStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println(Library.GREEN + "Book updated successfully!" + Library.RESET);
                    } else {
                        System.out.println(Library.RED + "Failed to update the book. Please try again." + Library.RESET);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void viewUsers() {
        System.out.println("Viewing users...");
        try (Connection connection = DriverManager.getConnection(URL, "root", "root");
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users");
                ResultSet resultSet = preparedStatement.executeQuery()) {
            DisplayTable.dispalyResultSet(resultSet, new String[] { "ID", "Username", "Role" },
                    new String[] { "UserID", "Username", "Role" }, new int[] { 5, 20, 10 });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addUser() {
        System.out.println("Adding a new user...");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter role: ");
        String role = scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(URL, "root", "root")) {
            if (usernameExists(connection, username)) {
                System.out.println(
                        Library.RED + "Username already exists. Please choose a different username." + Library.RESET);
                return;
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Users (Username, Password, Role) VALUES (?, ?, ?)")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password.hashCode() + "");
                preparedStatement.setString(3, role);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println(Library.GREEN + "User added successfully!" + Library.RESET);
                } else {
                    System.out.println(Library.RED + "Failed to add the user. Please try again." + Library.RESET);
                }
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
