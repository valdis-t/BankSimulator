package com.valdist.entity.account;

import com.valdist.exception.account.AccountException;
import com.valdist.exception.account.InsufficientFundsException;
import com.valdist.entity.transaction.Transaction;

//класс, описывающий логику аккаунта наличных средств
public class CashAccount extends Account {
    //аккаунты наличных средств будут отличаться на 1, mock
    private static long cashAccountCounter = 1_0000_0000_0000_000L;

    public CashAccount(String name, long balance, String currency) {
        super(cashAccountCounter++, name, balance, currency);
    }

    //метод, производящий операции с балансом наличных средств
    @Override
    public void makeTransaction(Transaction transaction) throws AccountException {
        //если транзакция отрицательная и превышает весь доступный баланс - бросить исключение BalanceDoesNotHaveEnoughFunds и добавить в список неуспешных транзакций
        if (transaction.getAmount() + balance < 0)
            addFailedTransaction(transaction, new InsufficientFundsException());
        //обновить баланс
        balance += transaction.getAmount();
    }

    //featured method, mock
    @Override
    public void makeServiceTransaction(Transaction transaction) {
    }
}
