
package lms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Swega
 */
class Catalog implements Search {
    
    private List<Book> booksInLibrary;
    private HashMap<String, Book> booksByTitle;
    private HashMap<String, Book> booksByAuthor;
    private HashMap<String, Book> booksBySubject;
    
    // creating a single catalog and updating it whenever a change happens
    private Catalog(List<Book> books) {
        this.booksInLibrary = books;
    }

    @Override
    public Book searchByTitle(String title) {
        return booksInLibrary.get(0);
    }

    @Override
    public Book searchByAuthor(String aName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Book searchBySubject(String subject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // Searching Books on basis of title, Subject or Author
    public ArrayList<Book> searchForBooks() throws IOException
    {
        String choice;
        String title = "", subject = "", author = "";

        Scanner sc = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true)
        {
            System.out.println("\nEnter either '1' or '2' or '3' for search by Title, Subject or Author of Book respectively: ");
            choice = sc.next();

            if (choice.equals("1") || choice.equals("2") || choice.equals("3"))
                break;
            else
                System.out.println("\nWrong Input!");
        }

        if (choice.equals("1"))
        {
            System.out.println("\nEnter the Title of the Book: ");
            title = reader.readLine();
        }

        else if (choice.equals("2"))
        {
            System.out.println("\nEnter the Subject of the Book: ");
            subject = reader.readLine();
        }

        else
        {
            System.out.println("\nEnter the Author of the Book: ");
            author = reader.readLine();
        }

        ArrayList<Book> matchedBooks = new ArrayList();

        //Retrieving all the books which matched the user's search query
        for(int i = 0; i < booksInLibrary.size(); i++)
        {
            Book b = booksInLibrary.get(i);

            if (choice.equals("1"))
            {
                if (b.getTitle().equals(title))
                    matchedBooks.add(b);
            }
            else if (choice.equals("2"))
            {
                if (b.getSubject().equals(subject))
                    matchedBooks.add(b);
            }
            else
            {
                if (b.getAuthorName().equals(author))
                    matchedBooks.add(b);
            }
        }

        //Printing all the matched Books
        if (!matchedBooks.isEmpty())
        {
            System.out.println("\nThese books are found: \n");

            System.out.println("------------------------------------------------------------------------------");
            System.out.println("No.\t\tTitle\t\t\tAuthor\t\t\tSubject");
            System.out.println("------------------------------------------------------------------------------");

            for (int i = 0; i < matchedBooks.size(); i++)
            {
                System.out.print(i + "-" + "\t\t");
//                matchedBooks.get(i).printInfo();
                System.out.print("\n");
            }

            return matchedBooks;
        }
        else
        {
            System.out.println("\nSorry. No Books were found related to your query.");
            return null;
        }
    }
}
