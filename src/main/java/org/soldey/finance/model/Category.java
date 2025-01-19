package org.soldey.finance.model;

public class Category {
    private String name;
    private int budget;
    private int spent;
    private int income;

    public Category(String name, int budget) {
        this.name = name;
        this.budget = budget;
        this.spent = 0;
        this.income = 0;
    }

    public String name() {
        return name;
    }

    public int budget() {
        return budget;
    }

    public int spent() {
        return spent;
    }

    public int income() {
        return income;
    }

    public void setIncome(int amount) {
        this.income = amount;
    }

    public void setSpent(int amount) {
        this.spent = amount;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return String.format("%s: бюджет %d, израсходовано %d, доход %d", name, budget, spent, income);
    }
}
