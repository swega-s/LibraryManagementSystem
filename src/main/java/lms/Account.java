
package lms;

/**
 *
 * @author Swega
 */
abstract class Account {
    String username;
    String password;
    AccountStatus status;
    
    public Account(String uname, String pass, AccountStatus aStatus){
        username = uname;
        password = pass;
        status = aStatus;
    }
}
