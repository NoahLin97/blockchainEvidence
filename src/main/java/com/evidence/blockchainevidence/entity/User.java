package com.evidence.blockchainevidence.entity;

public class User {

    private String id;
    private String username;
    private String passWord;
    private String phoneNumber;
    private String idCard;
    private String eMail;
    private String sex;
    private String remains;
    private String storageSpace;
    private String hasUsedStorage;



    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setRemains(String remains) {
        this.remains = remains;
    }

    public void setStorageSpace(String storageSpace) {
        this.storageSpace = storageSpace;
    }

    public void setHasUsedStorage(String hasUsedStorage) {
        this.hasUsedStorage = hasUsedStorage;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return username;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getIdCard() {
        return idCard;
    }

    public String geteMail() {
        return eMail;
    }

    public String getSex() {
        return sex;
    }

    public String getRemains() {
        return remains;
    }

    public String getStorageSpace() {
        return storageSpace;
    }

    public String getHasUsedStorage() {
        return hasUsedStorage;
    }


}
