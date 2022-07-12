package com.valdist.exception.account;

//mock
public class InsufficientFundsException extends AccountException {
    public InsufficientFundsException() {
        super("Insufficient funds.");
    }
}
