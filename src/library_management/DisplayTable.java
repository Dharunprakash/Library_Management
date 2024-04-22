package library_management;

import java.sql.*;
import java.util.Scanner;

public class DisplayTable {

    private static final String URL = "jdbc:mysql://localhost:3306/db";

    // public static void displayBooksByGenre(String genre) {

    // String cyanColor = Library.CYAN;
    // String blueColor = Library.BLUE;
    // String resetColor = Library.RESET;

    // StringBuilder table = new StringBuilder();
    // table.append(cyanColor).append("Books in genre:
    // ").append(genre).append(resetColor).append("\n");
    // table.append(blueColor).append(
    // "+-------+----------------------------------------------------+------------------------------+-----------------+----------+\n");
    // table.append("| ").append(cyanColor).append("S.No.
    // ").append(blueColor).append("| ").append(cyanColor)
    // .append(" Book Name ").append(blueColor)
    // .append("| ").append(cyanColor).append(" Author ")
    // .append(blueColor)
    // .append("| ").append(cyanColor).append(" Books Remaining").append(blueColor)
    // .append("| ").append(cyanColor).append(" Shelf |\n");
    // table.append(
    // "+-------+----------------------------------------------------+------------------------------+-----------------+----------+\n");
    // try (Connection connection = DriverManager.getConnection(URL, "root",
    // "root");
    // PreparedStatement preparedStatement = connection
    // .prepareStatement("SELECT * FROM Books WHERE Genre = ?")) {
    // preparedStatement.setString(1, genre);
    // try (ResultSet resultSet = preparedStatement.executeQuery()) {
    // int index = 1;
    // while (resultSet.next()) {
    // int bookID = resultSet.getInt("BookID");
    // String title = resultSet.getString("Title");
    // String author = resultSet.getString("Author");
    // int quantityAvailable = resultSet.getInt("QuantityAvailable");
    // String shelfLocation = resultSet.getString("ShelfLocation");
    // table.append(cyanColor).append("| ").append(resetColor)
    // .append(String.format("%-5d ", index))
    // .append(cyanColor).append("| ").append(resetColor)
    // .append(String.format("%-50s ", title))
    // .append(cyanColor).append("| ").append(resetColor)
    // .append(String.format("%-28s ", author))
    // .append(cyanColor).append("| ").append(resetColor)
    // .append(String.format("%-15d ", quantityAvailable))
    // .append(cyanColor).append("| ").append(resetColor)
    // .append(String.format("%-8s ", shelfLocation)).append(cyanColor)
    // .append("|\n");
    // }
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }

    // // Add the bottom border
    // table.append(blueColor).append(
    // "+-------+----------------------------------------------------+------------------------------+-----------------+----------+\n")
    // .append(resetColor);

    // // Print the constructed table
    // System.out.println(table.toString());
    // }

    public static void dispalyResultSet(ResultSet resultSet, String[] columnNames, String[] columnValues,
            int[] columnWidths) {
        if (resultSet == null || columnNames == null || columnNames.length == 0
                || columnValues == null || columnValues.length == 0
                || columnWidths == null || columnWidths.length == 0
                || columnNames.length != columnValues.length || columnNames.length != columnWidths.length) {
            System.out.println(
                    "Invalid input. ResultSet, column names, column values, and column widths must be provided and have the same length.");
            return;
        }

        StringBuilder table = new StringBuilder();
        String cyanColor = Library.CYAN;
        String blueColor = Library.BLUE;
        String resetColor = Library.RESET;
        String greenColor = Library.GREEN;

        table.append(greenColor).append("Search Results:").append(resetColor).append("\n");

        // Construct the header
        table.append(blueColor).append("+------+");
        for (int i = 0; i < columnNames.length; i++) {
            int width = columnWidths[i];
            table.append("-".repeat(width + 2)).append("+");
        }
        table.append("\n");

        // Add S.No. column
        table.append("| ").append(cyanColor).append(String.format("%-5s", "S.No."));

        for (int i = 0; i < columnNames.length; i++) {
            String columnName = columnNames[i];
            int width = columnWidths[i];
            table.append("| ").append(cyanColor).append(String.format("%-" + width + "s", columnName)).append(" ");
        }
        table.append("|\n");

        // Construct the separator line
        table.append(blueColor).append("+------+");
        for (int i = 0; i < columnNames.length; i++) {
            int width = columnWidths[i];
            table.append("-".repeat(width + 2)).append("+");
        }
        table.append("\n");

        // Construct the rows
        try {
            int rowNum = 1;
            while (resultSet.next()) {
                // Add S.No.
                table.append("| ").append(resetColor).append(String.format("%-5d", rowNum));
                rowNum++;

                for (int i = 0; i < columnValues.length; i++) {
                    String columnValue = resultSet.getString(columnValues[i]);
                    int width = columnWidths[i];
                    table.append(cyanColor).append("| ").append(resetColor)
                            .append(String.format("%-" + width + "s", columnValue))
                            .append(" ");
                }
                table.append(cyanColor).append("|\n");
                table.append("+------+");
                for (int i = 0; i < columnNames.length; i++) {
                    int width = columnWidths[i];
                    table.append("-".repeat(width + 2)).append("+");
                }
                table.append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table.append(resetColor);

        System.out.println(table.toString());
    }

    public static void displayMenu(String header, String[] options) throws Exception {
        String cyanColor = Library.CYAN;
        String blueColor = Library.BLUE;
        String resetColor = Library.RESET;
        String purpleColor = Library.PURPLE;    
        System.out.println();
      
        StringBuilder table = new StringBuilder();
        table.append(blueColor).append("+--------------------------+\n");
        table.append("| ").append(purpleColor).append(String.format("%-34s",  Library.centerString(header, 27, purpleColor))).append(blueColor).append("|\n");
        table.append(blueColor).append("+--------------------------+\n");
    
        for (String option : options) {
            table.append("| ").append(resetColor).append(String.format("%-25s", option)).append(blueColor)
                    .append("|\n");
        }
    
        table.append("+--------------------------+\n");
        table.append(resetColor);
        System.out.println(table.toString());
    }
       
}