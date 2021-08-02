package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "evidence", schema = "blockchain_evidence", catalog = "")
public class EvidenceEntity {
    private int evidenceId;
    private Integer userId;
    private Object evidenceType;
    private String evidenceName;
    private String filePath;
    private Integer fileSize;
    private String fileHash;
    private String evidenceBlockchainId;
    private Timestamp blockchainTime;
    private Timestamp evidenceTime;
    private Integer organizationId;
    private Integer notaryId;
    private Object notarizationStatus;
    private Integer transactionId;
    private Timestamp notarizationStartTime;
    private String notarizationBlockchainId;
    private Integer notarizationMoney;
    private Object notarizationType;
    private String notarizationInformation;
    private Timestamp notarizationEndTime;
    private String notarizationMatters;

    @Id
    @Column(name = "evidence_id")
    public int getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(int evidenceId) {
        this.evidenceId = evidenceId;
    }

    @Basic
    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "evidence_type")
    public Object getEvidenceType() {
        return evidenceType;
    }

    public void setEvidenceType(Object evidenceType) {
        this.evidenceType = evidenceType;
    }

    @Basic
    @Column(name = "evidence_name")
    public String getEvidenceName() {
        return evidenceName;
    }

    public void setEvidenceName(String evidenceName) {
        this.evidenceName = evidenceName;
    }

    @Basic
    @Column(name = "file_path")
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Basic
    @Column(name = "file_size")
    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    @Basic
    @Column(name = "file_hash")
    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    @Basic
    @Column(name = "evidence_blockchain_id")
    public String getEvidenceBlockchainId() {
        return evidenceBlockchainId;
    }

    public void setEvidenceBlockchainId(String evidenceBlockchainId) {
        this.evidenceBlockchainId = evidenceBlockchainId;
    }

    @Basic
    @Column(name = "blockchain_time")
    public Timestamp getBlockchainTime() {
        return blockchainTime;
    }

    public void setBlockchainTime(Timestamp blockchainTime) {
        this.blockchainTime = blockchainTime;
    }

    @Basic
    @Column(name = "evidence_time")
    public Timestamp getEvidenceTime() {
        return evidenceTime;
    }

    public void setEvidenceTime(Timestamp evidenceTime) {
        this.evidenceTime = evidenceTime;
    }

    @Basic
    @Column(name = "organization_id")
    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    @Basic
    @Column(name = "notary_id")
    public Integer getNotaryId() {
        return notaryId;
    }

    public void setNotaryId(Integer notaryId) {
        this.notaryId = notaryId;
    }

    @Basic
    @Column(name = "notarization_status")
    public Object getNotarizationStatus() {
        return notarizationStatus;
    }

    public void setNotarizationStatus(Object notarizationStatus) {
        this.notarizationStatus = notarizationStatus;
    }

    @Basic
    @Column(name = "transaction_id")
    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    @Basic
    @Column(name = "notarization_start_time")
    public Timestamp getNotarizationStartTime() {
        return notarizationStartTime;
    }

    public void setNotarizationStartTime(Timestamp notarizationStartTime) {
        this.notarizationStartTime = notarizationStartTime;
    }

    @Basic
    @Column(name = "notarization_blockchain_id")
    public String getNotarizationBlockchainId() {
        return notarizationBlockchainId;
    }

    public void setNotarizationBlockchainId(String notarizationBlockchainId) {
        this.notarizationBlockchainId = notarizationBlockchainId;
    }

    @Basic
    @Column(name = "notarization_money")
    public Integer getNotarizationMoney() {
        return notarizationMoney;
    }

    public void setNotarizationMoney(Integer notarizationMoney) {
        this.notarizationMoney = notarizationMoney;
    }

    @Basic
    @Column(name = "notarization_type")
    public Object getNotarizationType() {
        return notarizationType;
    }

    public void setNotarizationType(Object notarizationType) {
        this.notarizationType = notarizationType;
    }

    @Basic
    @Column(name = "notarization_information")
    public String getNotarizationInformation() {
        return notarizationInformation;
    }

    public void setNotarizationInformation(String notarizationInformation) {
        this.notarizationInformation = notarizationInformation;
    }

    @Basic
    @Column(name = "notarization_end_time")
    public Timestamp getNotarizationEndTime() {
        return notarizationEndTime;
    }

    public void setNotarizationEndTime(Timestamp notarizationEndTime) {
        this.notarizationEndTime = notarizationEndTime;
    }

    @Basic
    @Column(name = "notarization_matters")
    public String getNotarizationMatters() {
        return notarizationMatters;
    }

    public void setNotarizationMatters(String notarizationMatters) {
        this.notarizationMatters = notarizationMatters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvidenceEntity that = (EvidenceEntity) o;
        return evidenceId == that.evidenceId && Objects.equals(userId, that.userId) && Objects.equals(evidenceType, that.evidenceType) && Objects.equals(evidenceName, that.evidenceName) && Objects.equals(filePath, that.filePath) && Objects.equals(fileSize, that.fileSize) && Objects.equals(fileHash, that.fileHash) && Objects.equals(evidenceBlockchainId, that.evidenceBlockchainId) && Objects.equals(blockchainTime, that.blockchainTime) && Objects.equals(evidenceTime, that.evidenceTime) && Objects.equals(organizationId, that.organizationId) && Objects.equals(notaryId, that.notaryId) && Objects.equals(notarizationStatus, that.notarizationStatus) && Objects.equals(transactionId, that.transactionId) && Objects.equals(notarizationStartTime, that.notarizationStartTime) && Objects.equals(notarizationBlockchainId, that.notarizationBlockchainId) && Objects.equals(notarizationMoney, that.notarizationMoney) && Objects.equals(notarizationType, that.notarizationType) && Objects.equals(notarizationInformation, that.notarizationInformation) && Objects.equals(notarizationEndTime, that.notarizationEndTime) && Objects.equals(notarizationMatters, that.notarizationMatters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(evidenceId, userId, evidenceType, evidenceName, filePath, fileSize, fileHash, evidenceBlockchainId, blockchainTime, evidenceTime, organizationId, notaryId, notarizationStatus, transactionId, notarizationStartTime, notarizationBlockchainId, notarizationMoney, notarizationType, notarizationInformation, notarizationEndTime, notarizationMatters);
    }
}
