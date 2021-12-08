
package lms;

/**
 *
 * @author Swega
 */
interface Search {
    
    // searching with title keyword
    public Book searchByTitle(String title);
    
    // seaching with author keyword
    public Book searchByAuthor(String aName);
    
    // searching with subject keyword
    public Book searchBySubject(String subject);
}
