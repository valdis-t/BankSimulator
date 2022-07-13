package com.valdist.exception.account;

public class CreditAccountCannotSetItselfForObligationException extends Exception {
    public CreditAccountCannotSetItselfForObligationException() {
        super("Credit account cannot set itself for withdrawing by obligation.");
    }
}
