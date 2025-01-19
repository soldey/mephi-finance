package org.soldey.finance.repository;

import org.soldey.finance.model.Wallet;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WalletRepository {
    private final Map<UUID, Wallet> wallets = new HashMap<>();

    public Wallet createOne(Wallet wallet) {
        wallets.put(wallet.userId(), wallet);
        return wallet;
    }

    public Wallet selectOne(UUID userId) {
        return wallets.get(userId);
    }
}
