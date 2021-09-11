package com.evidence.blockchainevidence.entity;


import javax.persistence.*;

@Entity
@Table(name = "notarization_type", schema = "blockchain_evidence", catalog = "")
public class NotarizationTypeEntity {

    private String notarizationTypeId;
    private String autManId;
    private String notarizationType;
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
    @Column(name = "notarizationType")
    public String getNotarizationType() {
        return notarizationType;
    }

    public void setNotarizationType(String notarizationType) { this.notarizationType = notarizationType; }

    @Basic
    @Column(name = "autManId")
    public String getAutManId() {
        return autManId;
    }

    public void setAutManId(String autManId) { this.autManId = autManId; }


    @Basic
    @Column(name = "notarizationMoney")
    public String getNotarizationMoney() {
        return notarizationMoney;
    }

    public void setNotarizationMoney(String notarizationMoney) { this.notarizationMoney = notarizationMoney; }




}
