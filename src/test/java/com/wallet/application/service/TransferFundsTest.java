//package com.wallet.application.usecase;
//
//import com.wallet.model.dto.TransferFundsInput;
//import com.wallet.domain.model.Wallet;
//import com.wallet.domain.repository.WalletsRepository;
//import com.wallet.exception.DataNotFoundException;
//import com.wallet.exception.InputDataException;
//import com.wallet.exception.InsufficientFundException;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class TransferFundsTest {
//    private WalletsRepository walletsRepository;
//    private TransferFunds transferFunds;
//
//    @BeforeEach
//    void setUp() {
//        walletsRepository = mock(WalletsRepository.class);
//        transferFunds = new TransferFunds(walletsRepository);
//    }
//
//    @Test
//    void shouldThrowInputDataExceptionWhenFromUserIdIsNullOrBlank() {
//        assertThrows(InputDataException.class, () -> new TransferFundsInput(null, "to", BigDecimal.TEN));
//        assertThrows(InputDataException.class, () -> new TransferFundsInput("", "to", BigDecimal.TEN));
//    }
//
//    @Test
//    void shouldThrowInputDataExceptionWhenToUserIdIsNullOrBlank() {
//        assertThrows(InputDataException.class, () -> new TransferFundsInput("from", null, BigDecimal.TEN));
//        assertThrows(InputDataException.class, () -> new TransferFundsInput("from", "", BigDecimal.TEN));
//    }
//
//    @Test
//    void shouldThrowInputDataExceptionWhenAmountIsNullOrZeroOrNegative() {
//        assertThrows(InputDataException.class, () -> new TransferFundsInput("from", "to", null));
//        assertThrows(InputDataException.class, () -> new TransferFundsInput("from", "to", BigDecimal.ZERO));
//        assertThrows(InputDataException.class, () -> new TransferFundsInput("from", "to", new BigDecimal("-1")));
//    }
//
//    @Test
//    void shouldThrowDataNotFoundExceptionWhenFromUserWalletNotFound() {
//        TransferFundsInput input = new TransferFundsInput("from", "to", BigDecimal.TEN);
//        when(walletsRepository.findByUserId("from")).thenReturn(Optional.empty());
//        when(walletsRepository.findByUserId("to")).thenReturn(Optional.of(mock(Wallet.class)));
//        assertThrows(DataNotFoundException.class, () -> transferFunds.execute(input));
//    }
//
//    @Test
//    void shouldThrowDataNotFoundExceptionWhenToUserWalletNotFound() {
//        TransferFundsInput input = new TransferFundsInput("from", "to", BigDecimal.TEN);
//        when(walletsRepository.findByUserId("from")).thenReturn(Optional.of(mock(Wallet.class)));
//        when(walletsRepository.findByUserId("to")).thenReturn(Optional.empty());
//        assertThrows(DataNotFoundException.class, () -> transferFunds.execute(input));
//    }
//
//    @Test
//    void shouldThrowInsufficientFundExceptionWhenFromUserHasNoFunds() {
//        UUID fromUserId = UUID.randomUUID();
//        UUID toUserId = UUID.randomUUID();
//        TransferFundsInput input = new TransferFundsInput(fromUserId.toString(), toUserId.toString(), BigDecimal.TEN);
//
//        Wallet fromWallet = new Wallet(fromUserId);
//        Wallet toWallet = new Wallet(toUserId);
//
//        when(walletsRepository.findByUserId(fromUserId.toString())).thenReturn(Optional.of(fromWallet));
//        when(walletsRepository.findByUserId(toUserId.toString())).thenReturn(Optional.of(toWallet));
//
//        assertThrows(InsufficientFundException.class, () -> transferFunds.execute(input));
//        assertEquals(BigDecimal.ZERO, fromWallet.getBalance());
//        assertEquals(BigDecimal.ZERO, toWallet.getBalance());
//    }
//
//    @Test
//    void shouldTransferFundsWhenAllValidationsPass() {
//        UUID fromUserId = UUID.randomUUID();
//        UUID toUserId = UUID.randomUUID();
//        TransferFundsInput input = new TransferFundsInput(fromUserId.toString(), toUserId.toString(),
//                BigDecimal.TEN);
//
//        Wallet fromWallet = new Wallet(fromUserId);
//        fromWallet.deposit(new BigDecimal("100.00")); // Set initial balance
//
//        Wallet toWallet = new Wallet(toUserId);
//
//        when(walletsRepository.findByUserId(fromUserId.toString())).thenReturn(Optional.of(fromWallet));
//        when(walletsRepository.findByUserId(toUserId.toString())).thenReturn(Optional.of(toWallet));
//
//        transferFunds.execute(input);
//
//        verify(walletsRepository, times(1)).saveAll(List.of(fromWallet, toWallet));
//        assertEquals(BigDecimal.TEN, toWallet.getBalance());
//        assertEquals(new BigDecimal("90.00"), fromWallet.getBalance());
//    }
//
//}
