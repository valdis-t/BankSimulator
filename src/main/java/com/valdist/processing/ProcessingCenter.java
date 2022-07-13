package com.valdist.processing;

import com.valdist.entity.account.Account;
import com.valdist.entity.transaction.Transaction;
import com.valdist.exception.account.AccountException;
import com.valdist.exception.processing.AccountHasNotFoundException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//mock processing center
public class ProcessingCenter {
    private final Map<BigInteger, Account> accounts;
    private Account currentAccount;

    public ProcessingCenter(Collection<Account> accounts) {
        this.accounts = new HashMap<>();
        accounts.forEach(account -> {
            this.accounts.put(account.getAccountNumber(), account);
            currentAccount = account;
        });
    }

    public ProcessingCenter setCurrentAccount(BigInteger accountNumber) throws AccountHasNotFoundException {
        if (accounts.containsKey(accountNumber)) currentAccount = accounts.get(accountNumber);
        else throw new AccountHasNotFoundException(accountNumber);
        return this;
    }

    public void addAccount(Account account) {
        if (!accounts.containsKey(account.getAccountNumber())) {
            accounts.put(account.getAccountNumber(), account);
        }
    }

    public boolean makeTransaction(Transaction transaction) {
        try {
            currentAccount.makeTransaction(transaction);
        } catch (AccountException e) {
            e.printStackTrace(); //********************************************************************** HARD CODE
            return false;
        }
        return true;
    }

    public Collection<BigInteger> getAccountNumbers() {
        return accounts.keySet();
    }

    //получить номер текущего аккаунта
    public BigInteger getAccountNumber() {
        return currentAccount.getAccountNumber();
    }

    //получить полный доступный баланс текущего аккаунта
    public BigDecimal getAvailableBalance() {
        return currentAccount.getBalance();
    }

    //for debug
    public String getAccountToString() {
        return currentAccount.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Processing center\n");
        builder.append("Current account: ").append(currentAccount.getAccountNumber()).append('\n');
        accounts.forEach((key, value) -> builder.append(value).append('\n'));
        return builder.toString();
    }
}
