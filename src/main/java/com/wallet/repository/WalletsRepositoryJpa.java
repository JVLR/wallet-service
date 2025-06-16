package com.wallet.repository;

import com.wallet.constant.DbConstant;
import com.wallet.repository.entity.HistoricBalanceEntity;
import com.wallet.repository.entity.WalletEntity;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WalletsRepositoryJpa extends JpaRepository<WalletEntity, String> {

    @Query(DbConstant.FIND_WALLETS_BY_USER_ID)
    Optional<WalletEntity> findByUserId(String userId);

    @Query(DbConstant.FIND_HISTORIC_BALANCE_BY_USER_ID_AND_DATE)
    List<HistoricBalanceEntity> findHistoricBalanceByUserAndDateIdAndDate(String userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query(DbConstant.FIND_USER_FETCH_HISTORIC_BALANCE)
    Optional<WalletEntity> findByUserIdFetchHistoricBalance(String userId);

}