package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "evidence", schema = "blockchain_evidence", catalog = "")
public class EvidenceEntity {
    private String evidenceId;
    private String userId;
    private Object evidenceType;
    private String evidenceName;
    private String filePath;
    private String fileSize;
    private String fileHash;
    private String evidenceBlockchainId;
    private Timestamp blockchainTime;
    private Timestamp evidenceTime;
    private String organizationId;
    private String notaryId;
    private Object notarizationStatus;
    private String transactionId;
    private Timestamp notarizationStartTime;
    private String notarizationMoney;
    private String notarizationType;
    private String notarizationInformation;
    private Timestamp notarizationEndTime;
    private String notarizationMatters;
    private String notarizationBlockchainIdStart;
    private String notarizationBlockchainIdEnd;
    private Object transactionStatus;
    private UserEntity userByUserId;
    private OrganizationEntity organizationByOrganizationId;
    private NotaryEntity notaryByNotaryId;
    private TransactionEntity transactionByTransactionId;


    //我手动加的
    private String username;
    private String notaryName;
    private String organizationName;
    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    @Basic
    @Column(name = "notaryName")
    public String getNotaryName() {
        return notaryName;
    }
    public void setNotaryName(String notaryName) {
        this.notaryName = notaryName;
    }
    @Basic
    @Column(name = "organizationName")
    public String getOrganizationName() {
        return organizationName;
    }
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }







    @Id
    @Column(name = "evidenceId")
    public String getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(String evidenceId) {
        this.evidenceId = evidenceId;
    }

    @Basic
    @Column(name = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "evidenceType")
    public Object getEvidenceType() {
        return evidenceType;
    }

    public void setEvidenceType(Object evidenceType) {
        this.evidenceType = evidenceType;
    }

    @Basic
    @Column(name = "evidenceName")
    public String getEvidenceName() {
        return evidenceName;
    }

    public void setEvidenceName(String evidenceName) {
        this.evidenceName = evidenceName;
    }

    @Basic
    @Column(name = "filePath")
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Basic
    @Column(name = "fileSize")
    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    @Basic
    @Column(name = "fileHash")
    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    @Basic
    @Column(name = "evidenceBlockchainId")
    public String getEvidenceBlockchainId() {
        return evidenceBlockchainId;
    }

    public void setEvidenceBlockchainId(String evidenceBlockchainId) {
        this.evidenceBlockchainId = evidenceBlockchainId;
    }

    @Basic
    @Column(name = "blockchainTime")
    public Timestamp getBlockchainTime() {
        return blockchainTime;
    }

    public void setBlockchainTime(Timestamp blockchainTime) {
        this.blockchainTime = blockchainTime;
    }

    @Basic
    @Column(name = "evidenceTime")
    public Timestamp getEvidenceTime() {
        return evidenceTime;
    }

    public void setEvidenceTime(Timestamp evidenceTime) {
        this.evidenceTime = evidenceTime;
    }

    @Basic
    @Column(name = "organizationId")
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Basic
    @Column(name = "notaryId")
    public String getNotaryId() {
        return notaryId;
    }

    public void setNotaryId(String notaryId) {
        this.notaryId = notaryId;
    }

    @Basic
    @Column(name = "notarizationStatus")
    public Object getNotarizationStatus() {
        return notarizationStatus;
    }

    public void setNotarizationStatus(Object notarizationStatus) {
        this.notarizationStatus = notarizationStatus;
    }

    @Basic
    @Column(name = "transactionID")
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Basic
    @Column(name = "notarizationStartTime")
    public Timestamp getNotarizationStartTime() {
        return notarizationStartTime;
    }

    public void setNotarizationStartTime(Timestamp notarizationStartTime) {
        this.notarizationStartTime = notarizationStartTime;
    }

    @Basic
    @Column(name = "notarizationMoney")
    public String getNotarizationMoney() {
        return notarizationMoney;
    }

    public void setNotarizationMoney(String notarizationMoney) {
        this.notarizationMoney = notarizationMoney;
    }

    @Basic
    @Column(name = "notarizationType")
    public String getNotarizationType() {
        return notarizationType;
    }

    public void setNotarizationType(String notarizationType) {
        this.notarizationType = notarizationType;
    }

    @Basic
    @Column(name = "notarizationInformation")
    public String getNotarizationInformation() {
        return notarizationInformation;
    }

    public void setNotarizationInformation(String notarizationInformation) {
        this.notarizationInformation = notarizationInformation;
    }

    @Basic
    @Column(name = "notarizationEndTime")
    public Timestamp getNotarizationEndTime() {
        return notarizationEndTime;
    }

    public void setNotarizationEndTime(Timestamp notarizationEndTime) {
        this.notarizationEndTime = notarizationEndTime;
    }

    @Basic
    @Column(name = "notarizationMatters")
    public String getNotarizationMatters() {
        return notarizationMatters;
    }

    public void setNotarizationMatters(String notarizationMatters) {
        this.notarizationMatters = notarizationMatters;
    }

    @Basic
    @Column(name = "notarizationBlockchainIdStart")
    public String getNotarizationBlockchainIdStart() {
        return notarizationBlockchainIdStart;
    }

    public void setNotarizationBlockchainIdStart(String notarizationBlockchainIdStart) {
        this.notarizationBlockchainIdStart = notarizationBlockchainIdStart;
    }

    @Basic
    @Column(name = "notarizationBlockchainIdEnd")
    public String getNotarizationBlockchainIdEnd() {
        return notarizationBlockchainIdEnd;
    }

    public void setNotarizationBlockchainIdEnd(String notarizationBlockchainIdEnd) {
        this.notarizationBlockchainIdEnd = notarizationBlockchainIdEnd;
    }

    @Basic
    @Column(name = "transactionStatus")
    public Object getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(Object transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvidenceEntity that = (EvidenceEntity) o;
        return Objects.equals(evidenceId, that.evidenceId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(evidenceType, that.evidenceType) &&
                Objects.equals(evidenceName, that.evidenceName) &&
                Objects.equals(filePath, that.filePath) &&
                Objects.equals(fileSize, that.fileSize) &&
                Objects.equals(fileHash, that.fileHash) &&
                Objects.equals(evidenceBlockchainId, that.evidenceBlockchainId) &&
                Objects.equals(blockchainTime, that.blockchainTime) &&
                Objects.equals(evidenceTime, that.evidenceTime) &&
                Objects.equals(organizationId, that.organizationId) &&
                Objects.equals(notaryId, that.notaryId) &&
                Objects.equals(notarizationStatus, that.notarizationStatus) &&
                Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(notarizationStartTime, that.notarizationStartTime) &&
                Objects.equals(notarizationMoney, that.notarizationMoney) &&
                Objects.equals(notarizationType, that.notarizationType) &&
                Objects.equals(notarizationInformation, that.notarizationInformation) &&
                Objects.equals(notarizationEndTime, that.notarizationEndTime) &&
                Objects.equals(notarizationMatters, that.notarizationMatters) &&
                Objects.equals(notarizationBlockchainIdStart, that.notarizationBlockchainIdStart) &&
                Objects.equals(notarizationBlockchainIdEnd, that.notarizationBlockchainIdEnd) &&
                Objects.equals(transactionStatus, that.transactionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(evidenceId, userId, evidenceType, evidenceName, filePath, fileSize, fileHash, evidenceBlockchainId, blockchainTime, evidenceTime, organizationId, notaryId, notarizationStatus, transactionId, notarizationStartTime, notarizationMoney, notarizationType, notarizationInformation, notarizationEndTime, notarizationMatters, notarizationBlockchainIdStart, notarizationBlockchainIdEnd, transactionStatus);
    }

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    public UserEntity getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(UserEntity userByUserId) {
        this.userByUserId = userByUserId;
    }

    @ManyToOne
    @JoinColumn(name = "organizationId", referencedColumnName = "organizationId")
    public OrganizationEntity getOrganizationByOrganizationId() {
        return organizationByOrganizationId;
    }

    public void setOrganizationByOrganizationId(OrganizationEntity organizationByOrganizationId) {
        this.organizationByOrganizationId = organizationByOrganizationId;
    }

    @ManyToOne
    @JoinColumn(name = "notaryId", referencedColumnName = "notaryId")
    public NotaryEntity getNotaryByNotaryId() {
        return notaryByNotaryId;
    }

    public void setNotaryByNotaryId(NotaryEntity notaryByNotaryId) {
        this.notaryByNotaryId = notaryByNotaryId;
    }

    @ManyToOne
    @JoinColumn(name = "transactionID", referencedColumnName = "transactionId")
    public TransactionEntity getTransactionByTransactionId() {
        return transactionByTransactionId;
    }

    public void setTransactionByTransactionId(TransactionEntity transactionByTransactionId) {
        this.transactionByTransactionId = transactionByTransactionId;
    }
}
