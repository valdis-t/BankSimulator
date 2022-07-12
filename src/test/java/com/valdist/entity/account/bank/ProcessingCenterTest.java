package com.valdist.entity.account.bank;

import com.valdist.entity.account.Account;
import com.valdist.exception.processing.AccountHasNotFoundException;
import com.valdist.processing.ProcessingCenter;
import com.valdist.util.commandline.CommandLineUI;

import java.util.Collection;
import java.util.HashSet;

public class ProcessingCenterTest {
    public static void main(String[] args) {
        CommandLineUI ui = new CommandLineUI();
        Collection<Account> accounts = new HashSet<>();
        ProcessingCenter processingCenter;
        ui.println("Processing Center Test");

        while (true) {
            while (true) {
                accounts.add(ui.getRawAccount());
                if (!ui.getBoolean("Add new one account?", "y")) break;
            }
            processingCenter = new ProcessingCenter(accounts);

            String menu = "1 - make transaction\n" +
                    "2 - get account info\n" +
                    "3 - set account\n" +
                    "0 - exit from menu\n" +
                    "Input";
            label:
            {
                while (true) {
                    switch (ui.getInt(menu)) {
                        case 1: {
                            processingCenter.makeTransaction(ui.getRawTransaction());
                            break;
                        }
                        case 3: {
                            ui.println("Accounts");
                            processingCenter.getAccountNumbers().forEach(ui::println);
                            try {
                                processingCenter.setCurrentAccount(ui.getLong("Input account"));
                            } catch (AccountHasNotFoundException e) {
                                ui.println(e.getMessage());
                            }
                            break;
                        }
                        case 2: {
                            ui.println(processingCenter.getAccountToString());
                            break;
                        }
                        case 0: {
                            break label;
                        }
                    }
                }
            }
            if (ui.getBoolean("Exit?", "y")) break;
        }

    }
}
