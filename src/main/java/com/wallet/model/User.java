package com.wallet.model;

import java.util.UUID;

public class User {

    private final UUID id;
    private final String name;
    private final String document;
    private Wallet wallet;

    public User(String name, String document) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.document = document;
        this.wallet = new Wallet(id);
    }

    public User(UUID id, String name, String document, Wallet wallet) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.wallet = wallet;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDocument() {
        return document;
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User user))
            return false;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", document='" + document + '\'' +
                ", wallet=" + wallet +
                '}';
    }
}
