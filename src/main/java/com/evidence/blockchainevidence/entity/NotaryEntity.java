package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "notary", schema = "blockchain_evidence", catalog = "")
public class NotaryEntity {
    private Object notaryId;
    private String notaryName;
    private int organizationId;
    private Object notarizationType;
    private Object notaryid;
    private String notaryname;
    private int organizationid;
    private Object notarizationtype;

    @Id
    @Column(name = "notary_id")
    public Object getNotaryId() {
        return notaryId;
    }

    public void setNotaryId(Object notaryId) {
        this.notaryId = notaryId;
    }

    @Basic
    @Column(name = "notary_name")
    public String getNotaryName() {
        return notaryName;
    }

    public void setNotaryName(String notaryName) {
        this.notaryName = notaryName;
    }

    @Basic
    @Column(name = "organization_id")
    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    @Basic
    @Column(name = "notarization_type")
    public Object getNotarizationType() {
        return notarizationType;
    }

    public void setNotarizationType(Object notarizationType) {
        this.notarizationType = notarizationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotaryEntity that = (NotaryEntity) o;
        return organizationId == that.organizationId &&
                Objects.equals(notaryId, that.notaryId) &&
                Objects.equals(notaryName, that.notaryName) &&
                Objects.equals(notarizationType, that.notarizationType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notaryId, notaryName, organizationId, notarizationType);
    }

    @Id
    @Column(name = "notaryid")
    public Object getNotaryid() {
        return notaryid;
    }

    public void setNotaryid(Object notaryid) {
        this.notaryid = notaryid;
    }

    @Basic
    @Column(name = "notaryname")
    public String getNotaryname() {
        return notaryname;
    }

    public void setNotaryname(String notaryname) {
        this.notaryname = notaryname;
    }

    @Basic
    @Column(name = "organizationid")
    public int getOrganizationid() {
        return organizationid;
    }

    public void setOrganizationid(int organizationid) {
        this.organizationid = organizationid;
    }

    @Basic
    @Column(name = "notarizationtype")
    public Object getNotarizationtype() {
        return notarizationtype;
    }

    public void setNotarizationtype(Object notarizationtype) {
        this.notarizationtype = notarizationtype;
    }
}
