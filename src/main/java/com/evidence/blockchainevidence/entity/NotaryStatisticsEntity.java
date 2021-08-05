package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "notary_statistics", schema = "blockchain_evidence", catalog = "")
public class NotaryStatisticsEntity {
    private String notaryStatisticsId;
    private String notstyId;
    private String notaryName;
    private String organizationName;
    private Integer notarizationCount;
    private String notarizationTotalMoney;
    private Integer notarizationSuccessCount;
    private Object notarizationType;
    private Timestamp timeFlag;
    private NotaryEntity notaryByNotstyId;











    @Id
    @Column(name = "notaryStatisticsId")
    public String getNotaryStatisticsId() {
        return notaryStatisticsId;
    }

    public void setNotaryStatisticsId(String notaryStatisticsId) {
        this.notaryStatisticsId = notaryStatisticsId;
    }

    @Basic
    @Column(name = "notstyId")
    public String getNotstyId() {
        return notstyId;
    }

    public void setNotstyId(String notstyId) {
        this.notstyId = notstyId;
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
    @Column(name = "organizationName")
    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Basic
    @Column(name = "notarizationCount")
    public Integer getNotarizationCount() {
        return notarizationCount;
    }

    public void setNotarizationCount(Integer notarizationCount) {
        this.notarizationCount = notarizationCount;
    }

    @Basic
    @Column(name = "notarizationTotalMoney")
    public String getNotarizationTotalMoney() {
        return notarizationTotalMoney;
    }

    public void setNotarizationTotalMoney(String notarizationTotalMoney) {
        this.notarizationTotalMoney = notarizationTotalMoney;
    }

    @Basic
    @Column(name = "notarizationSuccessCount")
    public Integer getNotarizationSuccessCount() {
        return notarizationSuccessCount;
    }

    public void setNotarizationSuccessCount(Integer notarizationSuccessCount) {
        this.notarizationSuccessCount = notarizationSuccessCount;
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
    @Column(name = "timeFlag")
    public Timestamp getTimeFlag() {
        return timeFlag;
    }

    public void setTimeFlag(Timestamp timeFlag) {
        this.timeFlag = timeFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotaryStatisticsEntity that = (NotaryStatisticsEntity) o;
        return Objects.equals(notaryStatisticsId, that.notaryStatisticsId) &&
                Objects.equals(notstyId, that.notstyId) &&
                Objects.equals(notaryName, that.notaryName) &&
                Objects.equals(organizationName, that.organizationName) &&
                Objects.equals(notarizationCount, that.notarizationCount) &&
                Objects.equals(notarizationTotalMoney, that.notarizationTotalMoney) &&
                Objects.equals(notarizationSuccessCount, that.notarizationSuccessCount) &&
                Objects.equals(notarizationType, that.notarizationType) &&
                Objects.equals(timeFlag, that.timeFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notaryStatisticsId, notstyId, notaryName, organizationName, notarizationCount, notarizationTotalMoney, notarizationSuccessCount, notarizationType, timeFlag);
    }

    @ManyToOne
    @JoinColumn(name = "notstyId", referencedColumnName = "notaryId")
    public NotaryEntity getNotaryByNotstyId() {
        return notaryByNotstyId;
    }

    public void setNotaryByNotstyId(NotaryEntity notaryByNotstyId) {
        this.notaryByNotstyId = notaryByNotstyId;
    }
}
