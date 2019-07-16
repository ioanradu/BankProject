package service;

import model.Account;
import model.CurrencyType;
import model.User;
import utils.AccountUtil;
import utils.ApplicationConst;
import utils.TxtFileReader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

public class AccountService {
    public static final Logger logger = Logger.getLogger(Logger.class.getName());
    private User user;
    BigDecimal balance;

    public AccountService(User user) {
        this.user = user;
    }

    public void buildAccounts() {
        TxtFileReader txtFileReader = new TxtFileReader(ApplicationConst.ACCOUNTS_FILE_PATH);
        ArrayList<String> lines = txtFileReader.read();
        for (String line : lines) {
            String[] tokens = line.split(" ");
            if (tokens.length != 4) {
                continue;
            }
            String accountId = tokens[0];
            if (!AccountUtil.isValidId(accountId)) {
                continue;
            }

            String owner = tokens[1];
            if (!user.getUserId().equals(owner)) {
                continue;
            }

            String balance = tokens[2];
            int accountBalance = Integer.parseInt(balance);
            if (accountBalance < 0) {
                continue;
            }

            String currencyTypeStr = tokens[3];
            if (!AccountUtil.isValidCurrencyType(currencyTypeStr)) {
                continue;
            }
            CurrencyType currencyType = AccountUtil.getCurrencyType(currencyTypeStr);
            Account account = new Account(owner, accountId, new BigDecimal(accountBalance), currencyType);
            user.addAccount(account);
        }
    }

    public void displayCurrentInfo() {
        logger.info(user.toString()); // la logger trebuie apelat explicit toString()
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPress any key...");
        scanner.nextLine();
    }

    public Account createAccount() {
        Account account = null;
        Scanner scanner = new Scanner(System.in);
        boolean isValidAccount = false;
        boolean isValidAccountId = false;
        String accountId = "";
        while (!isValidAccount || !isValidAccountId) {
            System.out.println("\nAccount number (should start with \'RO' and should have 10 characters): ");
            accountId = scanner.nextLine();
            isValidAccount = AccountUtil.isValidId(accountId);
            if (isValidAccount) {
                account = user.getAccounts().get(accountId);
                if (account == null) {
                    isValidAccountId = true;
                } else {
                    System.out.println("Account already exists, please enter a new one. ");
                }
            }
        }

        boolean isValidNumber = false;
        String balanceStr = "";
        while (!isValidNumber) {
            System.out.println("Amount of money is : ");
            balanceStr = scanner.nextLine();
            isValidNumber = AccountUtil.isNumber(balanceStr);

            if (isValidNumber) {
                balance = new BigDecimal(Integer.parseInt(balanceStr));
                while (balance.compareTo(BigDecimal.ZERO) == -1) {
                    System.out.println("Amount of money is : ");
                    balanceStr = scanner.nextLine();
                    balance = new BigDecimal(Integer.parseInt(balanceStr));
                }
            }
        }

        String currencyStr = "";
        boolean isCurrency = false;
        while (!isCurrency) {
            System.out.println("Currency of your newly created account is: ");
            currencyStr = scanner.nextLine();
            if (!AccountUtil.isValidCurrencyType(currencyStr)) {
                System.out.println(currencyStr + " are not allowed, only RON and EUR are allowed");
            } else {
                isCurrency = true;
            }
        }

        CurrencyType currency = AccountUtil.getCurrencyType(currencyStr);
        account = new Account(user.getUserId(), accountId, balance, currency);
        user.addAccount(account);
        System.out.println("The account has been created!");
        return account;
    }

    public void makePayments() {

    }
}

