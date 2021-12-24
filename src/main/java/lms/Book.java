
package lms;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author Swega
 */
class Book {

    private final String bookId;
    private final String title;
    private final String authorName;
    private final String subject;
    private int count;

    // list for storing history of members borrowed this book
    private final ArrayList<BookLending> bookBorrowedMemHistory;
    private final ArrayList<BookLending> currentBorrowedMembers;

    public Book(String bookId, String title, String authorName, String subject, int count) {
        this.bookId = bookId;
        this.title = title;
        this.authorName = authorName;
        this.subject = subject;
        this.count = count;
        this.bookBorrowedMemHistory = new ArrayList<>();
        this.currentBorrowedMembers = new ArrayList<>();
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getSubject() {
        return subject;
    }

    public int getCount() {
        return count;
    }

    public void changeCount(int quantity) {
        this.count += quantity;
    }

    private void addToBorrowedHistory(BookLending book) {
        bookBorrowedMemHistory.add(book);
    }

    public void addToCurrentlyBorrowedMembersList(BookLending book) {
        currentBorrowedMembers.add(book);
    }

    public BookLending findBook(String bookId) {
        for (BookLending bookLending : currentBorrowedMembers) {
            if (bookLending.getBookId().equals(bookId)) {
                return bookLending;
            }
        }
        return null;
    }

    public void removeFromCurrentlyBorrowedMembersList(String bookId) {

        BookLending book = findBook(bookId);
        currentBorrowedMembers.remove(book);
        addToBorrowedHistory(book);
    }

    // printing book borrowed history of a book
    public void printBorrowedHistory() {

        if (bookBorrowedMemHistory.size() > 0) {
            bookBorrowedMemHistory.sort(Comparator.comparing(BookLending::getActualReturningDate).reversed());
            for (BookLending bk : bookBorrowedMemHistory) {
                bk.printInfo();
            }
        } else {
            System.out.println("No borrow history about this book");
        }
    }

    // printing currently borrowed members of this book
    public void printCurrentlyBorrowedMembers() {
        if (currentBorrowedMembers.size() > 0) {
            for (BookLending bk : currentBorrowedMembers) {
                bk.printInfo();
            }
        } else {
            System.out.println("No borrowal details currently about this book");
        }
    }

    // printing book's Info
    public void printInfo() {
        System.out.println(bookId + "\t\t" + title + " \t\t " + authorName + " \t\t " + subject);
    }
}
