package com.valdist.entity.account.bank;

import com.valdist.entity.account.Account;
import com.valdist.exception.processing.AccountHasNotFoundException;
import com.valdist.processing.ProcessingCenter;
import com.valdist.util.commandline.CommandLineUI;
import com.valdist.util.commandline.Execution;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class ProcessingCenterTest {
    public static void main(String[] args) {
        CommandLineUI ui = new CommandLineUI();
        Collection<Account> accounts = new HashSet<>();
        ui.println("Processing Center Test");

        do {
            do {
                accounts.add(ui.getRawAccount());
            } while (ui.getBoolean("Add new one account?", "y"));

            ProcessingCenter processingCenter = new ProcessingCenter(accounts);

            ui.execute(
                    List.of(
                            new Execution("1", "make transaction", () -> processingCenter.makeTransaction(ui.getRawTransaction())),
                            new Execution("2", "get account info", () -> ui.println(processingCenter.getAccountToString())),
                            new Execution("3", "set account", () -> {
                                ui.println("Accounts");
                                processingCenter.getAccountNumbers().forEach(ui::println);
                                try {
                                    processingCenter.setCurrentAccount(ui.getLong("Input account"));
                                } catch (AccountHasNotFoundException e) {
                                    ui.println(e.getMessage());
                                }
                            })),
                    new Execution("0", "exit", () -> {
                    }),
                    true);
        } while (ui.getBoolean("Reapeat the test?", "y"));
    }
}
