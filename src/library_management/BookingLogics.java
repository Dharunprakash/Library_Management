package library_management;

import java.sql.*;

import java.util.*;

public class BookingLogics {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String URL = "jdbc:mysql://localhost:3306/db";

    public static void viewBooks() {
        System.out.println(Library.Orange + "Genres:"+Library.BOLD);
    
        try (Connection connection = DriverManager.getConnection(URL, "root", "root");
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT Genre FROM Books");
                ResultSet resultSet = preparedStatement.executeQuery()) {
            int index = 1;
            Map<Integer, String> genreMap = new HashMap<>();
            while (resultSet.next()) {
                String genre = resultSet.getString("Genre");
                System.out.println(Library.GREEN + index + ". " + genre);
                genreMap.put(index, genre);
                index++;
            }
            
            System.out.print(Library.GREY+"Enter the number of the genre you want to view: "+Library.RESET);
            int selectedGenreIndex = scanner.nextInt();
            String selectedGenre = genreMap.get(selectedGenreIndex);
            try (PreparedStatement selectStatement = connection
                    .prepareStatement("SELECT * FROM Books WHERE Genre = ?")) {
                selectStatement.setString(1, selectedGenre);
                try (ResultSet selectedGenreResultSet = selectStatement.executeQuery()) {
                    if (!selectedGenreResultSet.isBeforeFirst()) {
                        System.out.println("No books found in the selected genre.");
                        return;
                    }
                    DisplayTable.dispalyResultSet(selectedGenreResultSet,
                            new String[] { "ID", "Title", "Author", "Available", "Shelf" },
                            new String[] { "BookId", "Title", "Author", "QuantityAvailable", "ShelfLocation" },
                            new int[] { 5, 50, 28, 15, 8 });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        scanner.nextLine();
    }

    public static void searchAndBuyBook() {
        System.out.print("Enter the title of the book you want to search: ");
        String titleToSearch = scanner.nextLine();

        try (Connection connection = DriverManager.getConnection(URL, "root", "root");
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT * FROM Books WHERE Title LIKE ?")) {
            preparedStatement.setString(1, "%" + titleToSearch + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    System.out.println("No books found with the given title.");
                    return;
                }
                System.out.println("Found books:");
                DisplayTable.dispalyResultSet(resultSet,
                        new String[] { "ID", "Title", "Author", "Available", "Shelf" },
                        new String[] { "BookId", "Title", "Author", "QuantityAvailable", "ShelfLocation" },
                        new int[] { 5, 50, 28, 15, 8 });

                System.out.print("1.Buy Book\n2.Exit\nEnter your choice:");
                if (scanner.nextInt() == 2) {
                    return;
                }
                System.out.print("Enter the book ID: ");

                int selectedBookIndex = scanner.nextInt();
                scanner.nextLine();
                try (PreparedStatement selectStatement = connection
                        .prepareStatement("SELECT * FROM Books WHERE BookID = ?")) {
                    selectStatement.setInt(1, selectedBookIndex);
                    try (ResultSet selectedBookResultSet = selectStatement.executeQuery()) {
                        if (selectedBookResultSet.next()) {
                            int selectedBookId = selectedBookResultSet.getInt("BookID");
                            String selectedBookTitle = selectedBookResultSet.getString("Title");
                            int selectedBookQuantity = selectedBookResultSet.getInt("QuantityAvailable");
                            if (selectedBookQuantity > 0) {
                                BookUtil.buyBook(Library.getUserId(), selectedBookId);
                            } else {
                                BookUtil.reserveBook(Library.getUserId(), selectedBookId);
                            }
                        } else {
                            System.out.println("Invalid book selection.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void borrowBook() {
        System.out.println("the books you have borrowed are:");
        try (Connection connection = DriverManager.getConnection(URL, "root", "root");
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "select Books.BookID, Books.Title, Books.Author, Transactions.TransactionDate, Transactions.DueDate from Books inner join Transactions on Books.BookID = Transactions.BookID where Transactions.UserID = ? and Transactions.ReturnDate is null")) {
            preparedStatement.setInt(1, Library.getUserId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    System.out.println("No books borrowed.");
                    return;
                }
                DisplayTable.dispalyResultSet(resultSet,
                        new String[] { "ID", "Title", "Author", "Borrowed Date", "Due Date" },
                        new String[] { "BookID", "Title", "Author", "TransactionDate", "DueDate" },
                        new int[] { 5, 50, 28,
                                20, 15 });
                System.out.print("if you want to return the book press 1 else exit press 2:");
                int choice = scanner.nextInt();
                if (choice == 2) {
                    return;
                }
                System.out.print("Enter the book ID: ");
                int selectedBookIndex = scanner.nextInt();
                scanner.nextLine();
                BookUtil.borrowBook(Library.getUserId(), selectedBookIndex);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public static void returnBook() {
        System.out.println("the books you have returned are:");
        try (Connection connection = DriverManager.getConnection(URL, "root", "root");
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "select Books.BookID, Books.Title, Books.Author, Transactions.TransactionDate, Transactions.ReturnDate from Books inner join Transactions on Books.BookID = Transactions.BookID where Transactions.UserID = ? and Transactions.ReturnDate is not null")) {
            preparedStatement.setInt(1, Library.getUserId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    System.out.println("No books returned.");
                    return;
                }
                DisplayTable.dispalyResultSet(resultSet,
                        new String[] { "ID", "Title", "Author", "Borrowed Date", "Return Date" },
                        new String[] { "BookID", "Title", "Author", "TransactionDate", "ReturnDate" },
                        new int[] { 5, 50, 28,
                                20, 18 });
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }
}
