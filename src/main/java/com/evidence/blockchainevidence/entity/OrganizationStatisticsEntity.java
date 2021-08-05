package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "organization_statistics", schema = "blockchain_evidence", catalog = "")
public class OrganizationStatisticsEntity {
    private String organizationStatisticsId;
    private String organizationId;
    private String organizationName;
    private Integer notarizationCount;
    private String notarizationTotalMoney;
    private Integer notarizationSuccessCount;
    private Integer notstyCount;
    private Timestamp timeFlag;
    private OrganizationEntity organizationByOrganizationId;

    @Id
    @Column(name = "organizationStatisticsId")
    public String getOrganizationStatisticsId() {
        return organizationStatisticsId;
    }

    public void setOrganizationStatisticsId(String organizationStatisticsId) {
        this.organizationStatisticsId = organizationStatisticsId;
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
    @Column(name = "notstyCount")
    public Integer getNotstyCount() {
        return notstyCount;
    }

    public void setNotstyCount(Integer notstyCount) {
        this.notstyCount = notstyCount;
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
        OrganizationStatisticsEntity that = (OrganizationStatisticsEntity) o;
        return Objects.equals(organizationStatisticsId, that.organizationStatisticsId) &&
                Objects.equals(organizationId, that.organizationId) &&
                Objects.equals(organizationName, that.organizationName) &&
                Objects.equals(notarizationCount, that.notarizationCount) &&
                Objects.equals(notarizationTotalMoney, that.notarizationTotalMoney) &&
                Objects.equals(notarizationSuccessCount, that.notarizationSuccessCount) &&
                Objects.equals(notstyCount, that.notstyCount) &&
                Objects.equals(timeFlag, that.timeFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizationStatisticsId, organizationId, organizationName, notarizationCount, notarizationTotalMoney, notarizationSuccessCount, notstyCount, timeFlag);
    }

    @ManyToOne
    @JoinColumn(name = "organizationId", referencedColumnName = "organizationId")
    public OrganizationEntity getOrganizationByOrganizationId() {
        return organizationByOrganizationId;
    }

    public void setOrganizationByOrganizationId(OrganizationEntity organizationByOrganizationId) {
        this.organizationByOrganizationId = organizationByOrganizationId;
    }
}
