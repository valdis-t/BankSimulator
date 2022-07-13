package com.valdist.entity.account.bank;

import com.valdist.entity.account.Account;

public abstract class ObligationAccount extends Account {
    private long interestRate;
    private Account accountForPayingObligation;

    protected ObligationAccount(long accountNumber, String name, long balance, String currency) {
        super(accountNumber, name, balance, currency);
    }

    public void setAccountForPayingObligation(Account account) {
        this.accountForPayingObligation = accountForPayingObligation;
    }
}
