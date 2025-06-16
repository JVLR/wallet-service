package com.wallet.controller;

import com.wallet.factory.LoggerFactoryService;
import com.wallet.model.dto.CreateUserRequest;
import com.wallet.model.dto.CreateUserResponse;
import com.wallet.model.dto.DepositRequest;
import com.wallet.model.dto.RetrieveBalanceRequest;
import com.wallet.model.dto.RetrieveBalanceResponse;
import com.wallet.model.dto.RetrieveHistoricBalanceRequest;
import com.wallet.model.dto.RetrieveHistoricBalanceResponse;
import com.wallet.model.dto.TransferRequest;
import com.wallet.model.dto.WithDrawRequest;
import com.wallet.service.CreateUserService;
import com.wallet.service.DepositService;
import com.wallet.service.RetrieveBalanceService;
import com.wallet.service.RetrieveHistoricBalanceService;
import com.wallet.service.TransferService;
import com.wallet.service.WithdrawService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WalletControllerTest {

    private CreateUserService createUserService;
    private DepositService depositService;
    private RetrieveBalanceService retrieveBalanceService;
    private RetrieveHistoricBalanceService retrieveHistoricBalanceService;
    private TransferService transferService;
    private WithdrawService withdrawService;
    private LoggerFactoryService loggerFactoryService;
    private Logger logger;
    private WalletController controller;

    @BeforeEach
    void setUp() {
        createUserService = mock(CreateUserService.class);
        depositService = mock(DepositService.class);
        retrieveBalanceService = mock(RetrieveBalanceService.class);
        retrieveHistoricBalanceService = mock(RetrieveHistoricBalanceService.class);
        transferService = mock(TransferService.class);
        withdrawService = mock(WithdrawService.class);
        loggerFactoryService = mock(LoggerFactoryService.class);
        logger = mock(Logger.class);
        when(loggerFactoryService.getLogger(any())).thenReturn(logger);

        controller = new WalletController(
                createUserService,
                depositService,
                retrieveBalanceService,
                retrieveHistoricBalanceService,
                transferService,
                withdrawService,
                loggerFactoryService
        );
    }

    @Test
    void createUser_shouldDelegateToService() {
        CreateUserRequest request = new CreateUserRequest("123", "12345678911");
        CreateUserResponse response = new CreateUserResponse("user-1", "123");
        when(createUserService.doExecute(request)).thenReturn(response);

        CreateUserResponse result = controller.createUser(request);

        verify(createUserService).doExecute(request);
        assertEquals("user-1", result.userId());
    }

    @Test
    void retrieveBalance_shouldDelegateToService() {
        RetrieveBalanceResponse response = new RetrieveBalanceResponse("user-1", BigDecimal.TEN);
        when(retrieveBalanceService.doExecute(any())).thenReturn(response);

        RetrieveBalanceResponse result = controller.retrieveBalance("user-1");

        ArgumentCaptor<RetrieveBalanceRequest> captor = ArgumentCaptor.forClass(RetrieveBalanceRequest.class);
        verify(retrieveBalanceService).doExecute(captor.capture());
        assertEquals("user-1", captor.getValue().userId());
        assertEquals(BigDecimal.TEN, result.balance());
    }

    @Test
    void depositFunds_shouldDelegateToService() {
        DepositRequest request = new DepositRequest("user-1", BigDecimal.TEN);

        controller.depositFunds(request);

        verify(depositService).doExecute(request);
    }

    @Test
    void retrievehistoricBalance_shouldDelegateToService() {
        String userId = "user-1";
        String startDate = "2025-06-14 00:00";
        String endDate = "2025-06-17 00:00";
        LocalDateTime start = LocalDate.parse(startDate, com.wallet.constant.AppConstant.formatteryyyyMMdd).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endDate, com.wallet.constant.AppConstant.formatteryyyyMMdd).atStartOfDay();

        List<RetrieveHistoricBalanceResponse> expected = Collections.emptyList();
        when(retrieveHistoricBalanceService.doExecute(any())).thenReturn(expected);

        List<RetrieveHistoricBalanceResponse> result = controller.retrievehistoricBalance(userId, startDate, endDate);

        ArgumentCaptor<RetrieveHistoricBalanceRequest> captor = ArgumentCaptor.forClass(RetrieveHistoricBalanceRequest.class);
        verify(retrieveHistoricBalanceService).doExecute(captor.capture());
        assertEquals(userId, captor.getValue().userId());
        assertEquals(start, captor.getValue().startDate());
        assertEquals(end, captor.getValue().endDate());
        assertSame(expected, result);
    }

    @Test
    void transferFunds_shouldDelegateToService() {
        TransferRequest request = new TransferRequest("from", "to", BigDecimal.ONE);

        controller.transferFunds(request);

        verify(transferService).doExecute(request);
    }

    @Test
    void withdrawFunds_shouldDelegateToService() {
        WithDrawRequest request = new WithDrawRequest("user-1", BigDecimal.ONE);

        controller.withdrawFunds(request);

        verify(withdrawService).doExecute(request);
    }
}