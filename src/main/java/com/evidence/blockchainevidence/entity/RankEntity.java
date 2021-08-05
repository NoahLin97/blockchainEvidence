package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "rank", schema = "blockchain_evidence", catalog = "")
public class RankEntity {
    private String rankId;
    private String notstyId;
    private String notaryName;
    private Integer notarizationCount;
    private Integer notaryRank;
    private Timestamp timeFlag;
    private NotaryEntity notaryByNotstyId;

    @Id
    @Column(name = "rankId")
    public String getRankId() {
        return rankId;
    }

    public void setRankId(String rankId) {
        this.rankId = rankId;
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
    @Column(name = "notarizationCount")
    public Integer getNotarizationCount() {
        return notarizationCount;
    }

    public void setNotarizationCount(Integer notarizationCount) {
        this.notarizationCount = notarizationCount;
    }

    @Basic
    @Column(name = "notaryRank")
    public Integer getNotaryRank() {
        return notaryRank;
    }

    public void setNotaryRank(Integer notaryRank) {
        this.notaryRank = notaryRank;
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
        RankEntity that = (RankEntity) o;
        return Objects.equals(rankId, that.rankId) &&
                Objects.equals(notstyId, that.notstyId) &&
                Objects.equals(notaryName, that.notaryName) &&
                Objects.equals(notarizationCount, that.notarizationCount) &&
                Objects.equals(notaryRank, that.notaryRank) &&
                Objects.equals(timeFlag, that.timeFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rankId, notstyId, notaryName, notarizationCount, notaryRank, timeFlag);
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
