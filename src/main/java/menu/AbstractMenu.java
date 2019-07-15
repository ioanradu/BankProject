package menu;

import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class AbstractMenu {

    public void displayMenu() {
        Integer option = Integer.MAX_VALUE;
        while (option != 0) {
            displayOptions();
            //readOption();
            option = readOption();
            executeOption(option);

        }
    }

    private Integer readOption() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("your option is: ");
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            return -1;
        }
    }

    protected abstract void executeOption(Integer option);

    protected abstract void displayOptions();
}
