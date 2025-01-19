package org.soldey.finance.service;

import org.soldey.finance.repository.TransactionRepository;
import org.soldey.finance.model.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionService {
    private final TransactionRepository transactionRepository = new TransactionRepository();

    public Transaction createOne(UUID userId, int amount, String category, boolean isIncome) {
        return transactionRepository.createOne(new Transaction(amount, category, isIncome, userId));
    }

    public List<Transaction> selectAllByUserId(UUID userId) {
        return transactionRepository.selectAllByUserId(userId);
    }
}
