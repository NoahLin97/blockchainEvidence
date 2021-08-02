package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "organization", schema = "blockchain_evidence", catalog = "")
public class OrganizationEntity {
    private int organizationId;
    private String organizationName;
    private String address;
    private String phoneNumber;
    private String email;
    private String legalPeople;

    @Id
    @Column(name = "organization_id")
    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    @Basic
    @Column(name = "organization_name")
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
    @Column(name = "phone_number")
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
    @Column(name = "legal_people")
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
        return organizationId == that.organizationId && Objects.equals(organizationName, that.organizationName) && Objects.equals(address, that.address) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(email, that.email) && Objects.equals(legalPeople, that.legalPeople);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationId, organizationName, address, phoneNumber, email, legalPeople);
    }
}
