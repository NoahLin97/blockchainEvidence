package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "aut_manager", schema = "blockchain_evidence", catalog = "")
public class AutManagerEntity {
    private String autManId;
    private String autName;
    private String password;
    private String phoneNumber;
    private String idCard;
    private String email;
    private Object sex;
    private String organizationId;
    private OrganizationEntity organizationByOrganizationId;


    //我手动加的

    private String organizationName;

    @Basic
    @Column(name = "organizationName")
    public String getOrganizationName() {
        return organizationName;
    }
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }









    @Id
    @Column(name = "autManId")
    public String getAutManId() {
        return autManId;
    }

    public void setAutManId(String autManId) {
        this.autManId = autManId;
    }

    @Basic
    @Column(name = "autName")
    public String getAutName() {
        return autName;
    }

    public void setAutName(String autName) {
        this.autName = autName;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
    @Column(name = "idCard")
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
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
    @Column(name = "sex")
    public Object getSex() {
        return sex;
    }

    public void setSex(Object sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "organizationId")
    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutManagerEntity that = (AutManagerEntity) o;
        return Objects.equals(autManId, that.autManId) &&
                Objects.equals(autName, that.autName) &&
                Objects.equals(password, that.password) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(idCard, that.idCard) &&
                Objects.equals(email, that.email) &&
                Objects.equals(sex, that.sex) &&
                Objects.equals(organizationId, that.organizationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(autManId, autName, password, phoneNumber, idCard, email, sex, organizationId);
    }

    @ManyToOne
    @JoinColumn(name = "organizationId", referencedColumnName = "organizationId")
    public OrganizationEntity getOrganizationByOrganizationId() {
        return organizationByOrganizationId;
    }

    public void setOrganizationByOrganizationId(OrganizationEntity organizationByOrganizationId) {
        this.organizationByOrganizationId = organizationByOrganizationId;
    }
}
