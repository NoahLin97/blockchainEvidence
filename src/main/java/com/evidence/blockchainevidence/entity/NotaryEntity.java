package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "notary", schema = "blockchain_evidence", catalog = "")
public class NotaryEntity {
    private String notaryId;
    private String notaryName;
    private String jobNumber;
    private String password;
    private String phoneNumber;
    private String idCard;
    private String email;
    private Object sex;
    private String organizationId;
    private Object notarizationType;
    private Collection<EvidenceEntity> evidencesByNotaryId;
    private OrganizationEntity organizationByOrganizationId;
    private Collection<NotaryStatisticsEntity> notaryStatisticsByNotaryId;
    private Collection<RankEntity> ranksByNotaryId;

    private String workYear;
    private String position;


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
    @Column(name = "notaryId")
    public String getNotaryId() {
        return notaryId;
    }

    public void setNotaryId(String notaryId) {
        this.notaryId = notaryId;
    }

    @Basic
    @Column(name = "notaryName")
    public String getNotaryName() {
        return notaryName;
    }

    public void setNotaryName(String notaryName) {
        this.notaryName = notaryName;
    }

    @Basic
    @Column(name = "jobNumber")
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

    @Basic
    @Column(name = "notarizationType")
    public Object getNotarizationType() {
        return notarizationType;
    }

    public void setNotarizationType(Object notarizationType) {
        this.notarizationType = notarizationType;
    }

    @Basic
    @Column(name = "workYear")
    public Object getWorkYear() {
        return workYear;
    }

    public void setWorkYear(String workYear) {
        this.workYear = workYear;
    }

    @Basic
    @Column(name = "position")
    public Object getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotaryEntity that = (NotaryEntity) o;
        return Objects.equals(notaryId, that.notaryId) &&
                Objects.equals(notaryName, that.notaryName) &&
                Objects.equals(jobNumber, that.jobNumber) &&
                Objects.equals(password, that.password) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(idCard, that.idCard) &&
                Objects.equals(email, that.email) &&
                Objects.equals(sex, that.sex) &&
                Objects.equals(organizationId, that.organizationId) &&
                Objects.equals(notarizationType, that.notarizationType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notaryId, notaryName, jobNumber, password, phoneNumber, idCard, email, sex, organizationId, notarizationType);
    }

    @OneToMany(mappedBy = "notaryByNotaryId")
    public Collection<EvidenceEntity> getEvidencesByNotaryId() {
        return evidencesByNotaryId;
    }

    public void setEvidencesByNotaryId(Collection<EvidenceEntity> evidencesByNotaryId) {
        this.evidencesByNotaryId = evidencesByNotaryId;
    }

    @ManyToOne
    @JoinColumn(name = "organizationId", referencedColumnName = "organizationId")
    public OrganizationEntity getOrganizationByOrganizationId() {
        return organizationByOrganizationId;
    }

    public void setOrganizationByOrganizationId(OrganizationEntity organizationByOrganizationId) {
        this.organizationByOrganizationId = organizationByOrganizationId;
    }

    @OneToMany(mappedBy = "notaryByNotstyId")
    public Collection<NotaryStatisticsEntity> getNotaryStatisticsByNotaryId() {
        return notaryStatisticsByNotaryId;
    }

    public void setNotaryStatisticsByNotaryId(Collection<NotaryStatisticsEntity> notaryStatisticsByNotaryId) {
        this.notaryStatisticsByNotaryId = notaryStatisticsByNotaryId;
    }

    @OneToMany(mappedBy = "notaryByNotstyId")
    public Collection<RankEntity> getRanksByNotaryId() {
        return ranksByNotaryId;
    }

    public void setRanksByNotaryId(Collection<RankEntity> ranksByNotaryId) {
        this.ranksByNotaryId = ranksByNotaryId;
    }
}
