package com.valdist.exception.processing;

public class AccountHasNotFoundException extends ProcessingCenterException {
    public AccountHasNotFoundException(long accountNumber) {
        super("Account has not been found by number " + accountNumber);
    }
}
