package library_management;

public class AdminMenu {

    public static void display() throws Exception {
        boolean logout = false;
        while (!logout) {
            System.out.println(Library.centerString("Admin Menu", 70, Library.PURPLE));
          
            DisplayTable.displayMenu("Admin Menu", new String[] { "1.Add Book", "2.Edit Book", "3.Remove Book", "4.View Books",
                    "5.View Users", "6.Add User", "7.Logout" });
            System.out.print(Library.YELLOW + "Enter your choice: " + Library.RESET);
            int choice = Library.scanner.nextInt();
            Library.scanner.nextLine();
            switch (choice) {
                case 1:
                    AdminLogics.addBook();
                    break;
                case 2:
                    AdminLogics.editBook();
                    break;

                case 3:
                    AdminLogics.removeBook();
                    break;
                case 4:
                    BookingLogics.viewBooks();
                    break;
                case 5:
                    AdminLogics.viewUsers();
                    break;
                case 6:
                    AdminLogics.addUser();
                    break;
                case 7:
                    logout = true;
                    System.out.println(Library.GREEN + "Logged out successfully." + Library.RESET);
                    break;
                default:
                    System.out.println(Library.RED + "Invalid choice. Please try again." + Library.RESET);
            }
        }
    }
}
