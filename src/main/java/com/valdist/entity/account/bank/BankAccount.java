package com.valdist.entity.account.bank;

import com.valdist.entity.account.Account;
import com.valdist.entity.transaction.Transaction;
import com.valdist.exception.account.AccountException;
import com.valdist.exception.account.InsufficientFundsException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

/*
 * Реализовать toString() у класса Builder
 */

//класс, описывающий логику банковского аккаунта
public class BankAccount extends Account {
    //установленный овердрафт
    protected final BigDecimal initialOverdraft;
    //баланс личных средств
    protected BigDecimal privateMoneyBalance;
    //использованный овердрафт
    protected BigDecimal usedOverdraft;

    //конструктор для аккаунта с овердрафтом
    protected BankAccount(BigInteger accountNumber, String name, BigDecimal privateMoneyBalance, BigDecimal initialOverdraft, String currency) {
        super(accountNumber, name, privateMoneyBalance.add(initialOverdraft), currency);
        usedOverdraft = BigDecimal.ZERO;
        this.initialOverdraft = initialOverdraft;
        this.privateMoneyBalance = privateMoneyBalance;
    }

    //метод, производящий операции с балансом банковского аккаунта
    @Override
    public void makeTransaction(Transaction transaction) throws AccountException {
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) == 0) makeServiceTransaction(transaction);
            //если баланс с установленным овердрафтом
        else if (initialOverdraft.compareTo(BigDecimal.ZERO) != 0) {
            //если транзакция - пополнение аккаунта
            if (transaction.getAmount().compareTo(BigDecimal.ZERO) > 0) refill(transaction);
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
        if (transaction.getAmount().add(balance).compareTo(BigDecimal.ZERO) < 0)
            addFailedTransaction(transaction, new InsufficientFundsException());
        //обновить баланс
        balance = balance.add(transaction.getAmount());
    }

    //снятие с аккаунта c установленным овердрафтом
    protected void withdraw(Transaction amount) throws AccountException {
        //если транзакция превышает весь доступный баланс - бросить исключение BalanceDoesNotHaveEnoughFunds и добавить в список неуспешных транзакций
        if (amount.getAmount().add(balance).compareTo(BigDecimal.ZERO) < 0)
            addFailedTransaction(amount, new InsufficientFundsException());
        //считать разницу между балансом личных средств и списанием
        BigDecimal residue = privateMoneyBalance.add(amount.getAmount());
        //если остаток < 0 -> обновить использованный овердрафт, установить баланс личных средств = 0
        if (residue.compareTo(BigDecimal.ZERO) < 0) {
            usedOverdraft = usedOverdraft.add(residue);
            privateMoneyBalance = BigDecimal.ZERO;
            //если остаток > 0 -> обновить баланс личных средств, установить использованный овердрафт = 0
        } else {
            privateMoneyBalance = residue;
            usedOverdraft = BigDecimal.ZERO;
        }
        //обновить доступний баланс после успешной транзакции
        balance = privateMoneyBalance.add(initialOverdraft).add(usedOverdraft);
    }

    //пополнение аккаунта c установленным овердрафтом
    protected void refill(Transaction transaction) {
        //считать разницу между использованным овердрафтом и пополнением
        BigDecimal residue = usedOverdraft.add(transaction.getAmount());
        //если остаток > 0 -> обновить баланс личных средств, установить использованный овердрафт = 0
        if (residue.compareTo(BigDecimal.ZERO) > 0) {
            privateMoneyBalance = privateMoneyBalance.add(residue);
            usedOverdraft = BigDecimal.ZERO;
            //если остаток < 0 -> обновить использованный овердрафт
        } else usedOverdraft = residue;
        //обновить доступний баланс после успешной транзакции
        balance = privateMoneyBalance.add(initialOverdraft).add(usedOverdraft);
    }

    //получить установленный овердрафт
    public BigDecimal getInitialOverdraft() {
        return initialOverdraft;
    }

    //получить использованный овердрафт
    public BigDecimal getUsedOverdraft() {
        return usedOverdraft;
    }

    @Override
    public String toString() {
        return "ACCOUNT NUMBER: " + accountNumber +
                ", ACCOUNT NAME: " + name +
                ", AVAILABLE BALANCE: " + balance +
                ", PERSONAL BALANCE: " + privateMoneyBalance +
                ", USED OVERDRAFT: " + usedOverdraft.multiply(BigDecimal.valueOf(-1)) +
                ", INITIAL OVERDRAFT: " + initialOverdraft +
                ", AMOUNT OF TRANSACTIONS: " + transactions.size();
    }

    public static final class Builder extends AccountBuilder<BankAccount> {
        private BigDecimal privateMoneyBalance;
        private BigDecimal initialOverdraft = BigDecimal.ZERO;
        private BankAccount account;

        public void setPrivateMoneyBalance(BigDecimal privateMoneyBalance) {
            this.privateMoneyBalance = privateMoneyBalance;
        }

        public void setInitialOverdraft(BigDecimal initialOverdraft) {
            this.initialOverdraft = initialOverdraft;
        }

        @Override
        protected boolean isInitialized() {
            return super.isInitialized() && privateMoneyBalance.compareTo(BigDecimal.ZERO) >= 0 && initialOverdraft.compareTo(BigDecimal.ZERO) >= 0;
        }

        public Optional<BankAccount> build() {
            if (account == null && isInitialized())
                account = new BankAccount(accountNumber, name, privateMoneyBalance, initialOverdraft, currency);
            return Optional.ofNullable(account);
        }
    }
}

