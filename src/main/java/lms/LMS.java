package lms;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Swega
 */
class LMS {

    // getting the particular book to return or renew
    private static String getBookId(Member member) {

        member.printMemberBorrowedDetails();

        String input = "";

        List<String> borrowBooksIds = member.getCurrentlyBorrowedBookIds(); //library.getBorrowedBooksId(member.getUsername());
        if (borrowBooksIds != null) {
            input = UtilityClass.getInput(borrowBooksIds);
        }
        return input;
    }

    private static void performAction(Admin admin, Method method, Library library, int choice)
            throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Member member = library.findMember();
        if (member != null) {
            method.invoke(admin, member);
        } else {
            System.out.println("Wish to continue?\n1. yes\n2. no");
            if (UtilityClass.getInput(1, 2) == 1) {
                performAdminOperation(admin, choice);
            } else {
                System.out.println("Redirecting to admin home page");
            }
        }
    }

    // Operations of an admin
    private static void performAdminOperation(Admin admin, int choice)
            throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Library library = Library.getInstance();

        switch (choice) {

            // add a book
            case 1 -> admin.addBook();

            // block a member
            case 2 -> {
                Method functionToPass = Admin.class.getMethod("blockMember", Member.class);
                performAction(admin, functionToPass, library, choice);
            }

            // unblock a member
            case 3 -> {
                Method functionToPass = Admin.class.getMethod("unblockMember", Member.class);
                performAction(admin, functionToPass, library, choice);
            }

            // see a member information
            case 4 -> {
                Member member = library.findMember();
                if (member != null) {
                    System.out.println(member);
                    member.printMemberBorrowedDetails();
                    System.out.println("Wish to see " + member.getUsername() + "'s past borrowed books history?" +
                            "\n1. yes\n2. no\n");
                    if (UtilityClass.getInput(1, 2) == 1) {
                        member.printBorrowedBooksHistory();
                    }
                }
            }

            // see borrow history for a book
            case 5 -> {
                List<String> booksIdList = library.searchForBooks();

                if (booksIdList != null) {
                    String input = UtilityClass.getInput(booksIdList);
                    if (input != null) {

                        label:
                        while (true) {
                            System.out.println("""
                                    1. See availability of books
                                    2. See currently borrowed members
                                    3. See history of borrowed members
                                    4. go back""");

                            switch (UtilityClass.getInput(1, 4)) {

                                // checking availability of books (count)
                                case 1 -> System.out.println("Books currently available: " + library.getBook(input).getCount());

                                // see members details who borrowed this book at present
                                case 2 -> library.getBook(input).printCurrentlyBorrowedMembers();

                                // see members details who borrowed this book in the past
                                case 3 -> library.getBook(input).printBorrowedHistory();

                                // go back
                                default -> {
                                    break label;
                                }
                            }
                            System.out.println("----------------------------------------------------------------------------------");
                        }
                    }
                }
                System.out.println("----------------------------------------------------------------------------------");
            }

            // see admin personal information
            case 6 -> admin.printInfo();

            // change admin's password
            case 7 -> library.changePassword(admin.getUsername());
        }
    }

    // operations for a member
    private static void performMemberOperation(Member member, int choice) throws IOException {
        Library library = Library.getInstance();

        switch (choice) {

            // borrow a book
            case 1 -> {
                ArrayList<String> bookIds = library.searchForBooks();

                if (bookIds != null) {
                    String input = UtilityClass.getInput(bookIds);

                    if (!member.isBookAlreadyBorrowed(input)) {
                        member.borrowBook(input);
                    } else {
                        System.out.println("You already borrowed this book.");
                    }
                }
            }

            // return a book
            case 2 -> {
                if (library.haveBorrowedBooks(member.getUsername())) {
                    String bookId = getBookId(member);
                    if (bookId != null) {
                        member.returnBook(bookId);
                    }
                } else {
                    System.out.println("No books to return");
                }
            }

            // renew a book
            case 3 -> {
                if (library.haveBorrowedBooks(member.getUsername())) {
                    String isbn = getBookId(member);
                    if (isbn != null) {
                        member.renewBook(isbn);
                    }
                } else {
                    System.out.println("no books to renew");
                }
            }

            // printing the member's personal info and borrowed book details
            case 4 -> {
                member.printInfo();
                System.out.println("----------------------------------------------------------------------------------");
                member.printMemberBorrowedDetails();
            }

            // show all previously borrowed books (returned books history)
            case 5 -> {
                member.printBorrowedBooksHistory();
                System.out.println("----------------------------------------------------------------------------------");
            }
        }
    }

    public static void main(String[] args) throws IOException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {

        /* Instantiating a library object */
        Library library = Library.getInstance();

        /* Initializing some default details about library */
        library.setName("Central Library");

        /* Initializing with a default admin account here */
        library.populateLibrary();

        System.out.println("Welcome to " + library.getName());
        boolean loop = true;
        Scanner scanner = new Scanner(System.in);

        while (loop) {
            // choosing the user category 
            System.out.println("\nChoose a user type");
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

                if (choice == 2) {
                    // a user is trying to create an account
                    library.signUp();
                    System.out.println("\nAccount created.");

                    System.out.println("Wish to login?\n1. Login\n2. Go back\nEnter your choice: ");
                    if (scanner.nextInt() == 2) {
                        continue;
                    } else {
                        System.out.println("\nRedirecting to login page..\n");
                    }
                }

                Account member = library.login(Member.class.getSimpleName());
                if (member != null) {

                    while (true) {
                        // showing member options

                        member.printInfo();
                        System.out.println("----------------------------------------------------------------------------------");
                        ((Member) member).printMemberBorrowedDetails();
                        System.out.println("----------------------------------------------------------------------------------");
                        System.out.println("Welcome to member's page");
                        System.out.println("----------------------------------------------------------------------------------");
                        System.out.println("1. Borrow a book");
                        System.out.println("2. Return a book");
                        System.out.println("3. Renew a book");
                        System.out.println("4. View profile");
                        System.out.println("5. See borrowed books history");
                        System.out.println("6. Logout");
                        System.out.println("----------------------------------------------------------------------------------");

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
                    System.out.println("Exiting from login process.");
                }

            } else if (choice == 2) {

                Account admin = library.login(Admin.class.getSimpleName()); // check for admin validation
                if (admin != null) {
                    // showing admin options

                    while (true) {
                        System.out.println("----------------------------------------------------------------------------------");
                        System.out.println("\tWelcome to admin's page");
                        System.out.println("----------------------------------------------------------------------------------");
                        System.out.println("1. Add book");
                        System.out.println("2. Block a member");
                        System.out.println("3. Unblock a member");
                        System.out.println("4. See a particular member details");
                        System.out.println("5. See a particular book details");
                        System.out.println("6. See admin's personal information");
                        System.out.println("7. Change password");
                        System.out.println("8. Logout");
                        System.out.println("----------------------------------------------------------------------------------");

                        choice = UtilityClass.getInput(1, 8);
                        if (choice != 8) {
                            performAdminOperation((Admin) admin, choice);
                        } else {
                            System.out.println("Logged out!");
                            break;
                        }
                        System.out.println("\nPress any key to continue..\n");
                        scanner.next();
                    }
                } else {
                    System.out.println("Exiting from login process.");
                }

            } else {
                loop = false;
                System.out.println("Thank you..");
            }
        }
    }
}
