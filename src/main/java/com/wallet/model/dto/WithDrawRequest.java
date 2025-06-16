package com.wallet.model.dto;

import com.wallet.exception.DataException;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Introspected
@Serdeable
public record WithDrawRequest(String userId, BigDecimal amount) {

    public WithDrawRequest {
        if (StringUtils.isBlank(userId)) {
            throw new DataException("User ID cannot be null or blank");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DataException("Amount must be greater than zero");
        }
    }

}
