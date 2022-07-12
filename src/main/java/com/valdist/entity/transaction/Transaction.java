package com.valdist.entity.transaction;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

//класс, описывающий логику транзакции (контейнера денежной операции)
public class Transaction implements Comparable<Transaction> {
    private final LocalDate date;
    private final String name;
    private final long amount;
    private final Type type;
    private final String currency;

    private Transaction(long amount, LocalDate date, Type type, String currency, String name) {
        this.amount = amount;
        this.name = name;
        this.date = date;
        this.type = type;
        this.currency = currency;
    }

    //получить builder транзакции
    public static Builder getBuilder() {
        return new Builder();
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public long getAmount() {
        return amount;
    }

    //логика реализации compareTo класса всегда будет возвращать или отрицательное число, или положительное
    //так как могуть быть несколько одинаковых транзакций на дату
    @Override
    public int compareTo(Transaction o) {
        if (this.date.isEqual(o.date)) {
            if (this.name.equalsIgnoreCase(o.name)) {
                if (this.amount < o.amount) return -1;
                else return 1;
            }
            return this.name.compareTo(o.name);
        }
        return this.date.compareTo(o.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.amount, amount) == 0 && Objects.equals(date, that.date) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, name, amount);
    }

    @Override
    public String toString() {
        return "TRANSACTION: DATE " + date.toString() + ", NAME " + name + ", AMOUNT " + amount;
    }

    public static enum Type {POS, EPOS, ATM, P2P, SEP, OBLIGATION, SERVICE}

    //builder для пошагового создания транзакции
    public static class Builder {
        private LocalDate date;
        private String name;
        private long amount = 0;
        private Type type;
        private String currency;

        private Builder() {
        }

        public Builder setLocalDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public Builder setAmount(long amount) {
            this.amount = amount;
            return this;
        }

        public Optional<Transaction> build() {
            boolean isInitialized = date != null && name != null && !name.isBlank() && type != null;
            return Optional.ofNullable(isInitialized ? new Transaction(amount, date, type, currency, name) : null);
        }
    }
}
