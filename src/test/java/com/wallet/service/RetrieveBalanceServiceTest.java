package com.wallet.service;

import com.wallet.exception.DataException;
import com.wallet.model.dto.RetrieveBalanceRequest;
import com.wallet.model.dto.RetrieveBalanceResponse;
import com.wallet.repository.WalletsRepositoryJpa;
import com.wallet.repository.entity.WalletEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RetrieveBalanceServiceTest {

    private WalletsRepositoryJpa walletsRepositoryJpa;
    private RetrieveBalanceService retrieveBalanceService;

    @BeforeEach
    void setUp() {
        walletsRepositoryJpa = mock(WalletsRepositoryJpa.class);
        retrieveBalanceService = new RetrieveBalanceService(walletsRepositoryJpa);
    }

    @Test
    void shouldReturnBalanceWhenWalletExists() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String walletId = UUID.randomUUID().toString();
        BigDecimal balance = BigDecimal.valueOf(100.50);

        RetrieveBalanceRequest request = mock(RetrieveBalanceRequest.class);
        when(request.userId()).thenReturn(userId.toString());

        WalletEntity walletEntity = mock(WalletEntity.class);
        when(walletEntity.getId()).thenReturn(walletId);
        when(walletEntity.getBalance()).thenReturn(balance);

        when(walletsRepositoryJpa.findByUserIdFetchHistoricBalance(userId.toString()))
                .thenReturn(Optional.of(walletEntity));

        // Act
        RetrieveBalanceResponse response = retrieveBalanceService.doExecute(request);

        // Assert
        assertNotNull(response);
        assertEquals(walletId, response.walletId());
        assertEquals(balance, response.balance());
    }

    @Test
    void shouldThrowExceptionWhenWalletNotFound() {
        // Arrange
        UUID userId = UUID.randomUUID();
        RetrieveBalanceRequest request = mock(RetrieveBalanceRequest.class);
        when(request.userId()).thenReturn(String.valueOf(userId));

        when(walletsRepositoryJpa.findByUserIdFetchHistoricBalance(String.valueOf(userId)))
                .thenReturn(Optional.empty());

        // Act & Assert
        DataException ex = assertThrows(DataException.class, () -> retrieveBalanceService.doExecute(request));
        assertTrue(ex.getMessage().contains("Balance for wallet not found for userId"));
    }
}