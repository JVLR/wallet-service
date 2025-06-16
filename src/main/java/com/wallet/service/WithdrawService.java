package com.wallet.service;

import com.wallet.exception.DataException;
import com.wallet.model.OperationType;
import com.wallet.model.dto.WithDrawRequest;
import com.wallet.repository.WalletsRepositoryJpa;
import jakarta.inject.Singleton;

@Singleton
public class WithdrawService {

    private final WalletsRepositoryJpa walletsRepository;
    private final WalletService walletService;

    public WithdrawService(WalletsRepositoryJpa walletsRepository, WalletService walletService) {
        this.walletsRepository = walletsRepository;
        this.walletService = walletService;
    }

    public void doExecute(WithDrawRequest input) {

        walletsRepository.findByUserId(input.userId())
                .ifPresentOrElse(
                        wallet -> {
                            if (wallet.getBalance().compareTo(input.amount()) < 0) {
                                throw new DataException("Insufficient funds");
                            }
                            walletService.withdraw(wallet, input.amount(), OperationType.WITHDRAW, "Withdrawal for user ID: " + input.userId());
                            walletsRepository.update(wallet);
                        },
                        () -> {
                            throw new DataException("Wallet not found for user ID: " + input.userId());
                        });
    }
}
