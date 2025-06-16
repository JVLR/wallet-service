package com.wallet.service;

import com.wallet.exception.DataException;
import com.wallet.model.dto.TransferRequest;
import com.wallet.repository.WalletsRepositoryJpa;
import com.wallet.repository.entity.WalletEntity;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class TransferService {

    private final WalletsRepositoryJpa walletsRepository;
    private final WalletService walletService;

    public TransferService(WalletsRepositoryJpa walletsRepository, WalletService walletService) {
        this.walletsRepository = walletsRepository;
        this.walletService = walletService;
    }

    public void doExecute(TransferRequest input) {
        WalletEntity walletFrom = walletsRepository.findByUserId(input.from())
                .orElseThrow(() -> new DataException("Wallet not found for user ID: " + input.from()));

        WalletEntity walletTo = walletsRepository.findByUserId(input.to())
                .orElseThrow(() -> new DataException("Wallet not found for user ID: " + input.to()));

        walletService.transfer(walletFrom, walletTo, input.amount());
        walletsRepository.updateAll(List.of(walletFrom, walletTo));
    }
}
