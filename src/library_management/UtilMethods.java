package library_management;

import java.sql.*;

import java.util.*;


public class UtilMethods {
    private static final Scanner scanner = new Scanner(System.in);

    private static final String URL = "jdbc:mysql://localhost:3306/db";

    public static void viewBooks() {
        System.out.println("Genres:");
        try (Connection connection = DriverManager.getConnection(URL, "root", "root");
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT Genre FROM Books");
                ResultSet resultSet = preparedStatement.executeQuery()) {

            int index = 1;
            Map<Integer, String> genreMap = new HashMap<>();

            while (resultSet.next()) {
                String genre = resultSet.getString("Genre");
                System.out.println(index + ". " + genre);
                genreMap.put(index, genre);
                index++;
            }

            System.out.print("Enter the number of the genre you want to view: ");
            int selectedGenreIndex = scanner.nextInt();

            String selectedGenre = genreMap.get(selectedGenreIndex);
            if (selectedGenre != null) {
                // Call a method to display books of the selected genre
                displayBooksByGenre(selectedGenre);
            } else {
                System.out.println("Invalid selection. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayBooksByGenre(String genre) {
        // Define the border characters and colors
        String cyanColor = Library.ANSI_CYAN;
        String blueColor = Library.ANSI_BLUE;
        String resetColor = Library.ANSI_RESET;
    
        // Construct the header
        StringBuilder table = new StringBuilder();
        table.append(cyanColor).append("Books in genre: ").append(genre).append(resetColor).append("\n");
        table.append(blueColor).append(
                "+-------+----------------------------------------------------+------------------------------+-----------------+-----------------+\n");
        table.append("| ").append(cyanColor).append("S.No. ").append(blueColor).append("| ").append(cyanColor)
                .append("                     Book Name                     ").append(blueColor)
                .append("| ").append(cyanColor).append("            Author            ").append(blueColor)
                .append("| ").append(cyanColor).append(" Books Remaining ").append(blueColor)
                .append("| ").append(cyanColor).append("      Shelf      |\n").append(blueColor);
        table.append(
                "+-------+----------------------------------------------------+------------------------------+-----------------+-----------------+\n");
    
        // Fetch and add book details
        try (Connection connection = DriverManager.getConnection(URL, "root", "root");
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT * FROM Books WHERE Genre = ?")) {
            preparedStatement.setString(1, genre);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int index = 1;
                while (resultSet.next()) {
                    int bookID = resultSet.getInt("BookID");
                    String title = resultSet.getString("Title");
                    String author = resultSet.getString("Author");
                    int quantityAvailable = resultSet.getInt("QuantityAvailable");
                    String shelfLocation = resultSet.getString("ShelfLocation");
    
                    // Append book details to the table
                    table.append(blueColor).append("| ").append(cyanColor).append(String.format("%-5d ", index))
                            .append(blueColor).append("| ").append(cyanColor).append(String.format("%-50s ", title))
                            .append(blueColor).append("| ").append(cyanColor).append(String.format("%-28s ", author))
                            .append(blueColor).append("| ").append(cyanColor)
                            .append(String.format("%-15d ", quantityAvailable))
                            .append(blueColor).append("| ").append(cyanColor)
                            .append(String.format("%-15s ", shelfLocation))
                            .append(blueColor).append("|\n");
                    index++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        // Add the bottom border
        table.append(blueColor).append(
                "+-------+----------------------------------------------------+------------------------------+-----------------+-----------------+\n")
                .append(resetColor);
    
        // Print the constructed table
        System.out.println(table.toString());
    }
    }
