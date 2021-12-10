
package lms;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * @author Swega
 */
class BookLending {

    private final String memberName;
    private final long bookId;
    private final LocalDate issuedDate;
    private final LocalDate returnDate;

    public long getBookId() {
        return bookId;
    }

    public String getMemberName() {
        return memberName;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public BookLending(String memberName, long bookId, LocalDate issuedDate, LocalDate returnDate) {
        this.memberName = memberName;
        this.bookId = bookId;
        this.issuedDate = issuedDate;
        this.returnDate = returnDate;
    }

    // printing details about a particular borrow entry of a book
    public void printInfo() {

        //temp cur date
        //LocalDate curDate = LocalDate.parse("2021-12-29");

        System.out.println("Book id: " + bookId + "\t\tissued at: " + issuedDate + "\t\t"
                + "Return date: " + returnDate + "\t\tDue in: "
                + LocalDate.now().until(returnDate, ChronoUnit.DAYS) + "days\n");
    }
}
