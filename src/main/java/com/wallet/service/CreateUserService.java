package com.wallet.service;

import com.wallet.exception.DataException;
import com.wallet.model.dto.CreateUserRequest;
import com.wallet.model.dto.CreateUserResponse;
import com.wallet.model.User;
import com.wallet.repository.UsersRepositoryJpa;
import com.wallet.repository.entity.UserEntity;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class CreateUserService {

    private final UsersRepositoryJpa usersRepositoryJpa;

    public CreateUserService(UsersRepositoryJpa usersRepositoryJpa) {
        this.usersRepositoryJpa = usersRepositoryJpa;
    }

    public CreateUserResponse doExecute(CreateUserRequest input) {

        Optional<UserEntity> userWallet = usersRepositoryJpa.findByDocument(input.document());

        if (userWallet.isPresent()) {
            throw new DataException("Wallet user already exists with document: " + input.document());
        }
        UserEntity entity = new UserEntity(input.toUser());

        User user = usersRepositoryJpa.save(entity).toModel();

        return new CreateUserResponse(user.getId().toString(), user.getWallet().getId().toString());
    }
}
