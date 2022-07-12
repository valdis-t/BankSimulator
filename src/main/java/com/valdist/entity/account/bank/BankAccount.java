package com.valdist.entity.account.bank;

import com.valdist.entity.account.Account;
import com.valdist.exception.account.AccountException;
import com.valdist.exception.account.InsufficientFundsException;
import com.valdist.entity.transaction.Transaction;

import java.util.Optional;

/*
* Реализовать toString() у класса Builder
 */

//класс, описывающий логику банковского аккаунта
public class BankAccount extends Account {

    //баланс личных средств
    protected long privateMoneyBalance;

    //установленный овердрафт
    protected final long initialOverdraft;

    //использованный овердрафт
    protected long usedOverdraft;

    //конструктор для аккаунта с овердрафтом
    protected BankAccount(long accountNumber, String name, long privateMoneyBalance, long initialOverdraft, String currency) {
        super(accountNumber, name, privateMoneyBalance + initialOverdraft, currency);
        usedOverdraft = 0;
        this.initialOverdraft = initialOverdraft;
        this.privateMoneyBalance = privateMoneyBalance;
    }

    //метод, производящий операции с балансом банковского аккаунта
    @Override
    public void makeTransaction(Transaction transaction) throws AccountException {
        if (transaction.getAmount() == 0) makeServiceTransaction(transaction);
            //если баланс с установленным овердрафтом
        else if (initialOverdraft != 0) {
            //если транзакция - пополнение аккаунта
            if (transaction.getAmount() > 0) refill(transaction);
                //если транзакция - снятие аккаунта
            else withdraw(transaction);
            //если баланс без установленного овердрафта
        } else addToBalance(transaction);
        //добавить транзакцию в список транзакций
        transactions.add(transaction);
    }

    //featured method, mock
    @Override
    public void makeServiceTransaction(Transaction transaction) {
    }

    //операция с балансом без установленного овердрафта
    protected void addToBalance(Transaction transaction) throws AccountException {
        //если транзакция отрицательная и превышает весь доступный баланс - бросить исключение BalanceDoesNotHaveEnoughFunds и добавить в список неуспешных транзакций
        if (transaction.getAmount() + balance < 0)
            addFailedTransaction(transaction, new InsufficientFundsException());
        //обновить баланс
        balance += transaction.getAmount();
    }

    //снятие с аккаунта c установленным овердрафтом
    protected void withdraw(Transaction amount) throws AccountException {
        long residue;
        //если транзакция превышает весь доступный баланс - бросить исключение BalanceDoesNotHaveEnoughFunds и добавить в список неуспешных транзакций
        if (Math.abs(amount.getAmount()) > balance) addFailedTransaction(amount, new InsufficientFundsException());
        //считать разницу между балансом личных средств и списанием
        residue = privateMoneyBalance + amount.getAmount();
        //если остаток < 0 -> обновить использованный овердрафт, установить баланс личных средств = 0
        if (residue < 0) {
            usedOverdraft += residue;
            privateMoneyBalance = 0;
            //если остаток > 0 -> обновить баланс личных средств, установить использованный овердрафт = 0
        } else {
            privateMoneyBalance = residue;
            usedOverdraft = 0;
        }
        //обновить доступний баланс после успешной транзакции
        balance = privateMoneyBalance + initialOverdraft + usedOverdraft;
    }

    //пополнение аккаунта c установленным овердрафтом
    protected void refill(Transaction transaction) {
        long residue;
        //считать разницу между использованным овердрафтом и пополнением
        residue = usedOverdraft + transaction.getAmount();
        //если остаток > 0 -> обновить баланс личных средств, установить использованный овердрафт = 0
        if (residue > 0) {
            privateMoneyBalance += residue;
            usedOverdraft = 0;
            //если остаток < 0 -> обновить использованный овердрафт
        } else usedOverdraft = residue;
        //обновить доступний баланс после успешной транзакции
        balance = privateMoneyBalance + initialOverdraft + usedOverdraft;
    }

    //получить установленный овердрафт
    public long getInitialOverdraft() {
        return initialOverdraft;
    }

    //получить использованный овердрафт
    public long getUsedOverdraft() {
        return usedOverdraft;
    }

    @Override
    public String toString() {
        return "ACCOUNT NUMBER: " + accountNumber +
                ", ACCOUNT NAME: " + name +
                ", AVAILABLE BALANCE: " + (usedOverdraft + privateMoneyBalance + initialOverdraft) +
                ", PERSONAL BALANCE: " + privateMoneyBalance +
                ", USED OVERDRAFT: " + (-usedOverdraft) +
                ", INITIAL OVERDRAFT: " + initialOverdraft +
                ", AMOUNT OF TRANSACTIONS: " + transactions.size();
    }

    public static final class Builder extends AccountBuilder<BankAccount> {
        private long privateMoneyBalance;
        private long initialOverdraft = 0;
        private BankAccount account;

        public void setPrivateMoneyBalance(long privateMoneyBalance) {
            this.privateMoneyBalance = privateMoneyBalance;
        }

        public void setInitialOverdraft(long initialOverdraft) {
            this.initialOverdraft = initialOverdraft;
        }

        @Override
        protected boolean isInitialized() {
            return super.isInitialized() && privateMoneyBalance >= 0 && initialOverdraft >= 0;
        }

        public Optional<BankAccount> build() {
            if (account == null && isInitialized())
                account = new BankAccount(accountNumber, name, privateMoneyBalance, initialOverdraft, currency);
            return Optional.ofNullable(account);
        }
    }
}

