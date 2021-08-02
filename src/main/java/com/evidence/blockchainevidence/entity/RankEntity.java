package com.evidence.blockchainevidence.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "rank", schema = "blockchain_evidence", catalog = "")
public class RankEntity {
    private int rankId;
    private Integer notstyId;
    private String notaryName;
    private Integer notarizationCount;
    private Integer notaryRank;
    private Timestamp timeFlag;

    @Id
    @Column(name = "rank_id")
    public int getRankId() {
        return rankId;
    }

    public void setRankId(int rankId) {
        this.rankId = rankId;
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
    @Column(name = "notarization_count")
    public Integer getNotarizationCount() {
        return notarizationCount;
    }

    public void setNotarizationCount(Integer notarizationCount) {
        this.notarizationCount = notarizationCount;
    }

    @Basic
    @Column(name = "notary_rank")
    public Integer getNotaryRank() {
        return notaryRank;
    }

    public void setNotaryRank(Integer notaryRank) {
        this.notaryRank = notaryRank;
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
        RankEntity that = (RankEntity) o;
        return rankId == that.rankId && Objects.equals(notstyId, that.notstyId) && Objects.equals(notaryName, that.notaryName) && Objects.equals(notarizationCount, that.notarizationCount) && Objects.equals(notaryRank, that.notaryRank) && Objects.equals(timeFlag, that.timeFlag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rankId, notstyId, notaryName, notarizationCount, notaryRank, timeFlag);
    }
}
