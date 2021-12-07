
package lms;

/**
 *
 * @author Swega
 */
abstract class Account {
    long id;
    String password;
    AccountStatus status;
    
    public Account(Long aid, String pass, AccountStatus aStatus){
        id = aid;
        password = pass;
        status = aStatus;
    }
}
