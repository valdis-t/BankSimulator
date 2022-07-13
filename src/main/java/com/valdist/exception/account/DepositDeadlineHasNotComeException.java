package com.valdist.exception.account;

public class DepositDeadlineHasNotComeException extends Exception{
    public DepositDeadlineHasNotComeException(){
        super("Deposit deadline for withdrawing has not come.");
    }
}
