
package lms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author Swega
 */
class Library {
    private String name;
    private Admin admin;
    private final HashMap<String, Account> accounts;
    private final HashMap<String, Book> booksList;

    // only a single instance of library is creating (a singleton pattern)
    private static Library obj;

    // private constructor
    private Library() {
        name = null;
        admin = null;
        accounts = new HashMap<>();
        booksList = new HashMap<>();
    }

    public static Library getInstance() {
        if (obj == null) {
            obj = new Library();
        }
        return obj;
    }

    // getter of library name
    public String getName() {
        return name;
    }

    // setter of library name
    public void setName(String libName) {
        name = libName;
    }

    // getter of accounts
    public HashMap<String, Account> getAccounts() {
        return accounts;
    }

    // adding a member to accounts
    public void addMember(Member member) {
        accounts.put(member.getUsername(), member);
    }

    // change book status
    public void changeBookAvailability(String bookId) {
        booksList.get(bookId).changeCount(-1);
    }

    public HashMap<String, Book> getBooksList() {
        return booksList;
    }

    // checking the book availability status
    public boolean checkBookAvailability(String bookId) {
        return booksList.get(bookId).getCount() > 0;
    }

    // populating library details with default information
    public void populateLibrary() {

        // setting a default admin
        Admin account = new Admin("admin", "ADMIN", AccountStatus.ACTIVE);
        if (admin == null) {
            admin = account;
            accounts.put(account.getUsername(), account);
        } else {
            System.out.println("Only one admin should be assigned for a library");
        }
    }

    // asking the user whether he wishes to try again after unsuccessful login
    private boolean continueLogin() {

        Scanner userInput = new Scanner(System.in);
        System.out.println("""

                Press c        - Try again
                any other char - Go back or exit""");

        System.out.print("Enter your choice: ");
        return userInput.next().equals("c");
    }

    // trying to log in using given data
    public Account login(String className) {
        Scanner userInput = new Scanner(System.in);

        // a user with account is trying to log in
        System.out.print("Enter your account username: ");
        String username = userInput.next();

        if (accounts.containsKey(username)) {

            Account curAccount = accounts.get(username);
            if (curAccount.getStatus() == AccountStatus.ACTIVE) {

                // authorization
                if (curAccount.getClass().getSimpleName().equals(className)) {

                    // asking for the password from the user
                    System.out.print("Enter your account password: ");
                    String accountPass = userInput.next();

                    // authentication
                    if (accountPass.equals(curAccount.getPassword())) {
                        return curAccount;
                    } else {
                        System.out.println("Wrong password. Try again!");
                        return continueLogin() ? login(className) : null;
                    }
                } else {
                    System.out.println("Irrelevant account information. Try again!");
                    return continueLogin() ? login(className) : null;
                }

            } else {
                System.out.println("Account is blocked. Contact administrator for further details.");
                return null;
            }
        } else {
            System.out.println("Invalid username. Try again!");
            return continueLogin() ? login(className) : null;
        }
    }

    // Searching Books on basis of title, Subject or Author
    public ArrayList<String> searchForBooks() throws IOException {
        String choice;
        String title = "", subject = "", author = "", keyword = "";

        Scanner sc = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("""
                    Enter
                    1. Title
                    2. Subject
                    3. Author
                    4. Any keyword""");
            choice = sc.next();

            if (choice.equals("1") || choice.equals("2") || choice.equals("3") || choice.equals("4"))
                break;
            else
                System.out.println("\nWrong Input!");
        }

        switch (choice) {
            case "1" -> {
                System.out.println("\nEnter the Title of the Book: ");
                title = reader.readLine();
            }
            case "2" -> {
                System.out.println("\nEnter the Subject of the Book: ");
                subject = reader.readLine();
            }
            case "3" -> {
                System.out.println("\nEnter the Author of the Book: ");
                author = reader.readLine();
            }
            case "4" -> {
                System.out.println("\nEnter any keyword you remember to search: ");
                keyword = reader.readLine();
            }
        }

        ArrayList<String> matchedBooks = new ArrayList<>();

