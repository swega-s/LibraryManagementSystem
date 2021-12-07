
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
    
    public static Library getInstance() {
        if (obj == null) {
            obj = new Library();
        }
        return obj;
    }
    
    // private constructor
    private Library() {
        name = null;
        librarian = null;
        accounts = new HashMap();
        books = new HashMap();
        lentBooks = new HashMap();
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
        Account account = new Librarian(1L, "LIBRARIAN", AccountStatus.ACTIVE);
        librarian = (Librarian) account;
        accounts.put(account.id, account);
    }
    
    // logging using given data
    public boolean login() {
        Scanner userInput = new Scanner(System.in);
                    
        // a user with account is trying to log in
        System.out.print("Enter your member id: ");
        long memberId = userInput.nextLong();
                    
        if (accounts.containsKey(memberId)) {
            System.out.print("Enter your password: ");
            String memberPass = userInput.next();
                        
            if (memberPass.equals(accounts.get(memberId).password)) { 
                return true;
            } else {
                System.out.println("Wrong password. Try again!");
                login();
            }
        } else {
            System.out.println("Invalid member id. Try again!");
            login();
        }
        return false;
    }
    
    // creating a new account
    public void signup() {
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
