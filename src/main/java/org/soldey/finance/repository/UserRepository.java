package org.soldey.finance.repository;

import org.soldey.finance.model.User;

import java.util.*;

public class UserRepository {
    private final Map<UUID, User> users = new HashMap<>();

    public User createOne(User user) {
        users.put(user.id(), user);
        return user;
    }

    public User selectOne(UUID userId) {
        return users.get(userId);
    }

    public User selectOneByLogin(String login) {
        List<User> result = users.values().stream().filter((user) -> user.login().equals(login)).toList();
        return result.isEmpty() ? null : result.get(0);
    }
}
