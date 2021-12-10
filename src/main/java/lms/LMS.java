package lms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Swega
 */
class LMS {

    // Operations of an admin
    public static void performAdminOperation(int choice) throws IOException {
        Library lib = Library.getInstance();

        switch (choice) {

            // add a book
            case 1 -> {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                System.out.println("\nEnter ISBN:");
                String isbn = reader.readLine();

                System.out.println("\nEnter Title:");
                String title = reader.readLine();

                System.out.println("\nEnter Author:");
                String author = reader.readLine();

                System.out.println("\nEnter Subject:");
                String subject = reader.readLine();

                lib.createBook(isbn, title, author, subject);
            }


            // block a member
            case 2 -> {
                Member member = lib.findMember();
                if (member != null) {
                    if (lib.haveBorrowedBooks(member.getUsername())) {
                        member.setStatus(AccountStatus.BLOCKED);
                    } else {
                        System.out.println("Member must return borrowed books.");
                    }
                }
            }


            // unblock a member
            case 3 -> {
                Member member = lib.findMember();
                if (member != null) {
                    member.setStatus(AccountStatus.ACTIVE);
                }
            }


            // see a member information
            case 4 -> {
                Member member = lib.findMember();
                member.printInfo();
                lib.printMemberBorrowedDetails(member.getUsername());
            }

            // see borrow history for a book
            case 5 -> {
                List<Long> bookIds = lib.searchForBooks();
                long input = UtilityClass.getInput(bookIds);
                lib.showBorrowedHistory(input);
            }

            // see admin personal information
            case 6 -> lib.getAdmin().printInfo();

            // change admin's password
            case 7 -> lib.changePassword(lib.getAdmin().getUsername());
        }
    }

    // operations for a member
    private static void performMemberOperation(Member member, int choice) throws IOException {
        Library library = Library.getInstance();

        Admin admin = library.getAdmin();

        switch (choice) {
            case 1:
                library.searchForBooks();
                break;

            case 2: {
                long input;
                ArrayList<Long> bookIds = library.searchForBooks();
                System.out.println(bookIds);

                if (bookIds != null) {
                    input = UtilityClass.getInput(bookIds);
                    library.getAdmin().issueBook(member, input);
                }
                break;
            }

            case 3: {
                long input;
                library.printMemberBorrowedDetails(member.getUsername());

                List<Long> borrowBooksIds = library.getBorrowedBooksSizeOfMember(member.getUsername());
                if (borrowBooksIds != null) {
                    input = UtilityClass.getInput(borrowBooksIds);
                    admin.returnBook(member.getUsername(), input);
                } else {
                    System.out.println("No books to return");
                }
                break;
            }

            case 4:
                // renew a book
                break;

            case 5:
                member.printInfo();
                library.printMemberBorrowedDetails(member.getUsername());
                break;
        }
    }

    public static void main(String[] args) throws IOException {

        /* Instantiating a library object */
        Library library = Library.getInstance();

        /* Initializing some default details about library */
        library.setName("Central Library");

        library.populateLibrary();

        System.out.println("Welcome to " + library.getName());
        boolean loop = true;
        Scanner scanner = new Scanner(System.in);

        while (loop) {
            // choosing the user category 
            System.out.println("Choose a user type\n");
            System.out.println("1. Member");
            System.out.println("2. Admin");
            System.out.println("3. Exit");

            int choice = UtilityClass.getInput(1, 3);

            if (choice == 1) {

                // go to log in or signup page
                System.out.println("Already a user?"
                        + "\nIf yes, SignIn; Else SignUp");
                System.out.println("1. Sign In\n2. Sign Up");

                choice = UtilityClass.getInput(1, 2);

                // showing member options
                if (choice != 1) {
                    // a user is trying to create an account
                    library.getAdmin().createMember();
                    System.out.println("Account created."
                            + " Redirecting to login page..");
                }

                Account member = library.login();
                if (member != null && member.getClass().getSimpleName().equals("Member")) {

                    while (true) {
                        // showing member options
                        System.out.println("----------------------------------------------------------------------------");
                        member.printInfo();
                        System.out.println("----------------------------------------------------------------------------");
                        library.printMemberBorrowedDetails(member.getUsername());
                        System.out.println("----------------------------------------------------------------------------");
                        System.out.println("Welcome to member's page");
                        System.out.println("----------------------------------------------------------------------------");
                        System.out.println("1. Search a book");
                        System.out.println("2. Borrow a book");
                        System.out.println("3. Return a book");
                        System.out.println("4. Renew a book");
                        System.out.println("5. View profile");
                        System.out.println("6. Logout");
                        System.out.println("----------------------------------------------------------------------------");

                        choice = UtilityClass.getInput(1, 6);
                        if (choice != 6) {
                            performMemberOperation((Member) member, choice);
                        } else {
                            System.out.println("Logged out");
                            break;
                        }
                        System.out.println("\nPress any key to continue..\n");
                        scanner.next();
                    }

                } else {
                    System.out.println("Irrelevant account information. Try again!");
                }

            } else if (choice == 2) {

                Account admin = library.login(); // check for admin validation
                if (admin != null && admin.getClass().getSimpleName().equals("Admin")) {
                    // showing admin options

                    while (true) {
                        System.out.println("----------------------------------------------------------------------------");
                        System.out.println("\tWelcome to admin's page");
                        System.out.println("----------------------------------------------------------------------------");
                        System.out.println("1. Add a book");
                        System.out.println("2. Block a member");
                        System.out.println("3. Unblock a member");
                        System.out.println("4. See a particular member details");
                        System.out.println("5. See borrowed history of a particular book");
                        System.out.println("6. See admin's personal information");
                        System.out.println("7. Change password");
                        System.out.println("8. Logout");
                        System.out.println("----------------------------------------------------------------------------");

                        choice = UtilityClass.getInput(1, 8);
                        if (choice != 8) {
                            performAdminOperation(choice);
                        } else {
                            System.out.println("Logged out!");
                            break;
                        }
                    }
                } else {
                    System.out.println("Irrelevant account information. Try again!");
                }

            } else {
                loop = false;
                System.out.println("Thank you..");
            }
        }
    }
}
