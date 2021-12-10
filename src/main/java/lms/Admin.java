
package lms;

import java.time.LocalDate;
import java.util.Scanner;

/**
 *
 * @author Swega
 */
class Admin extends Account {

    private final Library library = Library.getInstance();

    public Admin(String uname, String pass, AccountStatus aStatus) {
        super(uname, pass, aStatus);
    }

    // return a book by a particular member
    public void returnBook(String memberName, long bookId) {
        library.removeBookFromMember(memberName, bookId);
    }

    // issuing a book to a particular member
    public void issueBook(Member member, long bookId) {

        LocalDate todayDate = LocalDate.now();

        // checking if the book is available to issue
        if (library.checkBookAvailability(bookId)) {
            System.out.println("book is available.");
            library.changeBookAvailability(bookId, false);

            LocalDate returnDate = LocalDate.now().plusDays(15);
            BookLending lendingBook = new BookLending(member.getUsername(), bookId, todayDate, returnDate);

            library.addLendingBook(lendingBook);

        } else {
            System.out.println("The book is already taken by someone. " +
                    "Try searching any other books..");
            // add a condition whether to continue searching or going back

        }
    }

    // creating a new account (member)
    public void createMember() {
        Scanner newUserInput = new Scanner(System.in);

        System.out.print("Enter a username: ");
        String username = newUserInput.next();

        while (library.getAccounts().containsKey(username)) {
            System.out.println("Username not available!");
            System.out.print("Enter a username: ");
            username = newUserInput.next();
        }

        System.out.print("Enter a password: ");
        String pass = newUserInput.next();

        Member newAccount = new Member(username, pass, AccountStatus.ACTIVE);
        library.addMember(newAccount);
    }
}
