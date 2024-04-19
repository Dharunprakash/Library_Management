package library_management;

import java.util.Scanner;

public class Library {
	static Scanner scanner = new Scanner(System.in);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_BLUE = "\u001B[34m";
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;
        boolean flag = true;
        while (flag) {
            System.out.println(centerString("Welcome to Library Management System", 70, ANSI_GREEN));

            System.out.println(ANSI_CYAN + "1. Login");
            System.out.println("2. Signup");
            System.out.println("3. Exit" + ANSI_RESET);

            System.out.print(ANSI_YELLOW + "Enter your choice: " + ANSI_RESET);
            int choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
				case 1:
				String role = login();
				if (role != null) {
					System.out.println(ANSI_GREEN + "Login successful.");
					if (role.equals("Admin")) {
						
					} else if (role.equals("Member")) {
                        UserMenu.display();
                        flag = false;
					}
				} else {
					System.out.println(ANSI_RED + "Password Not Match. Please try again." + ANSI_RESET);
				}
				break;
			
				case 2:
                    signup();
                    break;
                case 3:
                    System.out.println(ANSI_GREEN + "Exiting the system. Goodbye!" + ANSI_RESET);
                    System.exit(0);
                default:
                    System.out.println(ANSI_GREEN + "Invalid choice. Please try again." + ANSI_RESET);
            }
        }
    }
    public static String login() {
		System.out.println(centerString("LOGIN", 40, ANSI_PURPLE));
		System.out.print("Enter username: ");
		String username = scanner.nextLine();
		System.out.print("Enter password: ");
		String password = scanner.nextLine();
		return UserUtil.login(username, password);
    }

    public static void signup() {
		System.out.println(centerString("SIGNUP", 40, ANSI_PURPLE));
		System.out.print("Enter username: ");
		String username = scanner.nextLine();
		System.out.print("Enter password: ");
		String password = scanner.nextLine();
		UserUtil.signup(username, password);

    }
    public static String centerString(String s, int width, String color) {
        return String.format("%" + width + "s", color + s + ANSI_RESET);
    }
}
