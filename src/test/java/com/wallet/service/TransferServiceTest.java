package com.wallet.service;

import com.wallet.exception.DataException;
import com.wallet.model.dto.TransferRequest;
import com.wallet.repository.WalletsRepositoryJpa;
import com.wallet.repository.entity.WalletEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransferServiceTest {

    private WalletsRepositoryJpa walletsRepository;
    private TransferService service;
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        walletsRepository = mock(WalletsRepositoryJpa.class);
        walletService = mock(WalletService.class);
        service = new TransferService(walletsRepository, walletService);
    }

    @Test
    void shouldTransferSuccessfully() {
        String from = "user1";
        String to = "user2";
        BigDecimal amount = BigDecimal.TEN;

        WalletEntity walletFrom = mock(WalletEntity.class);
        WalletEntity walletTo = mock(WalletEntity.class);

        when(walletsRepository.findByUserId(from)).thenReturn(Optional.of(walletFrom));
        when(walletsRepository.findByUserId(to)).thenReturn(Optional.of(walletTo));

        TransferRequest request = new TransferRequest(from, to, amount);

        service.doExecute(request);

        verify(walletService).transfer(walletFrom, walletTo, amount);
        verify(walletsRepository).updateAll(List.of(walletFrom, walletTo));
    }

    @Test
    void shouldThrowExceptionWhenWalletFromNotFound() {
        String from = "user1";
        String to = "user2";
        BigDecimal amount = BigDecimal.TEN;

        when(walletsRepository.findByUserId(from)).thenReturn(Optional.empty());

        TransferRequest request = new TransferRequest(from, to, amount);

        DataException ex = assertThrows(DataException.class, () -> service.doExecute(request));
        assertTrue(ex.getMessage().contains("Wallet not found for user ID: " + from));
    }

    @Test
    void shouldThrowExceptionWhenWalletToNotFound() {
        String from = "user1";
        String to = "user2";
        BigDecimal amount = BigDecimal.TEN;

        WalletEntity walletFrom = mock(WalletEntity.class);

        when(walletsRepository.findByUserId(from)).thenReturn(Optional.of(walletFrom));
        when(walletsRepository.findByUserId(to)).thenReturn(Optional.empty());

        TransferRequest request = new TransferRequest(from, to, amount);

        DataException ex = assertThrows(DataException.class, () -> service.doExecute(request));
        assertTrue(ex.getMessage().contains("Wallet not found for user ID: " + to));
    }
}