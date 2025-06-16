package com.wallet.service;

import com.wallet.exception.DataException;
import com.wallet.model.HistoricBalance;
import com.wallet.model.OperationType;
import com.wallet.repository.entity.HistoricBalanceEntity;
import com.wallet.repository.entity.WalletEntity;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Singleton
public class WalletService {

    public void withdraw(WalletEntity wallet, BigDecimal amount, OperationType operation, String description) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DataException("Withdrawal amount must be positive");
        }
        if (amount.compareTo(wallet.getBalance()) > 0) {
            throw new DataException("Insufficient funds");
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
        HistoricBalanceEntity historical = new HistoricBalanceEntity(
                new HistoricBalance(
                        wallet.getId(),
                        LocalDateTime.now(),
                        amount,
                        operation.getValue(),
                        description
                )
        );
        historical.setWallet(wallet);
        wallet.gethistoricBalance().add(historical);
    }

    public void deposit(WalletEntity wallet, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DataException("Deposit amount must be positive");
        }
        wallet.setBalance(wallet.getBalance().add(amount));
        HistoricBalanceEntity historical = new HistoricBalanceEntity(
                new HistoricBalance(
                        wallet.getId(),
                        LocalDateTime.now(),
                        amount,
                        OperationType.DEPOSIT.getValue(),
                        "Deposit of " + amount
                )
        );
        historical.setWallet(wallet);
        wallet.gethistoricBalance().add(historical);
    }

    public void transfer(WalletEntity fromWallet, WalletEntity toWallet, BigDecimal amount) {
        if (toWallet == null) {
            throw new DataException("Target wallet cannot be null");
        }
        withdraw(fromWallet, amount, OperationType.TRANSFER, "Transferred to wallet: " + toWallet.getId());
        deposit(toWallet, amount);
    }
}