package menu;

import model.Account;
import model.User;
import service.AccountService;
import utils.ApplicationConst;
import utils.TxtFileWriter;

import java.util.logging.Logger;

public class AccountsMenu extends AbstractMenu {

    private static final Logger logger = Logger.getLogger(Logger.class.getName());
    private User user;

    public AccountsMenu(User user) {
        this.user = user;
    }

    protected void displayOptions() {
        System.out.println("ACCOUNTS MENU");
        System.out.println("1 - Read accounts");
        System.out.println("2 - Create accounts");
        System.out.println("3 - Make payments");
        System.out.println("4 - Display info");
        System.out.println("0 - Logout");
    }

    protected void executeOption(Integer option) {
        AccountService accountService = new AccountService(user);
        switch (option) {
            case 1:
                accountService.buildAccounts();
                break;
            case 2:
                Account account = accountService.createAccount();
                TxtFileWriter txtFileWriter = new TxtFileWriter(ApplicationConst.ACCOUNTS_FILE_PATH);
                txtFileWriter.write(buildString(account));
                break;
            case 3:
                try {
                    accountService.makePayments();
                } catch (Exception e) {
                    System.out.println("Something went wrong during transaction. Payment not done!");
                }
                accountService.displayCurrentInfo();
                break;
            case 4:
                accountService.displayCurrentInfo();
                break;
            case 0:
                logger.info("User " + user.getUserId() + " is successfully logout");
                break;
            default:
                logger.warning("Invalid option!");
                break;
        }
    }

    private String buildString(Account account) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(account.getAccountId() + " " +
                account.getOwner() + " " +
                account.getBalance() + " " +
                account.getCurrency() + "\n");

        return stringBuilder.toString();
    }
}