        // getting all books that matches condition with the given user's input
        for (Book b : booksList.values()) {
            switch (choice) {
                case "1":
                    if (b.getTitle().equalsIgnoreCase(title))
                        matchedBooks.add(b.getBookId());
                    break;
                case "2":
                    if (b.getSubject().equalsIgnoreCase(subject))
                        matchedBooks.add(b.getBookId());
                    break;
                case "3":
                    if (b.getAuthorName().equalsIgnoreCase(author))
                        matchedBooks.add(b.getBookId());
                    break;
                default:
                    keyword = keyword.toLowerCase();
                    if (b.getTitle().toLowerCase().contains(keyword) ||
                            b.getSubject().contains(keyword) ||
                            b.getAuthorName().contains(keyword)) {
                        matchedBooks.add(b.getBookId());
                    }
                    break;
            }
        }

        //Printing all the matched Books
        if (!matchedBooks.isEmpty()) {
            System.out.println("\nThese books are found: \n");

            System.out.println("----------------------------------------------------------------------------------");
            System.out.println("No.\t\tTitle\t\t\tAuthor\t\t\tSubject");
            System.out.println("----------------------------------------------------------------------------------");

            for (String matchedBook : matchedBooks) {
                booksList.get(matchedBook).printInfo();
                System.out.print("\n");
            }

            return matchedBooks;
        } else {
            System.out.println("No books available with the given information.\n" +
                    "Try searching any other books!");
            return null;
        }
    }

    // finding a particular member to see details about them (by admin)
    public Member findMember() {
        System.out.println("Enter member username: ");

        String username;
        Scanner scanner = new Scanner(System.in);
        username = scanner.next();

        Account member = accounts.get(username);
        if (member != null && member.getClass().getSimpleName().equals("Member")) {
            return (Member) member;
        } else {
            System.out.println("\nNo member found with this Id.");
            return null;
        }
    }

    // creating a new account (member)
    public void signUp() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a username: ");
        String username = scanner.next();

        while (accounts.containsKey(username)) {
            System.out.println("Username not available!");
            System.out.print("Enter a username: ");
            username = scanner.next();
        }

        System.out.print("Enter a password: ");
        String pass = scanner.next();

        Member newAccount = new Member(username, pass, AccountStatus.ACTIVE);
        addMember(newAccount);
    }

    // creating and adding the book to library
    public void createBook(String bookId, String title, String authorName, String subject, int count) {
        booksList.put(bookId, new Book(bookId, title, authorName, subject, count));
    }

    // changing password of any account
    public void changePassword(String accountUsername) {
        Account curAccount = accounts.get(accountUsername);

        Scanner sc = new Scanner(System.in);

        // getting the old password to verify the user
        System.out.print("Enter old password: ");
        if (sc.nextLine().equals(curAccount.getPassword())) {
            System.out.println("enter new password: ");
            curAccount.setPassword(sc.nextLine());
        } else {
            System.out.println("Wrong password. Try some other time");
            System.out.println("Wish to try again?\n1. yes\n2. no");

            if (sc.nextLine().equals("1")) {
                changePassword(accountUsername);
            } else {
                System.out.println("Redirecting to home page");
            }
        }
    }

    // show borrowed book history of a particular book
    public void showBorrowedHistory(String bookId) {
        booksList.get(bookId).printBorrowedHistory();
    }

    // check whether a member have any borrowed books at present
    public boolean haveBorrowedBooks(String memberName) {
        Member member = (Member) accounts.get(memberName);
        return member.haveCurrentlyBorrowedBooks();
//        return (lentBooks.containsKey(memberName) && lentBooks.get(memberName).size() != 0);
    }

    // getting borrow request from a member
    public void requestForBookBorrowal(String member, String isbn) {

        // checking if the book is available to issue
        if (checkBookAvailability(isbn)) {
            LocalDate todayDate = LocalDate.now();

            changeBookAvailability(isbn);

            LocalDate returnDate = LocalDate.now().plusDays(15);
            BookLending lendingBook = new BookLending(isbn, member, todayDate, returnDate);

            Member member1 = (Member) accounts.get(member);
            member1.addLendingBook(lendingBook);

        } else {
            System.out.println("The book is not available at present. " +
                    "Try searching any other books..");
        }
    }

    // get a book with the given id
    public Book getBook(String id) {
        return booksList.get(id);
    }

}
