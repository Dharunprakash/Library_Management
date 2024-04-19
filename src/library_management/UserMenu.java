package library_management;

import java.util.Scanner;

public class UserMenu {
    private static final Scanner scanner = new Scanner(System.in);

    public static void display() {
        boolean logout = false;
        while (!logout) {
            System.out.println(Library.centerString("User Menu", 70, Library.ANSI_PURPLE));
            System.out.println(Library.ANSI_CYAN + "1. View Books");
            System.out.println(Library.ANSI_CYAN + "2. Search and Buy Books");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Logout" + Library.ANSI_RESET);
            System.out.print(Library.ANSI_YELLOW + "Enter your choice: " + Library.ANSI_RESET);
            int choice = scanner.nextInt();
            scanner.nextLine(); 
            
            switch (choice) {
                case 1:
                    UtilMethods.viewBooks();
                    break;
                case 2:
                  
                    break;
                case 3:
                   
                    break;
                case 4:
                 
                    break;
                case 5:
                    logout = true;
                    System.out.println(Library.ANSI_GREEN + "Logged out successfully." + Library.ANSI_RESET);
                    break;
                default:
                    System.out.println(Library.ANSI_RED + "Invalid choice. Please try again." + Library.ANSI_RESET);
            }
        }
    }
}
