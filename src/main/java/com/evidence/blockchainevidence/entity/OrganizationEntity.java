package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "organization", schema = "blockchain_evidence", catalog = "")
public class OrganizationEntity {
    private String organizationId;
    private String organizationName;
    private String address;
    private String phoneNumber;
    private String email;
    private String legalPeople;
    private Collection<AutManagerEntity> autManagersByOrganizationId;
    private Collection<EvidenceEntity> evidencesByOrganizationId;
    private Collection<NotaryEntity> notariesByOrganizationId;
    private Collection<OrganizationStatisticsEntity> organizationStatisticsByOrganizationId;

    @Id
    @Column(name = "organizationId")
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Basic
    @Column(name = "organizationName")
    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "legalPeople")
    public String getLegalPeople() {
        return legalPeople;
    }

    public void setLegalPeople(String legalPeople) {
        this.legalPeople = legalPeople;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationEntity that = (OrganizationEntity) o;
        return Objects.equals(organizationId, that.organizationId) &&
                Objects.equals(organizationName, that.organizationName) &&
                Objects.equals(address, that.address) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(email, that.email) &&
                Objects.equals(legalPeople, that.legalPeople);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationId, organizationName, address, phoneNumber, email, legalPeople);
    }

    @OneToMany(mappedBy = "organizationByOrganizationId")
    public Collection<AutManagerEntity> getAutManagersByOrganizationId() {
        return autManagersByOrganizationId;
    }

    public void setAutManagersByOrganizationId(Collection<AutManagerEntity> autManagersByOrganizationId) {
        this.autManagersByOrganizationId = autManagersByOrganizationId;
    }

    @OneToMany(mappedBy = "organizationByOrganizationId")
    public Collection<EvidenceEntity> getEvidencesByOrganizationId() {
        return evidencesByOrganizationId;
    }

    public void setEvidencesByOrganizationId(Collection<EvidenceEntity> evidencesByOrganizationId) {
        this.evidencesByOrganizationId = evidencesByOrganizationId;
    }

    @OneToMany(mappedBy = "organizationByOrganizationId")
    public Collection<NotaryEntity> getNotariesByOrganizationId() {
        return notariesByOrganizationId;
    }

    public void setNotariesByOrganizationId(Collection<NotaryEntity> notariesByOrganizationId) {
        this.notariesByOrganizationId = notariesByOrganizationId;
    }

    @OneToMany(mappedBy = "organizationByOrganizationId")
    public Collection<OrganizationStatisticsEntity> getOrganizationStatisticsByOrganizationId() {
        return organizationStatisticsByOrganizationId;
    }

    public void setOrganizationStatisticsByOrganizationId(Collection<OrganizationStatisticsEntity> organizationStatisticsByOrganizationId) {
        this.organizationStatisticsByOrganizationId = organizationStatisticsByOrganizationId;
    }
}
