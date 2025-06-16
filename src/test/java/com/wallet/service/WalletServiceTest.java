package com.wallet.service;

import com.wallet.exception.DataException;
import com.wallet.model.OperationType;
import com.wallet.repository.entity.HistoricBalanceEntity;
import com.wallet.repository.entity.WalletEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WalletServiceTest {

    private WalletService service;
    private WalletEntity wallet;

    @BeforeEach
    void setUp() {
        service = new WalletService();
        wallet = mock(WalletEntity.class);
        when(wallet.getBalance()).thenReturn(BigDecimal.valueOf(100));
        when(wallet.getId()).thenReturn("wallet-1");
        when(wallet.gethistoricBalance()).thenReturn(new ArrayList<>());
    }

    @Test
    void withdraw_shouldDecreaseBalanceAndAddHistoric() {
        doAnswer(invocation -> {
            when(wallet.getBalance()).thenReturn(BigDecimal.valueOf(80));
            return null;
        }).when(wallet).setBalance(BigDecimal.valueOf(80));

        service.withdraw(wallet, BigDecimal.valueOf(20), OperationType.WITHDRAW, "Test withdraw");

        verify(wallet).setBalance(BigDecimal.valueOf(80));
        assertEquals(1, wallet.gethistoricBalance().size());
        HistoricBalanceEntity historic = wallet.gethistoricBalance().get(0);
        assertEquals("wallet-1", historic.getWallet().getId());
        assertEquals(BigDecimal.valueOf(20), historic.toModel().getAmount());
    }

    @Test
    void withdraw_shouldThrowExceptionForNegativeAmount() {
        DataException ex = assertThrows(DataException.class, () ->
                service.withdraw(wallet, BigDecimal.valueOf(-10), OperationType.WITHDRAW, "Invalid")
        );
        assertTrue(ex.getMessage().contains("Withdrawal amount must be positive"));
    }

    @Test
    void withdraw_shouldThrowExceptionForInsufficientFunds() {
        when(wallet.getBalance()).thenReturn(BigDecimal.valueOf(10));
        DataException ex = assertThrows(DataException.class, () ->
                service.withdraw(wallet, BigDecimal.valueOf(20), OperationType.WITHDRAW, "Insufficient")
        );
        assertTrue(ex.getMessage().contains("Insufficient funds"));
    }

    @Test
    void deposit_shouldIncreaseBalanceAndAddHistoric() {
        doAnswer(invocation -> {
            when(wallet.getBalance()).thenReturn(BigDecimal.valueOf(120));
            return null;
        }).when(wallet).setBalance(BigDecimal.valueOf(120));

        service.deposit(wallet, BigDecimal.valueOf(20));

        verify(wallet).setBalance(BigDecimal.valueOf(120));
        assertEquals(1, wallet.gethistoricBalance().size());
        HistoricBalanceEntity historic = wallet.gethistoricBalance().get(0);
        assertEquals("wallet-1", historic.getWallet().getId());
        assertEquals(BigDecimal.valueOf(20), historic.toModel().getAmount());
    }

    @Test
    void deposit_shouldThrowExceptionForNegativeAmount() {
        DataException ex = assertThrows(DataException.class, () ->
                service.deposit(wallet, BigDecimal.valueOf(-5))
        );
        assertTrue(ex.getMessage().contains("Deposit amount must be positive"));
    }

    @Test
    void transfer_shouldWithdrawFromSourceAndDepositToTarget() {
        WalletEntity target = mock(WalletEntity.class);
        when(target.getBalance()).thenReturn(BigDecimal.valueOf(50));
        when(target.getId()).thenReturn("wallet-2");
        when(target.gethistoricBalance()).thenReturn(new ArrayList<>());

        doAnswer(invocation -> {
            when(wallet.getBalance()).thenReturn(BigDecimal.valueOf(80));
            return null;
        }).when(wallet).setBalance(BigDecimal.valueOf(80));
        doAnswer(invocation -> {
            when(target.getBalance()).thenReturn(BigDecimal.valueOf(70));
            return null;
        }).when(target).setBalance(BigDecimal.valueOf(70));

        service.transfer(wallet, target, BigDecimal.valueOf(20));

        verify(wallet).setBalance(BigDecimal.valueOf(80));
        verify(target).setBalance(BigDecimal.valueOf(70));
        assertEquals(1, wallet.gethistoricBalance().size());
        assertEquals(1, target.gethistoricBalance().size());
    }

    @Test
    void transfer_shouldThrowExceptionIfTargetIsNull() {
        DataException ex = assertThrows(DataException.class, () ->
                service.transfer(wallet, null, BigDecimal.valueOf(10))
        );
        assertTrue(ex.getMessage().contains("Target wallet cannot be null"));
    }
}