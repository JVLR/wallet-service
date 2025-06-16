package com.wallet.model.dto;

import com.wallet.exception.DataException;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import org.apache.commons.lang3.StringUtils;

@Introspected
@Serdeable
public record RetrieveBalanceRequest(String userId) {

    public RetrieveBalanceRequest {
        if (StringUtils.isBlank(userId)) {
            throw new DataException("User ID cannot be empty");
        }
    }
}
