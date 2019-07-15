package menu;

import model.Account;
import model.User;
import service.LoginService;

import java.util.Scanner;
import java.util.logging.Logger;

public class LoginMenu extends AbstractMenu {
    private static final Logger logger = Logger.getLogger(Logger.class.getName());

    protected void displayOptions() {
        System.out.println("Login Menu");
        System.out.println("1 - Login");
        System.out.println("0 - Exit");
    }

    protected void executeOption(Integer option) {
        switch (option) {
            case 1:
                Scanner scanner = new Scanner(System.in);
                System.out.println("user: ");
                String userId = scanner.nextLine();
                System.out.println("password: ");
                String password = scanner.nextLine();
                System.out.println("User: " + userId + ", password: " + password);
                LoginService loginService = new LoginService();
                User user = loginService.login(userId, password);
                if (user != null) {
                    logger.info("Welcome, " + userId);
                    AccountsMenu accountsMenu = new AccountsMenu(user);
                    accountsMenu.displayMenu();
                } else {
                    logger.warning("Invalid username / password!");
                }
                break;
            case 0:
                logger.info("Exiting...");
                break;
            default:
                logger.warning("Invalid option!");
        }
    }
}
