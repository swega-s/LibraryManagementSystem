package lms;

import java.util.Scanner;

/**
 * @author Swega
 */
class LMS {

    // getting input choice with a min and max range
    public static int getInput(int minValue, int maxValue) {
        int choice;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            if (!(minValue > choice || choice > maxValue)) {
                return choice;
            } else {
                System.out.println("Invalid input");
            }
        }
    }

    public static void main(String[] args) {

        /* Instantiating a library object */
        Library library = Library.getInstance();

        /* Initializing some default details about library */
        library.setName("Central Library");

        library.populateLibrary();

        System.out.println("Welcome to " + library.getName());
        boolean loop = true;

        while (loop) {
            // choosing the user category 
            System.out.println("Choose a user type\n");
            System.out.println("1. Member");
            System.out.println("2. Librarian");
            System.out.println("3. Admin");
            System.out.println("4. Exit");

            int choice = getInput(1, 4);

            if (choice == 1) {

                // go to login/ signup page
                System.out.println("Already a user?"
                        + "\nIf yes, SignIn; Else SignUp");
                System.out.println("1. Sign In\n2. Sign Up");

                choice = getInput(1, 2);

                // showing member options
                if (choice != 1) {
                    // a user is trying to create an account
                    library.signup();
                    System.out.println("Account created."
                            + " Redirecting to login page..");
                }
                if (library.login()) {
                    // showing member options
                    System.out.println("showing member options now");
                }
            } else if (choice == 2) {
                if (library.login()) {
                    // showing librarian options
                    System.out.println("showing librarian options now");
                }
            } else if (choice == 3) {
                // get password to check if this is admin
                Scanner scanner = new Scanner(System.in);
                String adminPassInput = scanner.next();

                do {
                    if (adminPassInput.equals("LIB_ADMIN")) {
                        // show admin options
                        System.out.println("showing admin options");
                        break;
                    } else {
                        System.out.println("Wrong password. Try again!");
                        adminPassInput = scanner.next();
                    }
                } while (true);

            } else {
                loop = false;
                System.out.println("Thank you..");
            }
        }
    }
}
