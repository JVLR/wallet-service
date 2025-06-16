package com.wallet.controller;

import com.wallet.constant.AppConstant;
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
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.List;

@Controller("/recpay")
@Tag(name = "Wallet Service Assignment")
public class WalletController {

    private final CreateUserService createUserService;
    private final DepositService depositService;
    private final RetrieveBalanceService retrieveBalanceService;
    private final RetrieveHistoricBalanceService retrievehistoricBalanceService;
    private final TransferService transferService;
    private final WithdrawService withdrawService;
    private final Logger log;

    public WalletController(
            CreateUserService createUserService,
            DepositService depositService,
            RetrieveBalanceService retrieveBalanceService,
            RetrieveHistoricBalanceService retrievehistoricBalanceService,
            TransferService transferService,
            WithdrawService withdrawService, LoggerFactoryService loggerFactoryService) {
        this.createUserService = createUserService;
        this.depositService = depositService;
        this.retrieveBalanceService = retrieveBalanceService;
        this.retrievehistoricBalanceService = retrievehistoricBalanceService;
        this.transferService = transferService;
        this.withdrawService = withdrawService;
        this.log = loggerFactoryService.getLogger(WalletController.class);
    }

    @Post(uri = "/user", produces = MediaType.APPLICATION_JSON)
    @Secured("isAuthenticated()")
    public CreateUserResponse createUser(@Body CreateUserRequest input) {
        log.info("Create user request: documentId: {} - name: {}", input.document(), input.name());

        return createUserService.doExecute(input);
    }

    @Get(uri = "/{id}/balance", produces = MediaType.APPLICATION_JSON)
    @Secured("isAuthenticated()")
    public RetrieveBalanceResponse retrieveBalance(@PathVariable String id) {
        log.info("Requesting balance status for userId: {}", id);

        return retrieveBalanceService.doExecute(new RetrieveBalanceRequest(id));
    }

    @Post(uri = "/deposits", produces = MediaType.APPLICATION_JSON)
    @Secured("isAuthenticated()")
    public void depositFunds(@Body DepositRequest input) {
        log.info("Deposit funds request: userId: {} - amount: {}", input.userId(), input.amount());

        depositService.doExecute(input);
    }

    @Get(uri = "/{id}/historic-balance", produces = MediaType.APPLICATION_JSON)
    @Secured("isAuthenticated()")
    public List<RetrieveHistoricBalanceResponse> retrievehistoricBalance(
            @PathVariable String id,
            @QueryValue String startDate,
            @QueryValue String endDate) {

        LocalDateTime startTime = LocalDateTime.parse(startDate, AppConstant.formatteryyyyMMdd);
        LocalDateTime endTime = LocalDateTime.parse(endDate, AppConstant.formatteryyyyMMdd);

        log.info("Requesting historic balance for userId: {} - toDate: {} - between {}:",id, startDate, endDate);

        return retrievehistoricBalanceService
                .doExecute(new RetrieveHistoricBalanceRequest(id, startTime, endTime));
    }

    @Post(uri = "/transfers", produces = MediaType.APPLICATION_JSON)
    @Secured("isAuthenticated()")
    public void transferFunds(@Body TransferRequest input) {

        log.info("Transfer funds request: fromUserId: {} - toUserId: {} - amount: {}", input.from(),
                input.to(), input.amount());

        transferService.doExecute(input);
    }

    @Post(uri = "/withdrawals", produces = MediaType.APPLICATION_JSON)
    @Secured("isAuthenticated()")
    public void withdrawFunds(@Body WithDrawRequest input) {

        log.info("Withdraw funds request: userId: {} - amount: {}", input.userId(), input.amount());

        withdrawService.doExecute(input);
    }
}