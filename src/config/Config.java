package config;

import com.google.gson.Gson;
import user.builder.UserBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementacija IConfig interfejsa.
 */
public class Config implements IConfig {

    /**
     * Set bildera korisnika {@link UserBuilder}.
     */
    private Set<UserBuilder> users = new HashSet<>();

    @Override
    public void addUser(UserBuilder userBuilder) {
        users.add(userBuilder);
    }

    @Override
    public Collection<UserBuilder> getUsers() {
        return users;
    }

    @Override
    public void updateUser(UserBuilder userBuilder) {
        if (!users.contains(userBuilder)) return;
        users.remove(userBuilder);
        users.add(userBuilder);
    }

    @Override
    public void deleteUser(UserBuilder userBuilder) {
        users.remove(userBuilder);
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }
}
