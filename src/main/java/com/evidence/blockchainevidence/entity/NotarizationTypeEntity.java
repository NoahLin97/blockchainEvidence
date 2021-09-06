package com.evidence.blockchainevidence.entity;


import javax.persistence.*;

@Entity
@Table(name = "notarization_type", schema = "blockchain_evidence", catalog = "")
public class NotarizationTypeEntity {

    private String notarizationTypeId;
    private String notarizationTypeName;
    private String notarizationMoney;


    @Id
    @Column(name = "notarizationTypeId")
    public String getNotarizationTypeId() {
        return notarizationTypeId;
    }

    public void setNotarizationTypeId(String notarizationTypeId) {
        this.notarizationTypeId = notarizationTypeId;
    }


    @Basic
    @Column(name = "notarizationTypeName")
    public String getNotarizationTypeName() {
        return notarizationTypeName;
    }

    public void setNotarizationTypeName(String notarizationTypeName) { this.notarizationTypeName = notarizationTypeName; }


    @Basic
    @Column(name = "notarizationMoney")
    public String getNotarizationMoney() {
        return notarizationMoney;
    }

    public void setNotarizationMoney(String notarizationMoney) { this.notarizationMoney = notarizationMoney; }




}
