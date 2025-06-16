//package com.wallet.domain.model;
//
//import com.wallet.exception.InputDataException;
//import com.wallet.exception.InsufficientFundException;
//import com.wallet.model.OperationType;
//import com.wallet.model.Wallet;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//class WalletTest {
//    @Test
//    void depositShouldIncreaseBalanceAndAddHistory() {
//        Wallet wallet = new Wallet(UUID.randomUUID());
//        wallet.deposit(new BigDecimal("100.00"));
//        assertEquals(new BigDecimal("100.00"), wallet.getBalance());
//        assertFalse(wallet.gethistoricBalances().isEmpty());
//        assertEquals(1, wallet.gethistoricBalances().size());
//    }
//
//    @Test
//    void depositShouldThrowExceptionForZeroOrNegative() {
//        Wallet wallet = new Wallet(UUID.randomUUID());
//        assertThrows(InputDataException.class, () -> wallet.deposit(BigDecimal.ZERO));
//        assertThrows(InputDataException.class, () -> wallet.deposit(new BigDecimal("-10.00")));
//    }
//
//    @Test
//    void withdrawShouldDecreaseBalanceAndAddHistory() {
//        Wallet wallet = new Wallet(UUID.randomUUID());
//        wallet.deposit(new BigDecimal("100.00"));
//        wallet.withdraw(new BigDecimal("50.00"), OperationType.WITHDRAW, "Test withdraw");
//        assertEquals(new BigDecimal("50.00"), wallet.getBalance());
//
//        assertEquals(2, wallet.gethistoricBalances().size());
//    }
//
//    @Test
//    void withdrawShouldThrowExceptionForZeroNegativeOrInsufficient() {
//        Wallet wallet = new Wallet(UUID.randomUUID());
//        wallet.deposit(new BigDecimal("20.00"));
//        assertThrows(InputDataException.class,
//                () -> wallet.withdraw(BigDecimal.ZERO, OperationType.WITHDRAW, ""));
//        assertThrows(InputDataException.class,
//                () -> wallet.withdraw(new BigDecimal("-5.00"), OperationType.WITHDRAW, ""));
//        assertThrows(InsufficientFundException.class,
//                () -> wallet.withdraw(new BigDecimal("30.00"), OperationType.WITHDRAW, ""));
//    }
//
//    @Test
//    void transferShouldMoveFundsBetweenWallets() {
//        Wallet from = new Wallet(UUID.randomUUID());
//        Wallet to = new Wallet(UUID.randomUUID());
//        from.deposit(new BigDecimal("100.00"));
//        from.transfer(to, new BigDecimal("40.00"));
//        assertEquals(new BigDecimal("60.00"), from.getBalance());
//        assertEquals(new BigDecimal("40.00"), to.getBalance());
//    }
//
//    @Test
//    void transferShouldThrowExceptionForNullTarget() {
//        Wallet from = new Wallet(UUID.randomUUID());
//        from.deposit(new BigDecimal("100.00"));
//        assertThrows(InputDataException.class, () -> from.transfer(null, new BigDecimal("10.00")));
//    }
//}
