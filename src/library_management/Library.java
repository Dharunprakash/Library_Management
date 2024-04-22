package library_management;

import java.sql.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Library {
    static Scanner scanner = new Scanner(System.in);
    public static final String RESET = "\u001B[0m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String PURPLE = "\u001B[35m";
    public static final String BLUE = "\u001B[34m";
    public static final String WHITE = "\u001B[37m";
    public static final String BLACK = "\u001B[30m";
    public static final String GREY = "\u001B[90m";
    public static final String LIGHT_GREEN = "\u001B[92m";
    public static final String LIGHT_BLUE = "\u001B[94m";
    public static final String Orange = "\u001B[38;5;208m";
    public static final String BOLD = "\u001B[1m";
    public static final String ITALIC = "\u001B[3m";
    public static final String UNDERLINE = "\u001B[4m";

    private static int userId;

    public static Date currentDate = new Date(System.currentTimeMillis());

    public static void animateWelcomeMessage(String s, int ws, String color, int delay) throws InterruptedException {
        String message = centerString(s, s.length() + ws,
                color);
        for (int i = 0; i < ws; i++) {
            System.out.print(message.charAt(i));
            TimeUnit.MILLISECONDS.sleep(10);
        }

        for (int i = ws; i < message.length(); i++) {
            System.out.print(message.charAt(i));
            TimeUnit.MILLISECONDS.sleep(delay);
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;
        boolean flag = true;

        while (true) {

            animateWelcomeMessage("Welcome to Library Management System", 35, GREEN, 50);
            animateWelcomeMessage("1. Login", 0, CYAN, 10);
            animateWelcomeMessage("2. Signup", 0, CYAN, 10);
            animateWelcomeMessage("3. Exit", 0, CYAN, 10);
            System.out.print(YELLOW + "Enter your choice: " + RESET);

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    String[] role = login();
                    if (role != null) {
                        System.out.println(GREEN + "Login successful.");
                        userId = Integer.parseInt(role[0]);
                        if (role[1].equals("Admin")) {
                            AdminMenu.display();
                        } else {
                            UserMenu.display();
                        }
                    } else {
                        System.out.println(RED + "Invalid username or password. Please try again." + RESET);
                    }
                    break;

                case 2:
                    signup();
                    break;
                case 3:
                    System.out.println(GREEN + "Exiting the system. Goodbye!" + RESET);
                    System.exit(0);
                default:
                    System.out.println(GREEN + "Invalid choice. Please try again." + RESET);
            }
        }
    }

    public static String[] login() {
        System.out.println(centerString("LOGIN", 40, PURPLE));
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        return UserUtil.login(username, password);
    }

    public static void signup() {
        System.out.println(centerString("SIGNUP", 40, PURPLE));
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        UserUtil.signup(username, password);

    }

    public static String centerString(String s, int width, String color) {
        return String.format("%" + width + "s", color + s + RESET);
    }

    public static int getUserId() {
        return userId;
    }
}
