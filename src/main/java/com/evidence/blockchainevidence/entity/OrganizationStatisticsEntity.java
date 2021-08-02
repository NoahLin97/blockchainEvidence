package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "organization_statistics", schema = "blockchain_evidence", catalog = "")
public class OrganizationStatisticsEntity {
    private int organizationStatisticsId;
    private Integer organizationId;
    private String organizationName;
    private Integer notarizationCount;
    private Integer notarizationTotalMoney;
    private Double notarizationSuccessRate;
    private Integer notstyCount;
    private Timestamp timeFlag;

    @Id
    @Column(name = "organization_statistics_id")
    public int getOrganizationStatisticsId() {
        return organizationStatisticsId;
    }

    public void setOrganizationStatisticsId(int organizationStatisticsId) {
        this.organizationStatisticsId = organizationStatisticsId;
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
    @Column(name = "notsty_count")
    public Integer getNotstyCount() {
        return notstyCount;
    }

    public void setNotstyCount(Integer notstyCount) {
        this.notstyCount = notstyCount;
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
        OrganizationStatisticsEntity that = (OrganizationStatisticsEntity) o;
        return organizationStatisticsId == that.organizationStatisticsId && Objects.equals(organizationId, that.organizationId) && Objects.equals(organizationName, that.organizationName) && Objects.equals(notarizationCount, that.notarizationCount) && Objects.equals(notarizationTotalMoney, that.notarizationTotalMoney) && Objects.equals(notarizationSuccessRate, that.notarizationSuccessRate) && Objects.equals(notstyCount, that.notstyCount) && Objects.equals(timeFlag, that.timeFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationStatisticsId, organizationId, organizationName, notarizationCount, notarizationTotalMoney, notarizationSuccessRate, notstyCount, timeFlag);
    }
}
