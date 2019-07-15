package model;

import java.math.BigDecimal;

public class Account {
    private String owner;
    private String accountId;
    private BigDecimal balance;
    private CurrencyType currency;

    public Account(String owner, String accountId, BigDecimal balance, CurrencyType currency) {
        this.owner = owner;
        this.accountId = accountId;
        this.balance = balance;
        this.currency = currency;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (!owner.equals(account.owner)) return false;
        if (!accountId.equals(account.accountId)) return false;
        if (!balance.equals(account.balance)) return false;
        return currency == account.currency;
    }

    @Override
    public int hashCode() {
        int result = owner.hashCode();
        result = 31 * result + accountId.hashCode();
        result = 31 * result + balance.hashCode();
        result = 31 * result + currency.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "\n\tAccountId=" + accountId +
                ", balance=" + balance +
                ", currency=" + currency;
    }
}
