package com.wallet.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Wallet {

    private final UUID id;
    private final UUID userId;
    private BigDecimal balance;
    private Set<HistoricBalance> historicBalances;

    public Wallet(UUID userId) {
        this.id = UUID.randomUUID();
        this.balance = BigDecimal.ZERO;
        this.userId = userId;
        this.historicBalances = new HashSet<>();
    }

    public Wallet(UUID id, UUID userId, BigDecimal balance, Set<HistoricBalance> historicBalances) {
        this.id = id;
        this.userId = userId;
        this.balance = balance != null ? balance : BigDecimal.ZERO;
        this.historicBalances = historicBalances != null ? historicBalances : new HashSet<>();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public UUID getId() {
        return id;
    }

    public Set<HistoricBalance> gethistoricBalances() {
        return historicBalances;
    }

    public UUID getUserId() {
        return userId;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Wallet wallet))
            return false;
        return id.equals(wallet.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", balance=" + balance +
                ", historicBalances=" + historicBalances +
                '}';
    }
}