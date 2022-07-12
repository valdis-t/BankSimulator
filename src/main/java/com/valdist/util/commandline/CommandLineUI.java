package com.valdist.util.commandline;

import com.valdist.entity.account.Account;
import com.valdist.entity.account.bank.BankAccount;
import com.valdist.entity.transaction.Transaction;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Scanner;
import java.util.UUID;

public class CommandLineUI {
    private final Scanner scanner = new Scanner(System.in);

    public int getInt(String message) {
        return (int) getLong(message);
    }

    public boolean getBoolean(String message, String predicateValue) {
        return getString(message + " - " + predicateValue).equalsIgnoreCase(predicateValue);
    }

    public long getLong(String message) {
        String string = getString(message);
        String digitsOnly = string.replaceAll("[^0-9]", "");

        return digitsOnly.length() == 0 ? 0 : string.replaceAll("[^-]", "").length() == 0 ? Long.parseLong(digitsOnly) : -Long.parseLong(digitsOnly);
    }

    public double getDouble(String message) {
        boolean commaDetected = false;
        boolean isNegative = false;
        double result;
        char[] chars = getString(message).toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char current : chars) {
            if (Character.isDigit(current)) {
                builder.append(current);
            } else if (!commaDetected && builder.length() != 0 & (current == ',' || current == '.')) {
                commaDetected = true;
                builder.append('.');
            } else if (current == '-') isNegative = true;
        }
        result = builder.length() != 0 ? Double.parseDouble(builder.toString()) : 0;
        return isNegative ? -result : result;
    }

    public String getString(String message) {
        System.out.print(message + ": ");
        return scanner.nextLine();
    }

    public Account getRawAccount() {
        BankAccount.Builder builder = new BankAccount.Builder();
        println("Создать аккаунт");
        do {
            builder.setName(getString("Введи имя аккаунта"));
            builder.setAccountNumber(1000_000 + (long) (100_000 * Math.random()));
            builder.setPrivateMoneyBalance(getLong("Введи личные средства"));
            builder.setInitialOverdraft(getLong("Введи овердрафт"));
            builder.setCurrency("USD");
        } while (builder.build().isEmpty());
        return builder.build().get();
    }

    public Transaction getRawTransaction() {
        Transaction.Builder builder = Transaction.getBuilder();
        println("Создать транзакцию");
        do {
            builder
                    .setAmount(getLong("Введи сумму"))
                    .setLocalDate(LocalDate.now())
                    .setName(UUID.randomUUID().toString())
                    .setType(Transaction.Type.SERVICE)
                    .setCurrency("USD");
        } while (builder.build().isEmpty());
        return builder.build().get();
    }

    public void execute(Collection<Execution> instruction, Execution breakingExecution, boolean inLoop){
        instruction.forEach(current -> System.out.printf("%s - %s%n", current.getCommand(), current.getDescription()));
        System.out.printf("%s - %s%n", breakingExecution.getCommand(), breakingExecution.getDescription());
        String input = getString("Input");
        if(input.equalsIgnoreCase(breakingExecution.getCommand())) {
            breakingExecution.execute();
            return;
        }
        instruction.stream().filter(element -> element.getCommand().equalsIgnoreCase(input)).findFirst().ifPresent(Execution::execute);

        if(inLoop){
            if(getBoolean("Repeat?", "y")) execute(instruction, breakingExecution, inLoop);
        }
    }

    public void println(Object message) {
        System.out.println(message);
    }

    public void print(Object message) {
        System.out.print(message);
    }
}
