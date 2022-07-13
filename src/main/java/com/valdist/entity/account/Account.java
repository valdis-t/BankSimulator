package com.valdist.entity.account;

import com.valdist.entity.transaction.Transaction;
import com.valdist.exception.account.AccountException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

//класс, описываюзий логику абстрактоного аккаунта
public abstract class Account {
    //номер счета аккаунта
    protected final BigInteger accountNumber;
    //имя аккаунта
    protected final String name;
    //валюта аккаунта
    protected final String currency;
    //список успешных операций с аккаунтом
    protected final Collection<Transaction> transactions;
    //список неуспешных операций с аккаунтом, mock
    protected final Map<Transaction, AccountException> failedTransaction;
    //баланс личных средств
    protected BigDecimal balance;

    protected Account(BigInteger accountNumber, String name, BigDecimal balance, String currency) {
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
    public final BigInteger getAccountNumber() {
        return accountNumber;
    }

    //получить имя аккаунта
    public final String getName() {
        return name;
    }

    //получить баланс личных средств
    public final BigDecimal getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        return ((Account) o).getAccountNumber().equals(this.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    protected static abstract class AccountBuilder<T extends Account> {
        protected BigInteger accountNumber;
        protected String name;
        protected String currency;

        public abstract Optional<T> build();

        protected boolean isInitialized() {
            return !name.isBlank() &&
                    accountNumber.compareTo(BigInteger.ZERO) > 0 &&
                    !currency.isBlank();
        }

        public void setAccountNumber(BigInteger accountNumber) {
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

