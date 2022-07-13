package com.valdist.entity.account.bank;

import com.valdist.entity.account.Account;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class ObligationAccount extends Account {
    private long interestRate;
    private Account accountForPayingObligation;

    protected ObligationAccount(BigInteger accountNumber, String name, BigDecimal balance, String currency) {
        super(accountNumber, name, balance, currency);
    }

    public void setAccountForPayingObligation(Account account) {
        this.accountForPayingObligation = account;
    }
}
