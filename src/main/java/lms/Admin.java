
package lms;

import java.util.Scanner;

/**
 * @author Swega
 */
class Admin extends Account {

    private final Library library = Library.getInstance();

    public Admin(String uname, String pass, AccountStatus aStatus) {
        super(uname, pass, aStatus);
    }

    // adding a book to the library
    public void addBook() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nEnter bookId:");
        String bookId = scanner.nextLine();

        if (library.getBooksList().containsKey(bookId)) {
            System.out.print("Book already exists!\nEnter the no of stocks to be added: ");
            int count = scanner.nextInt();
            library.getBooksList().get(bookId).changeCount(count);
        } else {
            System.out.println("\nEnter Title:");
            String title = scanner.nextLine();

            System.out.println("\nEnter Author:");
            String author = scanner.nextLine();

            System.out.println("\nEnter Subject:");
            String subject = scanner.nextLine();

            System.out.println("\nEnter stock quantity:");
            int count = scanner.nextInt();

            library.createBook(bookId, title, author, subject, count);
        }
    }

    // block a member
    public void blockMember(Member member) {
        if (!library.haveBorrowedBooks(member.getUsername())) {
            if (member.getStatus() != AccountStatus.BLOCKED) {
                member.setStatus(AccountStatus.BLOCKED);
                System.out.println("Account blocked successfully..");
            } else {
                System.out.println("This account is already blocked");
            }
        } else {
            System.out.println("Member must return borrowed books.");
        }
    }

    // unblock a member
    public void unblockMember(Member member) {
        if (member.getStatus() != AccountStatus.ACTIVE) {
            member.setStatus(AccountStatus.ACTIVE);
            System.out.println("Account is re-activated successfully..");
        } else {
            System.out.println("this account is already active.");
        }
    }

    // checking whether the status change is possible
    private boolean canChangeStatus(Account member, AccountStatus statusToChange) {
        return library.getAccounts().get(member.getUsername()).getStatus().equals(statusToChange);
    }
}
