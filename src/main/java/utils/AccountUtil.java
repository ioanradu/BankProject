package utils;

import model.CurrencyType;

public class AccountUtil {

    public static boolean isValidId(String accountId) {
        if (accountId.length() != ApplicationConst.ACCOUNT_NAME_LENGTH) {
            return false;
        }
        if (!accountId.substring(0, 2).equals(ApplicationConst.ACCOUNT_PREFIX)) {
            return false;
        }
        return true;
    }

    public static boolean isValidCurrencyType(String currencyType) {
        if (currencyType.equalsIgnoreCase("RON") || currencyType.equalsIgnoreCase("EUR")) {
            return true;
        }
        return false;
    }

    public static CurrencyType getCurrencyType(String currencyStr) {
        if (currencyStr.equalsIgnoreCase("RON")) {
            return CurrencyType.RON;
        }
        if (currencyStr.equalsIgnoreCase("EUR")) {
            return CurrencyType.EUR;
        }
        return CurrencyType.NO_CURRENCY;
    }

    public static boolean isNumber(String balanceStr) {
        boolean isANumber = false;
        if (balanceStr.charAt(0) == '-' || Character.isDigit(balanceStr.charAt(0))) {
            for (int i = 1; i < balanceStr.length(); i++) {
                isANumber = Character.isDigit(balanceStr.charAt(i));
                if (isANumber == false) {
                    return false;
                }
            }
        }
        return isANumber;
    }
}
