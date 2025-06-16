package com.wallet.model.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Introspected
@Serdeable
public record RetrieveBalanceResponse(String walletId, BigDecimal balance) {

}
