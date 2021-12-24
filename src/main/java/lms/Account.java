
package lms;

/**
 * @author Swega
 */
abstract class Account {
    private final String username;
    private String password;
    private AccountStatus status;

    public Account(String uname, String pass, AccountStatus aStatus) {
        username = uname;
        password = pass;
        status = aStatus;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void printInfo() {
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println("Username      : " + username);
        System.out.println("Account Status: " + status);
        System.out.println("----------------------------------------------------------------------------------");
    }

    @Override
    public String toString() {
        return "----------------------------------------------------------------------------------\n" +
                "Username      : " + username + "\n" +
                "Account Status: " + status + "\n" +
                "----------------------------------------------------------------------------------\n";
    }
}
