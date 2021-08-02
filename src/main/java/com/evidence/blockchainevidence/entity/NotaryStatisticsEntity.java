package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "notary_statistics", schema = "blockchain_evidence", catalog = "")
public class NotaryStatisticsEntity {
    private int notaryStatisticsId;
    private Integer notstyId;
    private String notaryName;
    private String organizationName;
    private Integer notarizationCount;
    private Integer notarizationTotalMoney;
    private Double notarizationSuccessRate;
    private Object notarizationType;
    private Timestamp timeFlag;

    @Id
    @Column(name = "notary_statistics_id")
    public int getNotaryStatisticsId() {
        return notaryStatisticsId;
    }

    public void setNotaryStatisticsId(int notaryStatisticsId) {
        this.notaryStatisticsId = notaryStatisticsId;
    }

    @Basic
    @Column(name = "notsty_id")
    public Integer getNotstyId() {
        return notstyId;
    }

    public void setNotstyId(Integer notstyId) {
        this.notstyId = notstyId;
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
    @Column(name = "organization_name")
    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Basic
    @Column(name = "notarization_count")
    public Integer getNotarizationCount() {
        return notarizationCount;
    }

    public void setNotarizationCount(Integer notarizationCount) {
        this.notarizationCount = notarizationCount;
    }

    @Basic
    @Column(name = "notarization_total_money")
    public Integer getNotarizationTotalMoney() {
        return notarizationTotalMoney;
    }

    public void setNotarizationTotalMoney(Integer notarizationTotalMoney) {
        this.notarizationTotalMoney = notarizationTotalMoney;
    }

    @Basic
    @Column(name = "notarization_success_rate")
    public Double getNotarizationSuccessRate() {
        return notarizationSuccessRate;
    }

    public void setNotarizationSuccessRate(Double notarizationSuccessRate) {
        this.notarizationSuccessRate = notarizationSuccessRate;
    }

    @Basic
    @Column(name = "notarization_type")
    public Object getNotarizationType() {
        return notarizationType;
    }

    public void setNotarizationType(Object notarizationType) {
        this.notarizationType = notarizationType;
    }

    @Basic
    @Column(name = "time_flag")
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
        return notaryStatisticsId == that.notaryStatisticsId && Objects.equals(notstyId, that.notstyId) && Objects.equals(notaryName, that.notaryName) && Objects.equals(organizationName, that.organizationName) && Objects.equals(notarizationCount, that.notarizationCount) && Objects.equals(notarizationTotalMoney, that.notarizationTotalMoney) && Objects.equals(notarizationSuccessRate, that.notarizationSuccessRate) && Objects.equals(notarizationType, that.notarizationType) && Objects.equals(timeFlag, that.timeFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notaryStatisticsId, notstyId, notaryName, organizationName, notarizationCount, notarizationTotalMoney, notarizationSuccessRate, notarizationType, timeFlag);
    }
}
