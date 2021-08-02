package com.evidence.blockchainevidence.entity;

public class User {

    private int userId;
    private String username;
    private String password;
    private String phoneNumber;
    private String idCard;
    private String email;
    public enum Sex{
        MALE,
        FEMALE
    };
    private Sex sex;
    private int remains;
    private int storageSpace;
    private int hasUsedStorage;
    private String public_key;

    public User(){}

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRemains(int remains) {
        this.remains = remains;
    }

    public void setStorageSpace(int storageSpace) {
        this.storageSpace = storageSpace;
    }

    public void setHasUsedStorage(int hasUsedStorage) {
        this.hasUsedStorage = hasUsedStorage;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public int getUserId() {
        return userId;
    }

    public Sex getSex() {
        return sex;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getEmail() {
        return email;
    }

    public int getRemains() {
        return remains;
    }

    public int getStorageSpace() {
        return storageSpace;
    }

    public int getHasUsedStorage() {
        return hasUsedStorage;
    }

    public String getPublic_key() {
        return public_key;
    }
}
