package com.wallet.model.dto;

import com.wallet.exception.DataException;
import com.wallet.model.User;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import org.apache.commons.lang3.StringUtils;


@Introspected
@Serdeable
public record CreateUserRequest(String name, String document) {

    public CreateUserRequest {
        if (StringUtils.isBlank(name)) {
            throw new DataException("Name can't be empty");
        }
        if (StringUtils.isBlank(document)) {
            throw new DataException("Document can't empty");
        }
        if (document.length() > 14 || document.length() < 11) {
            throw new DataException("Document must be 11 to 14 characters long");
        }
    }

    public User toUser() {
        return new User(this.name(), this.document());
    }
}
