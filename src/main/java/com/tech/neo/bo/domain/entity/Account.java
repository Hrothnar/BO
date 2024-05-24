package com.tech.neo.bo.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity()
@Table(name = "accounts")
public class Account {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "initial_deposit", unique = false, nullable = true, insertable = true, updatable = false)
    private BigDecimal initialDeposit;

    @Column(name = "balance", unique = false, nullable = true, insertable = true, updatable = true)
    private BigDecimal balance;

    @OneToOne(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true, nullable = false, insertable = true, updatable = true, foreignKey = @ForeignKey(name = "id", value = ConstraintMode.PROVIDER_DEFAULT))
    private User user;

    public Account() {

    }

    public Account(User user, BigDecimal initialDeposit) {
        this.user = user;
        this.initialDeposit = initialDeposit;
        this.balance = initialDeposit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
