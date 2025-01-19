package org.soldey.finance.service;

import org.soldey.finance.model.User;
import org.soldey.finance.repository.UserRepository;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.*;

public class UserService {
    private final UserRepository userRepository = new UserRepository();
    private final Map<UUID, List<String>> notifications = new HashMap<>();

    public User createOne(String login, String password) throws InstanceAlreadyExistsException {
        if (userRepository.selectOneByLogin(login) != null) {
            throw new InstanceAlreadyExistsException("user exists");
        }
        return userRepository.createOne(new User(UUID.randomUUID(), login, password));
    }

    public User selectOne(UUID userId) throws InstanceNotFoundException {
        User found = userRepository.selectOne(userId);
        if (found == null) {
            throw new InstanceNotFoundException("user doesnt exist");
        }
        return found;
    }

    public User selectOneByLogin(String login) throws InstanceNotFoundException {
        User found = userRepository.selectOneByLogin(login);
        if (found == null) {
            throw new InstanceNotFoundException("user doesnt exist");
        }
        return found;
    }

    public void notify(UUID userId, String message) {
        if (userId == null) {
            throw new IllegalArgumentException("user cant be null");
        }
        notifications.computeIfAbsent(userId, k -> new ArrayList<>());
        notifications.get(userId).add(message);
        System.out.println("* New notification");
    }

    public void showNotifications(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("user cant be null");
        }
        notifications.computeIfAbsent(userId, k -> new ArrayList<>());
        for (String message: notifications.get(userId)) {
            System.out.println(" + " + message);
        }
    }

    public void clearNotifications(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("user cant be null");
        }
        notifications.put(userId, new ArrayList<>());
    }
}
