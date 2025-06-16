package com.wallet.service;

import com.wallet.exception.DataException;
import com.wallet.model.HistoricBalance;
import com.wallet.model.dto.RetrieveHistoricBalanceRequest;
import com.wallet.model.dto.RetrieveHistoricBalanceResponse;
import com.wallet.repository.WalletsRepositoryJpa;
import com.wallet.repository.entity.HistoricBalanceEntity;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class RetrieveHistoricBalanceService {

    private final WalletsRepositoryJpa walletsRepository;

    public RetrieveHistoricBalanceService(WalletsRepositoryJpa walletsRepository) {
        this.walletsRepository = walletsRepository;
    }

    public List<RetrieveHistoricBalanceResponse> doExecute(RetrieveHistoricBalanceRequest input) {

        List<HistoricBalanceEntity> entities = walletsRepository.findHistoricBalanceByUserAndDateIdAndDate(
                input.userId(), input.startDate(), input.endDate());

        if (entities == null || entities.isEmpty()) {
            throw new DataException("Historical Balance not found for user: " + input.userId());
        }
        return entities.stream()
                .map(entity -> {
                    HistoricBalance balance = entity.toModel();
                    return new RetrieveHistoricBalanceResponse(
                            balance.getAmount(),
                            balance.getOperation(),
                            balance.getDate()
                    );
                })
                .toList();
    }
}
