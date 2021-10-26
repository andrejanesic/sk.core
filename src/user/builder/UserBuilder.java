package user.builder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Builder klasa za korisnika.
 */
public class UserBuilder {

    private String username;

    private String password;

    private Collection<PrivilegeBuilder> privileges;

    public UserBuilder(String username, String password) {
        this.username = username;
        this.password = password;
        this.privileges = new HashSet<>();
    }

    public UserBuilder(String username, String password, Collection<PrivilegeBuilder> privileges) {
        this.username = username;
        this.password = password;
        this.privileges = privileges;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserBuilder)) return false;
        UserBuilder that = (UserBuilder) o;
        return getUsername().equals(that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }
}
