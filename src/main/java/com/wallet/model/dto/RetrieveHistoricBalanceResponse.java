package com.wallet.model.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Introspected
@Serdeable
public record RetrieveHistoricBalanceResponse(BigDecimal amount, String operation, LocalDateTime date) {
}
