package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "aut_manager", schema = "blockchain_evidence", catalog = "")
public class AutManagerEntity {
    private int autManId;
    private String username;
    private String password;
    private String phoneNumber;
    private String idCard;
    private String email;
    private Object sex;
    private Integer organizationId;

    @Id
    @Column(name = "aut_man_id")
    public int getAutManId() {
        return autManId;
    }

    public void setAutManId(int autManId) {
        this.autManId = autManId;
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
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
    @Column(name = "id_card")
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
    @Column(name = "organization_id")
    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutManagerEntity that = (AutManagerEntity) o;
        return autManId == that.autManId && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(idCard, that.idCard) && Objects.equals(email, that.email) && Objects.equals(sex, that.sex) && Objects.equals(organizationId, that.organizationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(autManId, username, password, phoneNumber, idCard, email, sex, organizationId);
    }
}
