package library_management;

import java.util.Scanner;

public class UserMenu {
    private static final Scanner scanner = new Scanner(System.in);

    public static void display() throws Exception {
        boolean logout = false;
        while (!logout) {
            System.out.println(Library.centerString("User Menu", 70, Library.PURPLE));
            DisplayTable.displayMenu("User Menu", new String[] { "1.View Books", "2.Search and Buy Books",
                    "3.Borrow Book", "4.Return Book", "5.Logout" });
            System.out.print(Library.YELLOW + "Enter your choice: " + Library.RESET);
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    BookingLogics.viewBooks();
                    break;
                case 2:
                    BookingLogics.searchAndBuyBook();
                    break;
                case 3:
                    BookingLogics.borrowBook();
                    break;
                case 4:
                    BookingLogics.returnBook();
                    break;
                case 5:
                    logout = true;
                    System.out.println(Library.GREEN + "Logged out successfully." + Library.RESET);
                    break;
                default:
                    System.out.println(Library.RED + "Invalid choice. Please try again." + Library.RESET);
            }
        }
    }
}
