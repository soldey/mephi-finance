package org.soldey.finance.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class User {
    private final UUID id;
    private final String login;
    private String password;

    public UUID id() {
        return this.id;
    }

    public String login() {
        return this.login;
    }

    public String passwordHash() {
        return this.password;
    }

    public User(UUID id,
                String login,
                String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            this.password = new String(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
        this.login = login;
        this.id = id;
    }


}
