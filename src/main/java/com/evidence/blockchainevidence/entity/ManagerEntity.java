package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "manager", schema = "blockchain_evidence", catalog = "")
public class ManagerEntity {
    private String manId;
    private String username;
    private String password;
    private String phoneNumber;
    private String idCard;
    private String email;
    private Object sex;

    @Id
    @Column(name = "manId")
    public String getManId() {
        return manId;
    }

    public void setManId(String manId) {
        this.manId = manId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManagerEntity that = (ManagerEntity) o;
        return Objects.equals(manId, that.manId) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(idCard, that.idCard) &&
                Objects.equals(email, that.email) &&
                Objects.equals(sex, that.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manId, username, password, phoneNumber, idCard, email, sex);
    }
}
