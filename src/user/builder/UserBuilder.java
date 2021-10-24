package user.builder;

import java.util.Collection;

/**
 * Builder klasa za korisnika.
 */
public class UserBuilder {

    private String username;

    private String password;

    private Collection<PrivilegeBuilder> privileges;

    private boolean authenticated = false;

    public UserBuilder(String username, String password, Collection<PrivilegeBuilder> privileges, boolean authenticated) {
        this.username = username;
        this.password = password;
        this.privileges = privileges;
        this.authenticated = authenticated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<PrivilegeBuilder> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Collection<PrivilegeBuilder> privileges) {
        this.privileges = privileges;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
