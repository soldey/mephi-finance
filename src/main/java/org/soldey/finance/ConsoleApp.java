package org.soldey.finance;

import org.soldey.finance.model.Category;
import org.soldey.finance.model.User;
import org.soldey.finance.service.*;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private static final UserService userService = new UserService();
    private static final AuthService authService = new AuthService(userService);
    private static final CategoryService categoryService = new CategoryService();
    private static final TransactionService transactionService = new TransactionService();
    private static final WalletService walletService = new WalletService(
            transactionService, userService, categoryService
    );

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        help();


        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();

            if (command.startsWith("stop")) {
                break;
            }
            doSomething(command);
        }

        scanner.close();
    }

    private static void help() {
        System.out.println("Commands:");
        System.out.println("register <login> <password>");
        System.out.println("login <user id>");
        System.out.println("logout");
        System.out.println();
        System.out.println("notifications - shows list of your notifications");
        System.out.println("clear-notifications");
        System.out.println();
        System.out.println("create-category <name> (optional)<budget>");
        System.out.println("change-budget <name> <budget>");
        System.out.println("categories - shows created categories");
        System.out.println();
        System.out.println("income <category name> <amount>");
        System.out.println("expanse <category name> <amount>");
        System.out.println("report");
        System.out.println("help - shows this list");
    }

    private static void doSomething(String command) {
        String[] commandSplit = command.split(" ");
        try {
            switch (commandSplit[0]) {
                case "register":
                    register(commandSplit);
                    break;
                case "login":
                    login(commandSplit);
                    break;
                case "logout":
                    logout();
                    break;
                case "notifications":
                    notifications();
                    break;
                case "clear-notifications":
                    clearNotifications();
                    break;
                case "create-category":
                    createCategory(commandSplit);
                    break;
                case "change-budget":
                    changeBudget(commandSplit);
                    break;
                case "categories":
                    categories();
                    break;
                case "income":
                    income(commandSplit);
                    break;
                case "expense":
                    expense(commandSplit);
                    break;
                case "report":
                    report();
                    break;
                case "help":
                    help();
                    break;
                default:
                    System.out.println("no such command");
            }
        } catch (NumberFormatException e) {
            System.out.println("argument is not a number");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void register(String[] args) throws InstanceAlreadyExistsException, InstanceNotFoundException {
        if (args.length < 3) {
            throw new IllegalArgumentException("provide login and password");
        }
        User user = userService.createOne(args[1], args[2]);
        walletService.createOne(user.id());
        System.out.println("new user created: " + user.id());
    }

    private static void login(String[] args) throws AuthenticationException, InstanceNotFoundException {
        if (args.length < 3) {
            throw new IllegalArgumentException("provide login and password");
        }
        authService.login(args[1], args[2]);
        System.out.println("logged in as: " + authService.getLoggedIn().id());
    }

    private static void logout() {
        authService.logout();
    }

    private static void notifications() throws AuthenticationException {
        userService.showNotifications(authService.getLoggedIn().id());
    }

    private static void clearNotifications() throws AuthenticationException {
        userService.clearNotifications(authService.getLoggedIn().id());
    }

    private static void createCategory(String[] args) throws InstanceAlreadyExistsException, AuthenticationException {
        if (args.length < 2) {
            throw new IllegalArgumentException("provide name (and budget)");
        }
        if (args.length == 2) {
            Category category = categoryService.createOne(authService.getLoggedIn().id(), args[1], 0);
            System.out.println("category created: " + category.name());
        } else {
            Category category = categoryService.createOne(authService.getLoggedIn().id(), args[1], Integer.parseInt(args[2]));
            System.out.println("category created: " + category.name());
        }
    }

    private static void changeBudget(String[] args) throws InstanceNotFoundException, AuthenticationException {
        if (args.length < 3) {
            throw new IllegalArgumentException("provide name and budget");
        }
        categoryService.setBudget(authService.getLoggedIn().id(), args[1], Integer.parseInt(args[2]));
        System.out.println("category budget changed to " + args[2]);
    }

    private static void categories() throws AuthenticationException {
        List<Category> categories = categoryService.selectAll(authService.getLoggedIn().id());

        for (Category category: categories) {
            System.out.println(" . " + category.name() + ", budget - " + category.budget());
        }
    }

    private static void income(String[] args) throws InstanceNotFoundException, AuthenticationException {
        if (args.length < 3) {
            throw new IllegalArgumentException("provide category name and amount");
        }
        walletService.income(authService.getLoggedIn().id(), Integer.parseInt(args[2]), args[1]);
    }

    private static void expense(String[] args) throws InstanceNotFoundException, AuthenticationException {
        if (args.length < 3) {
            throw new IllegalArgumentException("provide category name and amount");
        }
        walletService.expense(authService.getLoggedIn().id(), Integer.parseInt(args[2]), args[1]);
    }

    private static void report() throws AuthenticationException {
        walletService.report(authService.getLoggedIn().id());
    }
}
