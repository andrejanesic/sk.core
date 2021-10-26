package config;

import com.google.gson.Gson;
import core.Core;
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
        if (userBuilder.getUsername() == null || userBuilder.getUsername().length() == 0 ||
                userBuilder.getPassword() == null || userBuilder.getPassword().length() == 0)
            return;
        users.add(userBuilder);
        save();
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
        save();
    }

    @Override
    public void deleteUser(UserBuilder userBuilder) {
        users.remove(userBuilder);
        save();
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void save() {
        if (Core.getInstance().ConfigManager().getConfig() != null
                && Core.getInstance().ConfigManager().getConfig().equals(this))
            Core.getInstance().ConfigManager().saveConfig();
    }
}
