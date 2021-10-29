package com.raf.sk.specification.config;

import com.google.gson.Gson;
import com.raf.sk.specification.core.Core;
import com.raf.sk.specification.repository.limitations.INodeLimitation;
import com.raf.sk.specification.user.builder.UserBuilder;

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

    /**
     * Set ograničenja {@link INodeLimitation}.
     */
    private Set<INodeLimitation> limitations = new HashSet<>();

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
    public UserBuilder getUser(String username) {
        for (UserBuilder userBuilder : users) {
            if (userBuilder.getUsername().equals(username)) {
                return userBuilder;
            }
        }
        return null;
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
    public void addLimitation(INodeLimitation nodeLimitation) {
        if (this.limitations.contains(nodeLimitation)) return;
        this.limitations.add(nodeLimitation);
        save();
    }

    @Override
    public Collection<INodeLimitation> getLimitations() {
        return this.limitations;
    }

    @Override
    public void deleteLimitation(INodeLimitation nodeLimitation) {
        this.limitations.remove(nodeLimitation);
        save();
    }

    @Override
    public String toJson() {
        for (INodeLimitation e : limitations)
            e.setPath(e.getHost().getPath());
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
