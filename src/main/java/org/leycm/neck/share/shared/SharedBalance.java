package org.leycm.neck.share.shared;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class SharedBalance {
    public static class Transaction {
        private final BigDecimal amount;
        private final String reason;
        private final LocalDateTime timestamp;
        private final String createdBy;
        private final TransactionType type;

        public enum TransactionType {
            INCOME, EXPENSE
        }

        public Transaction(BigDecimal amount, String reason, String createdBy, TransactionType type) {
            this.amount = amount;
            this.reason = reason;
            this.timestamp = LocalDateTime.now();
            this.createdBy = createdBy;
            this.type = type;
        }

        public BigDecimal getAmount() { return amount; }
        public String getReason() { return reason; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getCreatedBy() { return createdBy; }
        public TransactionType getType() { return type; }
    }

    private final List<Transaction> transactions;

    private SharedBalance() {
        this.transactions = new ArrayList<>();
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull SharedBalance empty() {
        return new SharedBalance();
    }

    public BigDecimal getBalance() {
        return transactions.stream()
                .map(t -> t.getType() == Transaction.TransactionType.INCOME ?
                        t.getAmount() : t.getAmount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addIncome(BigDecimal amount, String reason) {
        addIncome(amount, reason, "system");
    }

    public void addIncome(@NotNull BigDecimal amount, String reason, String createdBy) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Income amount must be positive");
        }
        transactions.add(new Transaction(amount, reason, createdBy, Transaction.TransactionType.INCOME));
    }

    public void addExpense(BigDecimal amount, String reason) {
        addExpense(amount, reason, "system");
    }

    public void addExpense(@NotNull BigDecimal amount, String reason, String createdBy) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Expense amount must be positive");
        }
        transactions.add(new Transaction(amount, reason, createdBy, Transaction.TransactionType.EXPENSE));
    }

    public void transfer(SharedBalance target, BigDecimal amount, String reason) {
        transfer(target, amount, reason, "system");
    }

    public void transfer(SharedBalance target, @NotNull BigDecimal amount, String reason, String createdBy) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        if (getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance for transfer");
        }

        this.addExpense(amount, "Transfer: " + reason, createdBy);
        target.addIncome(amount, "Transfer from: " + reason, createdBy);
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public List<Transaction> getTransactionsByType(Transaction.TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .toList();
    }

    public BigDecimal getTotalIncome() {
        return getTransactionsByType(Transaction.TransactionType.INCOME)
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalExpenses() {
        return getTransactionsByType(Transaction.TransactionType.EXPENSE)
                .stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
