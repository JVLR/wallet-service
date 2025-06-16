package com.wallet.service;

import com.wallet.exception.DataException;
import com.wallet.model.User;
import com.wallet.model.Wallet;
import com.wallet.model.dto.CreateUserRequest;
import com.wallet.model.dto.CreateUserResponse;
import com.wallet.repository.UsersRepositoryJpa;
import com.wallet.repository.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateUserServiceTest {

    private UsersRepositoryJpa usersRepositoryJpa;
    private CreateUserService createUserService;

    @BeforeEach
    void setUp() {
        usersRepositoryJpa = mock(UsersRepositoryJpa.class);
        createUserService = new CreateUserService(usersRepositoryJpa);
    }

    @Test
    void shouldCreateUserWhenDocumentDoesNotExist() {
        // Arrange
        CreateUserRequest request = mock(CreateUserRequest.class);
        when(request.document()).thenReturn("12345678900");
        User user = mock(User.class);
        when(user.getId()).thenReturn(UUID.randomUUID());
        Wallet wallet = mock(Wallet.class);
        when(wallet.getId()).thenReturn(UUID.randomUUID());
        when(wallet.getUserId()).thenReturn(UUID.randomUUID());
        when(user.getWallet()).thenReturn(wallet);
        when(user.getWallet().getId()).thenReturn(UUID.randomUUID());
        UserEntity entity = mock(UserEntity.class);
        when(request.toUser()).thenReturn(user);
        when(usersRepositoryJpa.findByDocument("12345678900")).thenReturn(Optional.empty());
        when(usersRepositoryJpa.save(any(UserEntity.class))).thenReturn(entity);
        when(entity.toModel()).thenReturn(user);

        // Act
        CreateUserResponse response = createUserService.doExecute(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.userId());
        assertNotNull(response.walletId());
        verify(usersRepositoryJpa).save(any(UserEntity.class));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        // Arrange
        CreateUserRequest request = mock(CreateUserRequest.class);
        when(request.document()).thenReturn("12345678900");
        UserEntity entity = mock(UserEntity.class);
        when(usersRepositoryJpa.findByDocument("12345678900")).thenReturn(Optional.of(entity));

        // Act & Assert
        DataException ex = assertThrows(DataException.class, () -> createUserService.doExecute(request));
        assertTrue(ex.getMessage().contains("Wallet user already exists"));
        verify(usersRepositoryJpa, never()).save(any());
    }
}