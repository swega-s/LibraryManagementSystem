
package lms;

import java.util.ArrayList;

/**
 * @author Swega
 */
public class Book {

    private final long bookId;
    private final String ISBN;
    private final String title;
    private final String authorName;
    private final String subject;
    private boolean isAvailable;

    // list for storing history of members borrowed this book
    private final ArrayList<String> booksBorrowedMembers;

    public Book(long id, String isbn, String title, String authorName, String subject) {
        this.bookId = id;
        this.ISBN = isbn;
        this.title = title;
        this.authorName = authorName;
        this.subject = subject;
        this.isAvailable = true;
        this.booksBorrowedMembers = new ArrayList<>();
    }

    public long getBookId() {
        return bookId;
    }

    public String getISBN() {
        return ISBN;
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean availableFlag) {
        isAvailable = availableFlag;
    }

    public void addToBorrowedHistory(String memberName) {
        booksBorrowedMembers.add(memberName);
    }

    // printing book borrowed history of a book
    public void printBorrowedHistory() {
        System.out.println(booksBorrowedMembers.size());
        for (int index = 1; index < booksBorrowedMembers.size() + 1; index++) {
            System.out.println(index + ". " + booksBorrowedMembers.get(index - 1));
        }
    }

    // printing book's Info
    public void printInfo() {
        System.out.println(bookId + " \t\t " + title + " \t\t " + authorName + " \t\t " + subject);
    }
}
