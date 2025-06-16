//package com.wallet.application.usecase;
//
//import com.wallet.model.dto.DepositFundsInput;
//import com.wallet.domain.model.Wallet;
//import com.wallet.domain.repository.WalletsRepository;
//import com.wallet.domain.repository.WalletsRepositoryJpa;
//import com.wallet.exception.DataNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import java.math.BigDecimal;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class DepositFundsTest {
//    private WalletsRepositoryJpa walletsRepositoryJpa;
//    private DepositFunds depositFunds;
//
//    @BeforeEach
//    void setUp() {
//        walletsRepositoryJpa = mock(WalletsRepositoryJpa.class);
//        depositFunds = new DepositFunds(walletsRepositoryJpa);
//    }
//
//    @Test
//    void shouldThrowDataNotFoundExceptionWhenWalletNotFound() {
//        DepositFundsInput input = mock(DepositFundsInput.class);
//        var uuid = UUID.randomUUID();
//        when(input.userId()).thenReturn(uuid.toString());
//        when(walletsRepositoryJpa.findByUserId(uuid.toString())).thenReturn(Optional.empty());
//        assertThrows(DataNotFoundException.class, () -> depositFunds.execute(input));
//    }
//
//    @Test
//    void shouldNotDepositWhenAmountIsZero() {
//        DepositFundsInput input = mock(DepositFundsInput.class);
//        Wallet wallet = mock(Wallet.class);
//        var uuid = UUID.randomUUID();
//        when(input.userId()).thenReturn(uuid.toString());
//        when(input.amount()).thenReturn(BigDecimal.ZERO);
//        when(walletsRepositoryJpa.findByUserId(uuid.toString())).thenReturn(Optional.of(wallet));
//        depositFunds.execute(input);
//        verify(wallet, times(1)).deposit(BigDecimal.ZERO);
//        verify(walletsRepositoryJpa, times(1)).save(wallet);
//    }
//
//    @Test
//    void shouldNotDepositWhenAmountIsNegative() {
//        DepositFundsInput input = mock(DepositFundsInput.class);
//        Wallet wallet = mock(Wallet.class);
//        var uuid = UUID.randomUUID();
//        when(input.userId()).thenReturn(uuid.toString());
//        when(input.amount()).thenReturn(new BigDecimal("-10.00"));
//        when(walletsRepositoryJpa.findByUserId(uuid.toString())).thenReturn(Optional.of(wallet));
//        depositFunds.execute(input);
//        verify(wallet, times(1)).deposit(new BigDecimal("-10.00"));
//        verify(walletsRepositoryJpa, times(1)).save(wallet);
//    }
//
//    @Test
//    void shouldDepositSuccessfully() {
//        DepositFundsInput input = mock(DepositFundsInput.class);
//        Wallet wallet = mock(Wallet.class);
//        var uuid = UUID.randomUUID();
//        when(input.userId()).thenReturn(uuid.toString());
//        when(input.amount()).thenReturn(new BigDecimal("100.00"));
//        when(walletsRepository.findByUserId(uuid.toString())).thenReturn(Optional.of(wallet));
//        depositFunds.execute(input);
//        verify(wallet, times(1)).deposit(new BigDecimal("100.00"));
//        verify(walletsRepository, times(1)).save(wallet);
//    }
//}
