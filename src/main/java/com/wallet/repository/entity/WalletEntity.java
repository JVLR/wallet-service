package com.wallet.repository.entity;

import com.wallet.exception.DataException;
import com.wallet.model.HistoricBalance;
import com.wallet.model.OperationType;
import com.wallet.model.Wallet;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity(name = "Wallet")
@Table(name = "wallets", schema = "wallet")
public class WalletEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<HistoricBalanceEntity> historicBalance;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    public WalletEntity() {
    }

    public WalletEntity(Wallet wallet) {
        this.id = wallet.getId().toString();
        this.user = new UserEntity(wallet.getUserId().toString());
        this.balance = wallet.getBalance();
        this.historicBalance = wallet.gethistoricBalances().stream()
                .map(historicBalance -> {
                    HistoricBalanceEntity historical = new HistoricBalanceEntity(historicBalance);
                    historical.setWallet(this);
                    return historical;
                })
                .toList();
    }

    public Wallet toModel() {
        return new Wallet(UUID.fromString(id), UUID.fromString(user.getId()), balance, historicBalance.stream()
                .map(HistoricBalanceEntity::toModel).collect(Collectors.toSet()));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return user != null ? user.getId() : null;
    }

    public void setUserId(String userId) {
        this.user.setId(userId);
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<HistoricBalanceEntity> gethistoricBalance() {
        return historicBalance;
    }

    public void sethistoricBalance(List<HistoricBalanceEntity> historicBalance) {
        this.historicBalance = historicBalance;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
