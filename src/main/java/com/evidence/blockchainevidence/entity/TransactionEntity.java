package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "transaction", schema = "blockchain_evidence", catalog = "")
public class TransactionEntity {
    private String transactionId;
    private String userRemains;
    private String transactionMoney;
    private String transactionPeople;
    private Object transactionType;
    private String storageSize;
    private Timestamp transactionTime;
    private String transactionBlockchainId;
    private Timestamp blockchainTime;
    private Collection<EvidenceEntity> evidencesByTransactionId;
    private UserEntity userByUserId;
    private String userId;



    //我手动加的
    private String username;
    private String transactionPeopleName;

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "transactionPeopleName")
    public String getTransactionPeopleName() {
        return transactionPeopleName;
    }
    public void setTransactionPeopleName(String transactionPeopleName) {
        this.transactionPeopleName = transactionPeopleName;
    }










    @Id
    @Column(name = "transactionId")
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Basic
    @Column(name = "userRemains")
    public String getUserRemains() {
        return userRemains;
    }

    public void setUserRemains(String userRemains) {
        this.userRemains = userRemains;
    }

    @Basic
    @Column(name = "transactionMoney")
    public String getTransactionMoney() {
        return transactionMoney;
    }

    public void setTransactionMoney(String transactionMoney) {
        this.transactionMoney = transactionMoney;
    }

    @Basic
    @Column(name = "transactionPeople")
    public String getTransactionPeople() {
        return transactionPeople;
    }

    public void setTransactionPeople(String transactionPeople) {
        this.transactionPeople = transactionPeople;
    }

    @Basic
    @Column(name = "transactionType")
    public Object getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Object transactionType) {
        this.transactionType = transactionType;
    }

    @Basic
    @Column(name = "storageSize")
    public String getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(String storageSize) {
        this.storageSize = storageSize;
    }

    @Basic
    @Column(name = "transactionTime")
    public Timestamp getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Timestamp transactionTime) {
        this.transactionTime = transactionTime;
    }

    @Basic
    @Column(name = "transactionBlockchainId")
    public String getTransactionBlockchainId() {
        return transactionBlockchainId;
    }

    public void setTransactionBlockchainId(String transactionBlockchainId) {
        this.transactionBlockchainId = transactionBlockchainId;
    }

    @Basic
    @Column(name = "blockchainTime")
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
        return Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(userRemains, that.userRemains) &&
                Objects.equals(transactionMoney, that.transactionMoney) &&
                Objects.equals(transactionPeople, that.transactionPeople) &&
                Objects.equals(transactionType, that.transactionType) &&
                Objects.equals(storageSize, that.storageSize) &&
                Objects.equals(transactionTime, that.transactionTime) &&
                Objects.equals(transactionBlockchainId, that.transactionBlockchainId) &&
                Objects.equals(blockchainTime, that.blockchainTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, userRemains, transactionMoney, transactionPeople, transactionType, storageSize, transactionTime, transactionBlockchainId, blockchainTime);
    }

    @OneToMany(mappedBy = "transactionByTransactionId")
    public Collection<EvidenceEntity> getEvidencesByTransactionId() {
        return evidencesByTransactionId;
    }

    public void setEvidencesByTransactionId(Collection<EvidenceEntity> evidencesByTransactionId) {
        this.evidencesByTransactionId = evidencesByTransactionId;
    }

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    public UserEntity getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(UserEntity userByUserId) {
        this.userByUserId = userByUserId;
    }

    @Basic
    @Column(name = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
