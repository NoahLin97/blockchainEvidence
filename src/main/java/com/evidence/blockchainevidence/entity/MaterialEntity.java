package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "material", schema = "blockchain_evidence", catalog = "")
public class MaterialEntity {
    private String materialId;
    private String notarizationType;
    private String organizationId;
    private String filePath;
    private Timestamp uploadTime;

    @Id
    @Column(name = "materialId")
    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
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
    @Column(name = "organizationId")
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
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
    @Column(name = "uploadTime")
    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialEntity that = (MaterialEntity) o;
        return Objects.equals(materialId, that.materialId) &&
                Objects.equals(notarizationType, that.notarizationType) &&
                Objects.equals(organizationId, that.organizationId) &&
                Objects.equals(filePath, that.filePath) &&
                Objects.equals(uploadTime, that.uploadTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialId, notarizationType, organizationId, filePath, uploadTime);
    }
}
