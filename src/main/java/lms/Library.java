
package lms;

import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Swega
 */
class Library {
    private String name;
    private Librarian librarian;
    private HashMap<Long, Account> accounts;
    private HashMap<Long, Book> books;
    private HashMap<Long, Book> lentBooks;
    
    // only a single instance of library is creating (a singleton pattern)
    private static Library obj;

    // private constructor
    private Library() {
        name = null;
        librarian = null;
        accounts = new HashMap();
        books = new HashMap();
        lentBooks = new HashMap();
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
    public HashMap<Long, Account> getAccounts() {
        return accounts;
    }
    
    // populating library details with default information
    public void populateLibrary() {
        
        // setting a default librarian
        Librarian account = new Librarian(1L, "LIBRARIAN", AccountStatus.ACTIVE);
        if (librarian == null) {
            librarian = account;
            accounts.put(account.id, account);
        } else {
            System.out.println("Only one librarian should be assigned for a library");
        }
    }
    
    // logging using given data
    public boolean login() {
        Scanner userInput = new Scanner(System.in);
                    
        // a user with account is trying to log in
        System.out.print("Enter your account id: ");
        long accountId = userInput.nextLong();
                    
        if (accounts.containsKey(accountId)) {
            System.out.print("Enter your password: ");
            String accountPass = userInput.next();
                        
            if (accountPass.equals(accounts.get(accountId).password)) {
                return true;
            } else {
                System.out.println("Wrong password. Try again!");
                return login();
            }
        } else {
            System.out.println("Invalid account id. Try again!");
            return login();
        }
    }
    
    // creating a new account (member)
    public void createMember() {
        Scanner newUserInput = new Scanner(System.in);
        
        System.out.print("Enter an id: ");
        long id = newUserInput.nextLong();

        while (accounts.containsKey(id)) {
            System.out.println("User id not available!");
            System.out.print("Enter an id: ");
            id = newUserInput.nextLong();
        }
        
        System.out.print("Enter a password: ");
        String pass = newUserInput.next();

        Account newAccount = new Member(id, pass, AccountStatus.ACTIVE);
        accounts.put(id, newAccount);
    }
}
