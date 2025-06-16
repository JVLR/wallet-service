package com.wallet.repository;

import com.wallet.repository.entity.UserEntity;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UsersRepositoryJpa extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByDocument(String document);
}