
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
    private final HashMap<Long, Book> books;
    private final HashMap<String, ArrayList<BookLending>> lentBooks;

    private static long booksIdCount;

    // only a single instance of library is creating (a singleton pattern)
    private static Library obj;

    // private constructor
    private Library() {
        name = null;
        admin = null;
        accounts = new HashMap<>();
        books = new HashMap<>();
        lentBooks = new HashMap<>();
        booksIdCount = 0;
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

    // getter of admin
    public Admin getAdmin() {
        return admin;
    }

    // change book status
    public void changeBookAvailability(long bookId, boolean availability) {
        books.get(bookId).setAvailable(availability);
    }

    // checking the book availability status
    public boolean checkBookAvailability(long bookId) {
        return books.get(bookId).isAvailable();
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
        System.out.println("Press c        - Try again\n" +
                "any other char - Go back or exit");

        return userInput.next().equals("c");
    }

    // logging using given data
    public Account login() {
        Scanner userInput = new Scanner(System.in);

        // a user with account is trying to log in
        System.out.print("Enter your account username: ");
        String username = userInput.next();

        if (accounts.containsKey(username)) {
            System.out.print("Enter your account password: ");
            String accountPass = userInput.next();

            Account curAccount = accounts.get(username);
            if (curAccount.getStatus() == AccountStatus.ACTIVE && accountPass.equals(curAccount.getPassword())) {
                return curAccount;
            } else {
                System.out.println("Wrong password. Try again!");
                return continueLogin() ? login() : null;
            }
        } else {
            System.out.println("Invalid username. Try again!");
            return continueLogin() ? login() : null;
        }
    }

    // Searching Books on basis of title, Subject or Author
    public ArrayList<Long> searchForBooks() throws IOException {
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

        ArrayList<Long> matchedBooks = new ArrayList<>();

        // getting all books that matches condition with the given user's input
        for (Book b : books.values()) {
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

            System.out.println("----------------------------------------------------------------------------");
            System.out.println("No.\t\tISBN\t\tTitle\t\t\tAuthor\t\t\tSubject");
            System.out.println("----------------------------------------------------------------------------");

            for (Long matchedBook : matchedBooks) {
                books.get(matchedBook).printInfo();
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

        String username = "";
        Scanner scanner = new Scanner(System.in);

        try {
            username = scanner.next();
        } catch (InputMismatchException e) {
            System.out.println("\nInvalid Input");
        }

        Account member = accounts.get(username);
        if (member != null && member.getClass().getSimpleName().equals("Member")) {
            return (Member) member;
        } else {
            System.out.println("\nNo member found with this Id.");
            return null;
        }
    }

    // creating and adding the book to library
    public void createBook(String isbn, String title, String authorName, String subject) {

        booksIdCount += 1;
        books.put(booksIdCount, new Book(booksIdCount, isbn, title, authorName, subject));
        System.out.println(books.size());
    }

    // return a book (from the member)
    public void removeBookFromMember(String memberName, long bookId) {
        BookLending bookLent = null;

        for (BookLending bookLending : lentBooks.get(memberName)) {
            if (bookLending.getBookId() == bookId) {
                bookLent = bookLending;
                break;
            }
        }

        if (bookLent != null) {
            // check for fine
            LocalDate rDate = bookLent.getReturnDate(); // return date per record

            // checking for an expired return date condition
            LocalDate rtDate = LocalDate.parse("2021-12-29"); // actual returning date

            // checking whether the recorded return date is equal or before to actual returning date
            boolean isReturnDateExpired = rDate.isBefore(rtDate) || rDate.equals(rtDate);

            if (isReturnDateExpired) {
                System.out.println("You have to pay the fine");
                computeFine((int) rDate.until(rtDate, ChronoUnit.DAYS));
            }
            lentBooks.get(memberName).remove(bookLent);
            books.get(bookId).setAvailable(true);
        } else {
            System.out.println("no book with the given id. try again!");
        }
    }

    // computing fine for the books
    private void computeFine(int daysDifference) {

        System.out.println("----------------------------------------------------------------------------");
        System.out.println("You have a fine to pay");
        int total = 0;
        total += daysDifference * 2; // for each day let fine amount be Rs.2

        System.out.println("You should pay Rs." + total);
        System.out.println("----------------------------------------------------------------------------");
    }

    // creating a lending object for a book being borrowed by a member
    public void addLendingBook(BookLending bookLendingObj) {
        String memberName = bookLendingObj.getMemberName();

        if (lentBooks.containsKey(memberName)) {
            if (lentBooks.get(memberName).size() < 3) {
                lentBooks.get(memberName).add(bookLendingObj);
                books.get(bookLendingObj.getBookId()).addToBorrowedHistory(memberName);
            } else {
                System.out.println("Maximum limit reached to borrow a book");
            }
        } else {
            ArrayList<BookLending> newList = new ArrayList<>();
            newList.add(bookLendingObj);
            lentBooks.put(memberName, newList);
            books.get(bookLendingObj.getBookId()).addToBorrowedHistory(memberName);
        }
    }

    // printing details of a particular member's borrowed book list
    public void printMemberBorrowedDetails(String memberName) {
        if (lentBooks.containsKey(memberName)) {
            for (BookLending bookLending : lentBooks.get(memberName)) {
                System.out.print("Book Name: " + books.get(bookLending.getBookId()).getTitle().toUpperCase() + "\n");
                bookLending.printInfo();
            }
        } else {
            System.out.println(memberName + " have not borrowed any books currently.");
        }
    }

    // returning total count of borrowed books by a particular member at present
    public List<Long> getBorrowedBooksSizeOfMember(String memberName) {
        if (lentBooks.containsKey(memberName)) {
            ArrayList<Long> lentBooksId = new ArrayList<>();
            for (BookLending bookLending : lentBooks.get(memberName)) {
                lentBooksId.add(bookLending.getBookId());
            }
            return lentBooksId;
        }
        return null;
    }

    // changing password of any account
    public void changePassword(String accountUsername) throws IOException {
        Account curAccount = accounts.get(accountUsername);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // getting the old password to verify the user
        System.out.print("Enter old password: ");
        if (reader.readLine().equals(curAccount.getPassword())) {
            System.out.println("enter new password: ");
            curAccount.setPassword(reader.readLine());
        } else {
            System.out.println("Wrong password. Try some other time");
        }
    }

    // show borrowed book history of a particular book
    public void showBorrowedHistory(long bookId) {
        books.get(bookId).printBorrowedHistory();
    }

    // check whether a member have any borrowed books at present
    public boolean haveBorrowedBooks(String memberName) {
        return (lentBooks.containsKey(memberName) && lentBooks.get(memberName).size() == 0);
    }
}
