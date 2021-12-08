
package lms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author Swega
 */
class Library {
    private String name;
    private Librarian librarian;
    private HashMap<String, Account> accounts;
    private ArrayList<Book> books;
    private HashMap<Long, Book> lentBooks;
    
    // only a single instance of library is creating (a singleton pattern)
    private static Library obj;

    // private constructor
    private Library() {
        name = null;
        librarian = null;
        accounts = new HashMap();
        books = new ArrayList();
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
    public HashMap<String, Account> getAccounts() {
        return accounts;
    }
    
    // populating library details with default information
    public void populateLibrary() {
        
        // setting a default librarian
        Librarian account = new Librarian("librarian", "LIBRARIAN", AccountStatus.ACTIVE);
        if (librarian == null) {
            librarian = account;
            accounts.put(account.username, account);
        } else {
            System.out.println("Only one librarian should be assigned for a library");
        }
    }

    // asking the user whether he wishes to try again after unsuccessful login
    private boolean continueLogin() {

        Scanner userInput = new Scanner(System.in);
        System.out.println("Press c         - Try again\n" +
                "any other char - Go back or exit");
        if (userInput.next().equals("c")) {
            return login();
        } else {
            return false;
        }
    }
    
    // logging using given data
    public boolean login() {
        Scanner userInput = new Scanner(System.in);

        // a user with account is trying to log in
        System.out.print("Enter your account username: ");
        String username = userInput.next();
                    
        if (accounts.containsKey(username)) {
            System.out.print("Enter your password: ");
            String accountPass = userInput.next();
                        
            if (accountPass.equals(accounts.get(username).password)) {
                return true;
            } else {
                System.out.println("Wrong password. Try again!");
                return continueLogin();
            }
        } else {
            System.out.println("Invalid username. Try again!");
            return continueLogin();
        }
    }
    
    // creating a new account (member)
    public void createMember() {
        Scanner newUserInput = new Scanner(System.in);
        
        System.out.print("Enter a username: ");
        String username = newUserInput.next();

        while (accounts.containsKey(username)) {
            System.out.println("Username not available!");
            System.out.print("Enter a username: ");
            username = newUserInput.next();
        }
        
        System.out.print("Enter a password: ");
        String pass = newUserInput.next();

        Account newAccount = new Member(username, pass, AccountStatus.ACTIVE);
        accounts.put(username, newAccount);
    }
}
