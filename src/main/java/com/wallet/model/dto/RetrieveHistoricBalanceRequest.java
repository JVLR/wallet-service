package com.wallet.model.dto;

import com.wallet.exception.DataException;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

@Introspected
@Serdeable
public record RetrieveHistoricBalanceRequest(String userId, LocalDateTime startDate, LocalDateTime endDate) {

    public RetrieveHistoricBalanceRequest {
        if (StringUtils.isBlank(userId)) {
            throw new DataException("Wallet ID cannot be null or blank");
        }
        if (startDate == null || endDate == null) {
            throw new DataException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new DataException("Start date cannot be after end date");
        }
    }
}
