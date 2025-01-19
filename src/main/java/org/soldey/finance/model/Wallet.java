package org.soldey.finance.model;

import java.util.*;

public class Wallet {
    private final UUID userId;
    private int balance;

    public Wallet(UUID userId, int balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public UUID userId() {
        return this.userId;
    }

    public int balance() {
        return this.balance;
    }

    public void setBalance(int amount) {
        this.balance = amount;
    }
}
