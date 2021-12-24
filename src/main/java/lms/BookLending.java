
package lms;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * @author Swega
 */
class BookLending {

    private final String bookId;
    private final String memberName;
    private final LocalDate issuedDate;
    private final LocalDate returnDate;
    private LocalDate actualReturningDate;

    public BookLending(String id, String memberName, LocalDate issuedDate, LocalDate returnDate) {
        this.bookId = id;
        this.memberName = memberName;
        this.issuedDate = issuedDate;
        this.returnDate = returnDate;
        this.actualReturningDate = null;
    }

    public String getBookId() {
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

    public LocalDate getActualReturningDate() {
        return actualReturningDate;
    }

    public void setActualReturningDate(LocalDate actualReturningDate) {
        this.actualReturningDate = actualReturningDate;
    }

    // printing details about a particular borrow entry of a book
    public void printInfo() {

        //temp cur date
        //LocalDate curDate = LocalDate.parse("2021-12-29");
        // LocalDate.now().until(returnDate, ChronoUnit.DAYS)

        System.out.println("Book id: " + bookId + "\tissued to: " + memberName + "\tissued at: " + issuedDate + "\t"
                + "Due date: " + returnDate + "\tReturned date: " + this.actualReturningDate + "\n");

    }

}
