package com.wallet.model.dto;

import com.wallet.exception.DataException;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Introspected
@Serdeable
public record TransferRequest(String from, String to, BigDecimal amount) {

    public TransferRequest {

        if (StringUtils.isBlank(from)) {
            throw new DataException("From User ID cannot be empty");
        }
        if (StringUtils.isBlank(to)) {
            throw new DataException("To User ID cannot be empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DataException("Amount must be greater than zero");
        }
        if (from.equals(to)) {
            throw new DataException("From User ID and To User ID cannot be the same");
        }
    }
}
