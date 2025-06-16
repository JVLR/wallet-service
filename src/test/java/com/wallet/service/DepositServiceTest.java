package com.wallet.service;

import com.wallet.exception.DataException;
import com.wallet.model.Wallet;
import com.wallet.model.dto.DepositRequest;
import com.wallet.repository.WalletsRepositoryJpa;
import com.wallet.repository.entity.WalletEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DepositServiceTest {

    private WalletsRepositoryJpa walletsRepositoryJpa;
    private DepositService depositService;
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        walletsRepositoryJpa = mock(WalletsRepositoryJpa.class);
        walletService = mock(WalletService.class);
        depositService = new DepositService(walletsRepositoryJpa, walletService);
    }

    @Test
    void shouldDepositWhenWalletExists() {
        // Arrange
        String userId = UUID.randomUUID().toString();
        BigDecimal amount = BigDecimal.valueOf(50.0);
        DepositRequest request = mock(DepositRequest.class);
        when(request.userId()).thenReturn(userId);
        when(request.amount()).thenReturn(amount);

        WalletEntity walletEntity = mock(WalletEntity.class);
        Wallet wallet = mock(Wallet.class);
        when(walletEntity.toModel()).thenReturn(wallet);
        when(walletsRepositoryJpa.findByUserId(userId)).thenReturn(Optional.of(walletEntity));

        // Act
        depositService.doExecute(request);

        // Assert
        verify(walletService). deposit(walletEntity, amount);
        verify(walletsRepositoryJpa).update(walletEntity);
    }

    @Test
    void shouldThrowExceptionWhenWalletNotFound() {
        // Arrange
        String userId = UUID.randomUUID().toString();
        DepositRequest request = mock(DepositRequest.class);
        when(request.userId()).thenReturn(userId);

        when(walletsRepositoryJpa.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        DataException ex = assertThrows(DataException.class, () -> depositService.doExecute(request));
        assertTrue(ex.getMessage().contains("Wallet not found for user ID: " + userId));
        verify(walletsRepositoryJpa, never()).update(any());
    }
}