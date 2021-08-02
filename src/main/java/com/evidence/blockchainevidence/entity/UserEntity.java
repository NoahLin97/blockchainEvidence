package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user", schema = "blockchain_evidence", catalog = "")
public class UserEntity {
    private Object userId;
    private String username;
    private Integer remains;
    private Integer storageSpace;
    private Object userid;
    private Integer storagespace;

    @Id
    @Column(name = "userid")
    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
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
    @Column(name = "remains")
    public Integer getRemains() {
        return remains;
    }

    public void setRemains(Integer remains) {
        this.remains = remains;
    }

    @Basic
    @Column(name = "storagespace")
    public Integer getStorageSpace() {
        return storageSpace;
    }

    public void setStorageSpace(Integer storageSpace) {
        this.storageSpace = storageSpace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(username, that.username) &&
                Objects.equals(remains, that.remains) &&
                Objects.equals(storageSpace, that.storageSpace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, remains, storageSpace);
    }

    @Id
    @Column(name = "userid")
    public Object getUserid() {
        return userid;
    }

    public void setUserid(Object userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "storagespace")
    public Integer getStoragespace() {
        return storagespace;
    }

    public void setStoragespace(Integer storagespace) {
        this.storagespace = storagespace;
    }
}
