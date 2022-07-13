package com.valdist.exception.processing;

import java.math.BigInteger;

public class AccountHasNotFoundException extends ProcessingCenterException {
    public AccountHasNotFoundException(BigInteger accountNumber) {
        super("Account has not been found by number " + accountNumber);
    }
}
