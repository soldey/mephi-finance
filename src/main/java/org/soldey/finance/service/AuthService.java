package org.soldey.finance.service;

import org.soldey.finance.model.User;

import javax.management.InstanceNotFoundException;
import javax.naming.AuthenticationException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class AuthService {
    private User loggedIn = null;
    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public void login(String login, String password) throws AuthenticationException, InstanceNotFoundException {
        if (loggedIn != null) {
            throw new AuthenticationException("someone is already authorized");
        }
        User user = userService.selectOneByLogin(login);

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            if (!user.passwordHash().equals(new String(messageDigest.digest()))) {
                throw new AuthenticationException("password doesnt match");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }

        loggedIn = user;
    }

    public void logout() {
        loggedIn = null;
    }

    public User getLoggedIn() throws AuthenticationException {
        if (this.loggedIn == null) {
            throw new AuthenticationException("noone authorized");
        }
        return this.loggedIn;
    }
}
