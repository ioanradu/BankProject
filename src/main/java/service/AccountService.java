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
    BigDecimal balanceTransfer;

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
        logger.info(user.toString());// la logger trebuie apelat explicit toString()
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

        //VERIFICAM DACA AMOUNT OF MONEY ESTE UN NUMAR SI DACA ESTE > 0
        boolean isValidNumber = false;
        String balanceStr = "";
        while (!isValidNumber) {
            System.out.println("Amount of money is : ");
            balanceStr = scanner.nextLine();
            isValidNumber = AccountUtil.isNumber(balanceStr);
            if (isValidNumber) {
                balance = new BigDecimal(Integer.parseInt(balanceStr));
                if (balance.compareTo(BigDecimal.ZERO) == -1) {
                    System.out.println("The amount of money must be positive : ");
                    isValidNumber = false;
                }
            }
        }

        // VERIFICAM DACA CURRENCY ESTE DE TIP RON SAU EUR
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

    public void makePayments() throws NegativeNumberException {

        logger.info(user.toString());
        // VERIFICAM DACA CONTUL SURSA ESTE SELECTAT CORECT
        boolean isSourceAccount = false;
        Account sourceAccount = null;
        Scanner scanner = null;
        while (!isSourceAccount) {
            scanner = new Scanner(System.in);
            System.out.println("Select an AccountId to make a payment: ");
            String sourceAccountStr = scanner.nextLine();
            sourceAccount = user.getAccounts().get(sourceAccountStr);

            if (sourceAccount == null) {
                System.out.println("Account you entered is not in your account's list!");
            } else {
                isSourceAccount = true;
            }
        }

        // VERIFICAM DACA SUMA DE TRANSFER E UN NUMAR VALID SI DACA E MAI MARE CA 0
        boolean isValidNumber = false;
        String balanceAccount = "";
        while (!isValidNumber) {
            System.out.println("Please enter the amount of money you want to transfer");
            balanceAccount = scanner.nextLine();
            isValidNumber = AccountUtil.isNumber(balanceAccount);

            if (isValidNumber) {
                balanceTransfer = new BigDecimal(Integer.parseInt(balanceAccount));
                //VERIFICAM DACA SUMA DE TRANSFER ESTE UN NUMAR POZITIV SAU NEGATIV
                //DACA E NUMAR NEGATIV ARUNCAM EXCEPTIA NEGATIVE NUMBER EXCEPTION
                if (balanceTransfer.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("The amount of money you want to transfer must be greater than 0");
                    throw new NegativeNumberException();

                    //VERIFICAM DACA SUMA DE TRANSFER E MAI MICA SAU EGALA CU SUMA DIN CONT
                } else if (balanceTransfer.compareTo(sourceAccount.getBalance()) == 1) {
                    System.out.println("You entered a grater amount of money than account has: ");
                    isValidNumber = false;
                } else {
                    isValidNumber = true;
                }
            }
        }

        //VALIDAM DACA CONTUL DE TRANSFER E SELECTAT CORECT
        boolean isDestAccount = false;
        Account destAccount = null;
        while (!isDestAccount) {
            scanner = new Scanner(System.in);
            System.out.println("Select an AccountId to make the transfer: ");
            String destAccountStr = scanner.nextLine();
            destAccount = user.getAccounts().get(destAccountStr);

            if (destAccount == null) {
                System.out.println("Please enter the account correctly!");
                //VERIFICAM DACA CONTURILE AU ACELASI TIP DE CURRENCY
            } else if (!(destAccount.getCurrency() == sourceAccount.getCurrency())) {
                System.out.println("Currency must be the same." +
                        "Please choose an account with currency: " + sourceAccount.getCurrency());
            } else {
                isDestAccount = true;
            }
        }
        // REALIZAM TRANSFERUL DE SUME INTRE CELE 2 CONTURI
        transferMoney(sourceAccount, destAccount, balanceTransfer);
    }

    public void transferMoney(Account sourceAccount, Account destAccount, BigDecimal balanceTransfer) {
        BigDecimal subtractBalance = sourceAccount.getBalance().subtract(balanceTransfer);
        BigDecimal addBalance = destAccount.getBalance().add(balanceTransfer);
        sourceAccount.setBalance(subtractBalance);
        destAccount.setBalance(addBalance);
        System.out.println("The payment have been done successfully!");
    }
}
