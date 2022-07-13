package com.valdist.entity.account;

import com.valdist.entity.transaction.Transaction;
import com.valdist.exception.account.AccountException;
import com.valdist.exception.account.InsufficientFundsException;
import java.math.BigDecimal;
import java.math.BigInteger;

//класс, описывающий логику аккаунта наличных средств
public class CashAccount extends Account {
    //аккаунты наличных средств будут отличаться на 1, mock
    private static long cashAccountCounter = 1_0000_0000_0000_000L;

    public CashAccount(String name, BigDecimal balance, String currency) {
        super(BigInteger.valueOf(cashAccountCounter++), name, balance, currency);
    }

    //метод, производящий операции с балансом наличных средств
    @Override
    public void makeTransaction(Transaction transaction) throws AccountException {
        //если транзакция отрицательная и превышает весь доступный баланс - бросить исключение BalanceDoesNotHaveEnoughFunds и добавить в список неуспешных транзакций
        if (transaction.getAmount().compareTo(balance) < 0)
            addFailedTransaction(transaction, new InsufficientFundsException());
        //обновить баланс
        balance = balance.add(transaction.getAmount());
    }

    //featured method, mock
    @Override
    public void makeServiceTransaction(Transaction transaction) {
    }
}
