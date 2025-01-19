package org.soldey.finance.repository;

import org.soldey.finance.model.Transaction;
import java.util.*;

public class TransactionRepository {
    private final Map<UUID, List<Transaction>> transactions = new HashMap<>();

    public Transaction createOne(Transaction transaction) {
        transactions.computeIfAbsent(transaction.userId(), k -> new ArrayList<>());
        transactions.get(transaction.userId()).add(transaction);
        return transaction;
    }

    public List<Transaction> selectAllByUserId(UUID userId) {
        List<Transaction> result = transactions.get(userId);
        return result == null ? new ArrayList<>() : result;
    }
}
