
package lms;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Swega
 */
class Member extends Account {

    private final ArrayList<BookLending> bookIdsOfPastBorrowedBooks;
    private final ArrayList<BookLending> currentlyBorrowedBooks;
    private final Library library = Library.getInstance();

    public Member(String uname, String pass, AccountStatus aStatus) {
        super(uname, pass, aStatus);
        this.bookIdsOfPastBorrowedBooks = new ArrayList<>();
        this.currentlyBorrowedBooks = new ArrayList<>();
    }

    public ArrayList<String> getCurrentlyBorrowedBookIds() {
        ArrayList<String> ids = new ArrayList<>();

        for (BookLending bk : currentlyBorrowedBooks) {
            ids.add(bk.getBookId());
        }

        return ids;
    }

    public boolean haveCurrentlyBorrowedBooks() {
        return !currentlyBorrowedBooks.isEmpty();
    }

    public void addToBooksBorrowedHistory(BookLending borrowedBook) {
        bookIdsOfPastBorrowedBooks.add(borrowedBook);
    }

    // print all books that are borrowed by this member
    public void printBorrowedBooksHistory() {

        if (bookIdsOfPastBorrowedBooks.size() > 0) {
            bookIdsOfPastBorrowedBooks.sort(Comparator.comparing(BookLending::getActualReturningDate).reversed());
            for (BookLending item : bookIdsOfPastBorrowedBooks) {
                item.printInfo();
            }
        } else {
            System.out.println("No records found!");
        }
    }

    // add book bookId to currently borrowed list
    public void addToCurrentBorrowedBooks(BookLending bookLending) {
        if (currentlyBorrowedBooks.size() < 3) {
            currentlyBorrowedBooks.add(bookLending);
            System.out.println("Book successfully added to current books borrowed");
        } else {
            System.out.println("Maximum borrowal limit reached.");
        }
    }

    // check whether a given bookId is already present in member currently borrowed list
    public boolean isBookAlreadyBorrowed(String bookId) {
        return currentlyBorrowedBooks.contains(getBorrowedBook(bookId));
    }

    // issuing a book to a particular member
    public void borrowBook(String bookId) {
        library.requestForBookBorrowal(getUsername(), bookId);
    }

    // get book lending object
    private BookLending getBookLending(String bookId) {
        for (BookLending bk : currentlyBorrowedBooks) {
            if (bk.getBookId().equals(bookId)) {
                return bk;
            }
        }
        return null;
    }

    // return a book by a particular member
    public void returnBook(String bookId) {
        removeBookFromMember(bookId);
        library.getBook(bookId).removeFromCurrentlyBorrowedMembersList(bookId);
    }

    // check if trying to renew the book
    public BookLending getBorrowedBook(String bookId) {

        for (BookLending b : currentlyBorrowedBooks) {
            if (b.getBookId().equals(bookId)) {
                return b;
            }
        }
        return null;
    }


    // renew a book
    public void renewBook(String book) {

        BookLending bk = getBorrowedBook(book);

        LocalDate todayDate = LocalDate.now();
        LocalDate returnDate = bk.getReturnDate();

        if (todayDate.isEqual(returnDate) || todayDate.isAfter(returnDate)) {
            library.changeBookAvailability(bk.getBookId());
            borrowBook(book);
        } else {
            System.out.println("Due date is not near yet! Due in " +
                    todayDate.until(returnDate, ChronoUnit.DAYS));
        }

    }


    // return a book (from the member)
    public void removeBookFromMember(String bookId) {
        BookLending bookLent = null;

        for (BookLending bookLending : currentlyBorrowedBooks) {
            if (bookLending.getBookId().equals(bookId)) {
                bookLent = bookLending;
                break;
            }
        }

        if (bookLent != null) {
            // check for fine
            LocalDate rDate = bookLent.getReturnDate(); // return date per record

            // checking for an expired return date condition
            LocalDate rtDate = LocalDate.parse("2022-01-15"); // actual returning date

            // checking whether the recorded return date is equal or before to actual returning date
            boolean isReturnDateExpired = rDate.isBefore(rtDate) || rDate.equals(rtDate);

            if (isReturnDateExpired) {
                System.out.println("You have to pay the fine");
                computeFine((int) rDate.until(rtDate, ChronoUnit.DAYS));
            }
            currentlyBorrowedBooks.remove(bookLent);
            System.out.println("Book successfully returned!");

            library.getBook(bookId).changeCount(1);

            System.out.println("choose return date: (1/2) 1-today 2-dummy date");
            if (UtilityClass.getInput(1, 2) == 1) {
                bookLent.setActualReturningDate(LocalDate.now());
            } else {
                bookLent.setActualReturningDate(rtDate);
            }
            addToBooksBorrowedHistory(bookLent);

        } else {
            System.out.println("no book with the given id. try again!");
        }
    }

    // printing details of a particular member's borrowed book list
    public void printMemberBorrowedDetails() {
        if (currentlyBorrowedBooks.size() > 0) {
            for (BookLending bookLending : currentlyBorrowedBooks) {
                System.out.print("Book Name: " + library.getBook(bookLending.getBookId()).getTitle() + "\n");
                bookLending.printInfo();
            }
        } else {
            System.out.println(getUsername() + " have not borrowed any books currently.");
        }
    }

    // creating a lending object for a book being borrowed by a member
    public void addLendingBook(BookLending bookLendingObj) {
        addToCurrentBorrowedBooks(bookLendingObj);
        library.getBook(bookLendingObj.getBookId()).addToCurrentlyBorrowedMembersList(bookLendingObj);
    }

    // computing fine for the books
    private void computeFine(int daysDifference) {

        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("You have a fine to pay");
        int total = 0;
        total += daysDifference * 2; // for each day let fine amount be Rs.2

        System.out.println("You should pay Rs." + total);
        System.out.println("----------------------------------------------------------------------------------");

        try {
            Thread.sleep(2000);
            System.out.println("You paid the fine :)");
        } catch (InterruptedException ignored) {}
    }

}
