package org.soldey.finance.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Transaction {
    private final int amount;
    private final String category;
    private final boolean isIncome;
    private final Date createdAt;
    private final UUID userId;

    public Transaction(int amount,
                       String category,
                       boolean isIncome,
                       UUID userId) {
        this.amount = amount;
        this.category = category;
        this.isIncome = isIncome;
        this.createdAt = new Date();
        this.userId = userId;
    }

    public int amount() {
        return amount;
    }

    public String category() {
        return category;
    }

    public boolean isIncome() {
        return isIncome;
    }

    public Date createdAt() {
        return new Date(createdAt.getTime());
    }

    public UUID userId() {
        return this.userId;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String type = isIncome ? "Доход" : "Расход";
        return String.format("%s: %d, Категория: %s, Дата: %s", type, amount, category, dateFormat.format(createdAt));
    }
}
