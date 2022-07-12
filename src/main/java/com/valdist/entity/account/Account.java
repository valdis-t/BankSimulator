package com.valdist.entity.account;

import com.valdist.exception.account.AccountException;
import com.valdist.entity.transaction.Transaction;

import java.util.*;

//класс, описываюзий логику абстрактоного аккаунта
public abstract class Account {
    //номер счета аккаунта
    protected final long accountNumber;
    //имя аккаунта
    protected final String name;
    //валюта аккаунта
    protected final String currency;
    //список успешных операций с аккаунтом
    protected final Collection<Transaction> transactions;
    //список неуспешных операций с аккаунтом, mock
    protected final Map<Transaction, AccountException> failedTransaction;
    //баланс личных средств
    protected long balance;

    protected Account(long accountNumber, String name, long balance, String currency) {
        transactions = new TreeSet<>();
        failedTransaction = new HashMap<>();
        this.accountNumber = accountNumber;
        this.name = name;
        this.currency = currency;
        this.balance = balance;
    }

    //абстрактный метод совершения транзакции со счета
    public abstract void makeTransaction(Transaction transaction) throws AccountException;

    //абстрактный метод совершения сервисной транзакции
    public abstract void makeServiceTransaction(Transaction transaction);

    //зарегистрируй неуспешную транзакцию и брось исключение
    protected void addFailedTransaction(Transaction transaction, AccountException exception) throws AccountException {
        failedTransaction.put(transaction, exception);
        throw exception;
    }

    //получить номер аккаунта
    public final long getAccountNumber() {
        return accountNumber;
    }

    //получить имя аккаунта
    public final String getName() {
        return name;
    }

    //получить баланс личных средств
    public final long getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        return ((Account) o).getAccountNumber() == this.accountNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    protected static abstract class AccountBuilder<T extends Account> {
        protected long accountNumber;
        protected String name;
        protected String currency;

        public abstract Optional<T> build();

        protected boolean isInitialized() {
            return !name.isBlank() &&
                    accountNumber > 0 &&
                    !currency.isBlank();
        }

        public void setAccountNumber(long accountNumber) {
            this.accountNumber = accountNumber;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

}

