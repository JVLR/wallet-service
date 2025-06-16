package com.wallet.service;

import com.wallet.exception.DataException;
import com.wallet.model.dto.RetrieveBalanceRequest;
import com.wallet.model.dto.RetrieveBalanceResponse;
import com.wallet.repository.WalletsRepositoryJpa;
import com.wallet.repository.entity.WalletEntity;
import jakarta.inject.Singleton;

@Singleton
public class RetrieveBalanceService {

    private final WalletsRepositoryJpa walletsRepositoryJpa;

    public RetrieveBalanceService(WalletsRepositoryJpa walletsRepositoryJpa) {
        this.walletsRepositoryJpa = walletsRepositoryJpa;
    }

    public RetrieveBalanceResponse doExecute(RetrieveBalanceRequest input) {

        WalletEntity wallet = walletsRepositoryJpa.findByUserIdFetchHistoricBalance(input.userId())
                .orElseThrow(() -> new DataException("Balance for wallet not found for userId: " + input.userId()));
        return new RetrieveBalanceResponse(wallet.getId(), wallet.getBalance());
    }
}
