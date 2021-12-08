
package lms;

/**
 *
 * @author Swega
 */
public class Book {
    String ISBN;
    String title;
    String authorName;
    String subject;
    
    public Book(String isbn, String title, String authorName, String subject) {
        this.ISBN = isbn;
        this.title = title;
        this.authorName = authorName;
        this.subject = subject;
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
}
