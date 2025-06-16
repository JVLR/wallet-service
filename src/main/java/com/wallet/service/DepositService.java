package com.wallet.service;

import com.wallet.exception.DataException;
import com.wallet.model.dto.DepositRequest;
import com.wallet.repository.WalletsRepositoryJpa;
import jakarta.inject.Singleton;

@Singleton
public class DepositService {

    private final WalletsRepositoryJpa walletsRepositoryJpa;
    private final WalletService walletService;

    public DepositService(WalletsRepositoryJpa walletsRepositoryJpa, WalletService walletService) {
        this.walletsRepositoryJpa = walletsRepositoryJpa;
        this.walletService = walletService;
    }

    public void doExecute(DepositRequest input) {
        walletsRepositoryJpa.findByUserId(input.userId())
                .ifPresentOrElse(
                        wallet -> {
                            walletService.deposit(wallet, input.amount());
                            walletsRepositoryJpa.update(wallet);
                        },
                        () -> {
                            throw new DataException("Wallet not found for user ID: " + input.userId());
                        });
    }
}