package com.wallet.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HistoricBalance {
    private String walletId;
    private LocalDateTime date;
    private BigDecimal amount;
    private String operation;
    private String description;

    public HistoricBalance(String walletId, LocalDateTime date, BigDecimal amount, String operation,
                           String description) {
        this.walletId = walletId;
        this.date = date;
        this.amount = amount;
        this.operation = operation;
        this.description = description;
    }

    public String getWalletId() {
        return walletId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getOperation() {
        return operation;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "historicBalance{" +
                "walletId='" + walletId + '\'' +
                ", date='" + date + '\'' +
                ", amount=" + amount +
                ", operation='" + operation + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HistoricBalance that))
            return false;

        if (that.amount.compareTo(amount) != 0)
            return false;
        if (!walletId.equals(that.walletId))
            return false;
        if (operation != null && !operation.equals(that.operation))
            return false;
        return date.equals(that.date) && amount == that.amount && walletId.equals(that.walletId);
    }
}
