package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "transaction", schema = "blockchain_evidence", catalog = "")
public class TransactionEntity {
    private int transactionId;
    private String username;
    private Integer userRemains;
    private Integer transactionMoney;
    private Integer transactionPeople;
    private Object transactionStatus;
    private Object transactionType;
    private Integer storageSize;
    private Timestamp transactionTime;
    private String transactionBlockchainId;
    private Timestamp blockchainTime;

    @Id
    @Column(name = "transaction_id")
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "user_remains")
    public Integer getUserRemains() {
        return userRemains;
    }

    public void setUserRemains(Integer userRemains) {
        this.userRemains = userRemains;
    }

    @Basic
    @Column(name = "transaction_money")
    public Integer getTransactionMoney() {
        return transactionMoney;
    }

    public void setTransactionMoney(Integer transactionMoney) {
        this.transactionMoney = transactionMoney;
    }

    @Basic
    @Column(name = "transaction_people")
    public Integer getTransactionPeople() {
        return transactionPeople;
    }

    public void setTransactionPeople(Integer transactionPeople) {
        this.transactionPeople = transactionPeople;
    }

    @Basic
    @Column(name = "transaction_status")
    public Object getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(Object transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    @Basic
    @Column(name = "transaction_type")
    public Object getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Object transactionType) {
        this.transactionType = transactionType;
    }

    @Basic
    @Column(name = "storage_size")
    public Integer getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(Integer storageSize) {
        this.storageSize = storageSize;
    }

    @Basic
    @Column(name = "transaction_time")
    public Timestamp getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Timestamp transactionTime) {
        this.transactionTime = transactionTime;
    }

    @Basic
    @Column(name = "transaction_blockchain_id")
    public String getTransactionBlockchainId() {
        return transactionBlockchainId;
    }

    public void setTransactionBlockchainId(String transactionBlockchainId) {
        this.transactionBlockchainId = transactionBlockchainId;
    }

    @Basic
    @Column(name = "blockchain_time")
    public Timestamp getBlockchainTime() {
        return blockchainTime;
    }

    public void setBlockchainTime(Timestamp blockchainTime) {
        this.blockchainTime = blockchainTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionEntity that = (TransactionEntity) o;
        return transactionId == that.transactionId && Objects.equals(username, that.username) && Objects.equals(userRemains, that.userRemains) && Objects.equals(transactionMoney, that.transactionMoney) && Objects.equals(transactionPeople, that.transactionPeople) && Objects.equals(transactionStatus, that.transactionStatus) && Objects.equals(transactionType, that.transactionType) && Objects.equals(storageSize, that.storageSize) && Objects.equals(transactionTime, that.transactionTime) && Objects.equals(transactionBlockchainId, that.transactionBlockchainId) && Objects.equals(blockchainTime, that.blockchainTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, username, userRemains, transactionMoney, transactionPeople, transactionStatus, transactionType, storageSize, transactionTime, transactionBlockchainId, blockchainTime);
    }
}
