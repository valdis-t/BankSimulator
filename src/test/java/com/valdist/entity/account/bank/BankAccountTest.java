package com.valdist.entity.account.bank;

import com.valdist.entity.account.Account;
import com.valdist.exception.account.AccountException;
import com.valdist.util.commandline.CommandLineUI;

public class BankAccountTest {
    public static void main(String[] args) {
        CommandLineUI ui = new CommandLineUI();

        Account account = null;
        label1:
        {
            while (true) {
                if (account == null || ui.getBoolean("Создать новый аккаунт", "y")) {
                    ui.println("Создай аккаунт.");
                    BankAccount.Builder bankAccountBuilder = new BankAccount.Builder();
                    do {
                        bankAccountBuilder.setName(ui.getString("Введи имя аккаунта"));
                        bankAccountBuilder.setAccountNumber(ui.getBigInteger("Введи номер счета"));
                        bankAccountBuilder.setPrivateMoneyBalance(ui.getBigDecimal("Введи личные средства"));
                        bankAccountBuilder.setInitialOverdraft(ui.getBigDecimal("Введи овердрафт"));
                        bankAccountBuilder.setCurrency(ui.getString("Введи валюту аккаунта"));
                    } while (bankAccountBuilder.build().isEmpty());
                    account = bankAccountBuilder.build().get();
                }

                ui.println("Создай транзакции");
                int counter = ui.getInt("Введи кол-во транзакций");
                while (counter != 0) {
                    try {
                        account.makeTransaction(ui.getRawTransaction());
                    } catch (AccountException e) {
                        e.printStackTrace();
                    }
                    counter--;
                }

                if (!ui.getBoolean("Повторить", "y")) break label1;
            }
        }
    }
}
