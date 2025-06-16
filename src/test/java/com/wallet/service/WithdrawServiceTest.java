package com.wallet.service;

import com.wallet.exception.DataException;
import com.wallet.model.OperationType;
import com.wallet.model.dto.WithDrawRequest;
import com.wallet.repository.WalletsRepositoryJpa;
import com.wallet.repository.entity.WalletEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WithdrawServiceTest {

    private WalletsRepositoryJpa walletsRepository;
    private WithdrawService withdrawService;
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        walletsRepository = mock(WalletsRepositoryJpa.class);
        walletService = mock(WalletService.class);
        withdrawService = new WithdrawService(walletsRepository, walletService);
    }

    @Test
    void shouldWithdrawWhenBalanceIsSufficient() {
        String userId = "user-1";
        BigDecimal amount = BigDecimal.valueOf(100);
        WithDrawRequest request = mock(WithDrawRequest.class);
        when(request.userId()).thenReturn(userId);
        when(request.amount()).thenReturn(amount);

        WalletEntity wallet = mock(WalletEntity.class);
        when(wallet.getBalance()).thenReturn(BigDecimal.valueOf(200));
        when(walletsRepository.findByUserId(userId)).thenReturn(Optional.of(wallet));

        withdrawService.doExecute(request);

        verify(walletService).withdraw(wallet, amount, OperationType.WITHDRAW, "Withdrawal for user ID: " + userId);
        verify(walletsRepository).update(wallet);
    }

    @Test
    void shouldThrowExceptionWhenBalanceIsInsufficient() {
        String userId = "user-2";
        BigDecimal amount = BigDecimal.valueOf(300);
        WithDrawRequest request = mock(WithDrawRequest.class);
        when(request.userId()).thenReturn(userId);
        when(request.amount()).thenReturn(amount);

        WalletEntity wallet = mock(WalletEntity.class);
        when(wallet.getBalance()).thenReturn(BigDecimal.valueOf(100));
        when(walletsRepository.findByUserId(userId)).thenReturn(Optional.of(wallet));

        DataException ex = assertThrows(DataException.class, () -> withdrawService.doExecute(request));
        assertEquals("Insufficient funds", ex.getMessage());

        verify(walletsRepository, never()).update(any());
    }

    @Test
    void shouldThrowExceptionWhenWalletNotFound() {
        String userId = "user-3";
        WithDrawRequest request = mock(WithDrawRequest.class);
        when(request.userId()).thenReturn(userId);

        when(walletsRepository.findByUserId(userId)).thenReturn(Optional.empty());

        DataException ex = assertThrows(DataException.class, () -> withdrawService.doExecute(request));
        assertEquals("Wallet not found for user ID: " + userId, ex.getMessage());

        verify(walletsRepository, never()).update(any());
    }
}