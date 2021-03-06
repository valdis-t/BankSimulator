package com.valdist.entity.account.bank;

import com.valdist.entity.account.Account;
import com.valdist.entity.transaction.Transaction;
import com.valdist.exception.account.AccountException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Optional;

/*
 * Реализовать следующую логику:
 * Через билдер строится объект депозитного аккаунта, далее пользователь подписывается на аккаунт для выплаты депозита (по умолчанию this), есть несколько вариантов:
 * setAccountForDepositPayOff(Account account) - целевое назначение, для выплат на внешний аккаунт
 * setAccountForDepositPayOff(null) - нецелевое, проценты будут выплачиваться на счет депозита, прибавляясь к телу
 *
 * При завершении срока действия депозита closingDate, есть аккаунт для выплаты - это текущий объект:
 * сменить ставку по депочиту interestRate = 1
 * прибавить 1 год к дате закрытия
 * при типе депозита "срочный" сменить на "безсрочный"
 *
 * Реализовать toString() у класса Builder
 */

//класс, описывающий логику депозитного аккаунта
public class DepositAccount extends Account {
    //аккаунт для выплаты насчитанных средств по депозитному вкладу
    private Account accountForDepositPayOff;
    private final LocalDate openingDate;
    private final LocalDate closingDate;
    private final BigDecimal interestRate;

    protected DepositAccount(BigInteger accountNumber, String name, BigDecimal balance, String currency, BigDecimal interestRate, LocalDate openingDate, LocalDate closingDate) {
        super(accountNumber, name, balance, currency);
        this.interestRate = interestRate;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        accountForDepositPayOff = this;
    }

    //сеттер для изменения аккаунта для выплаты насчитанных средств по депозитному вкладу
    public void setAccountForDepositPayOff(Account accountForDepositPayOff) {
        this.accountForDepositPayOff = accountForDepositPayOff;
    }

    //featured method, mock
    @Override
    public void makeTransaction(Transaction transaction) throws AccountException {
    }

    //featured method, mock
    @Override
    public void makeServiceTransaction(Transaction transaction) {
    }

    public static class Builder extends AccountBuilder<DepositAccount> {
        private BigDecimal balance;
        private BigDecimal interestRate;
        private LocalDate openingDate;
        private LocalDate closingDate;

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }

        public void setInterestRate(BigDecimal interestRate) {
            this.interestRate = interestRate;
        }

        public void setOpeningDate(LocalDate openingDate) {
            this.openingDate = openingDate;
        }

        public void setClosingDate(LocalDate closingDate) {
            this.closingDate = closingDate;
        }

        @Override
        protected boolean isInitialized() {
            return super.isInitialized() &&
                    balance.compareTo(BigDecimal.ZERO) >= 0 &&
                    interestRate.compareTo(BigDecimal.ZERO) != 0 &&
                    openingDate != null &&
                    closingDate != null &&
                    openingDate.isBefore(closingDate);
        }

        @Override
        public Optional<DepositAccount> build() {
            return Optional.ofNullable(isInitialized() ? new DepositAccount(accountNumber, name, balance, currency, interestRate, openingDate, closingDate) : null);
        }
    }
}
