package com.wallet.repository.entity;

import com.wallet.model.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.UUID;

@Entity(name = "User")
@Table(name = "users", schema = "wallet")
public class UserEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "document", nullable = false, unique = true)
    private String document;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private WalletEntity wallet;

    public UserEntity() {
    }

    public UserEntity(User user) {
        this.id = user.getId().toString();
        this.name = user.getName();
        this.document = user.getDocument();
        this.wallet = new WalletEntity(user.getWallet());
        this.wallet.setUser(this);
    }

    public UserEntity(String id) {
        this.id = id;
    }

    public User toModel() {
        return new User(UUID.fromString(id), name, document, wallet.toModel());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public WalletEntity getWallet() {
        return wallet;
    }

    public void setWallet(WalletEntity wallet) {
        this.wallet = wallet;
    }
}
