package org.soldey.finance.service;

import org.soldey.finance.model.Category;
import org.soldey.finance.repository.WalletRepository;
import org.soldey.finance.model.Wallet;

import javax.management.InstanceNotFoundException;
import java.util.UUID;

public class WalletService {
    private final WalletRepository walletRepository = new WalletRepository();
    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final UserService userService;

    public WalletService(TransactionService transactionService, UserService userService, CategoryService categoryService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public Wallet createOne(UUID userId) throws InstanceNotFoundException {
        if (userId == null) {
            throw new IllegalArgumentException("user cant be null");
        }

        userService.selectOne(userId);
        return walletRepository.createOne(new Wallet(userId, 0));
    }

    public Wallet selectOne(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("user cant be null");
        }
        return walletRepository.selectOne(userId);
    }

    public void income(UUID userId, int amount, String category) throws InstanceNotFoundException {
        Wallet wallet = selectOne(userId);

        transactionService.createOne(userId, amount, category, true);
        Category categoryInstance = categoryService.selectOne(userId, category);
        categoryInstance.setIncome(categoryInstance.income() + amount);
        wallet.setBalance(wallet.balance() + amount);
    }

    public void expense(UUID userId, int amount, String category) throws InstanceNotFoundException {
        Wallet wallet = selectOne(userId);
        if (wallet.balance() < amount) {
            throw new IllegalArgumentException("not enough money (no overdraft)");
        }

        transactionService.createOne(userId, amount, category, false);
        Category categoryInstance = categoryService.selectOne(userId, category);
        categoryInstance.setSpent(categoryInstance.spent() + amount);
        wallet.setBalance(wallet.balance() - amount);
        if (categoryInstance.budget() - categoryInstance.spent() + categoryInstance.income() < 0) {
            userService.notify(userId, "You are out of your budget for " + categoryInstance.name());
        }
    }

    public void report(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("user cant be null");
        }
        int totalIncome = transactionService.selectAllByUserId(userId)
                .stream().filter(t -> t.isIncome()).mapToInt(t -> t.amount()).sum();
        int totalExpanses = transactionService.selectAllByUserId(userId)
                .stream().filter(t -> !t.isIncome()).mapToInt(t -> t.amount()).sum();

        System.out.println("Отчёт по кошельку пользователя " + userId);

        System.out.println("Общие доходы: " + totalIncome);
        System.out.println("Доходы по категориям:");
        for (Category category: categoryService.selectAll(userId)) {
            if (category.income() > 0) {
                System.out.println(category.name() + " - " + category.income());
            }
        }

        System.out.println("Общие расходы: " + totalExpanses);
        System.out.println("Бюджет по категориям:");
        for (Category category: categoryService.selectAll(userId)) {
            if (category.budget() == 0) continue;
            System.out.print(category.name() + ": ");
            if (category.spent() > 0) {
                System.out.print(category.spent() + ", ");
            }
            System.out.println("оставшийся бюджет - " + (category.budget() - category.spent()));
        }
    }
}
