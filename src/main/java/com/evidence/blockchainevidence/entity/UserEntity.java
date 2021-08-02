package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user", schema = "blockchain_evidence", catalog = "")
public class UserEntity {
    private int userId;
    private String username;
    private String password;
    private String phoneNumber;
    private String idCard;
    private String email;
    private Object sex;
    private String remains;
    private String storageSpace;
    private String publicKey;
    private Integer hasUsedStorage;

    @Id
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
    @Column(name = "remains")
    public String getRemains() {
        return remains;
    }

    public void setRemains(String remains) {
        this.remains = remains;
    }


    @Basic
    @Column(name = "storage_space")
    public String getStorageSpace() {
        return storageSpace;
    }

    public void setStorageSpace(String storageSpace) {
        this.storageSpace = storageSpace;
    }


    @Basic
    @Column(name = "public_key")
    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Basic
    @Column(name = "has_used_storage")
    public Integer getHasUsedStorage() {
        return hasUsedStorage;
    }

    public void setHasUsedStorage(Integer hasUsedStorage) {
        this.hasUsedStorage = hasUsedStorage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return userId == that.userId && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(idCard, that.idCard) && Objects.equals(email, that.email) && Objects.equals(sex, that.sex) && Objects.equals(remains, that.remains) && Objects.equals(storageSpace, that.storageSpace) && Objects.equals(publicKey, that.publicKey) && Objects.equals(hasUsedStorage, that.hasUsedStorage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, phoneNumber, idCard, email, sex, remains, storageSpace, publicKey, hasUsedStorage);
    }
}
