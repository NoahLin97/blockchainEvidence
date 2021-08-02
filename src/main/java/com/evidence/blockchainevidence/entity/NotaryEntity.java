package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "notary", schema = "blockchain_evidence", catalog = "")
public class NotaryEntity {
    private int notaryId;
    private String notaryName;
    private String jobNumber;
    private String password;
    private String phoneNumber;
    private String idCard;
    private String email;
    private Object sex;
    private Integer organizationId;
    private Object notarizationType;

    @Id
    @Column(name = "notary_id")
    public int getNotaryId() {
        return notaryId;
    }

    public void setNotaryId(int notaryId) {
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
    @Column(name = "job_number")
    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
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
        return notaryId == that.notaryId && Objects.equals(notaryName, that.notaryName) && Objects.equals(jobNumber, that.jobNumber) && Objects.equals(password, that.password) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(idCard, that.idCard) && Objects.equals(email, that.email) && Objects.equals(sex, that.sex) && Objects.equals(organizationId, that.organizationId) && Objects.equals(notarizationType, that.notarizationType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notaryId, notaryName, jobNumber, password, phoneNumber, idCard, email, sex, organizationId, notarizationType);
    }
}
