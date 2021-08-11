package com.evidence.blockchainevidence.service;


import com.evidence.blockchainevidence.entity.EvidenceEntity;
import com.evidence.blockchainevidence.mapper.EvidenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EvidenceService {

    @Autowired
    EvidenceMapper evidenceMapper;

    public EvidenceEntity selectByEvidenceId(String evidenceId){
        return evidenceMapper.selectByEvidenceId(evidenceId);
    }

    public int updateOrganIdAndNotarType(String organizationId,String notarizationType,String evidenceId){
        return evidenceMapper.updateOrganIdAndNotarType(organizationId,notarizationType,evidenceId);
    }

    public int updateNotarStatus(String notarizationStatus,String evidenceId){
        return evidenceMapper.updateNotarStatus(notarizationStatus,evidenceId);
    }

    public int updateNotarMatters(String notarizationMatters,String evidenceId){
        return evidenceMapper.updateNotarMatters(notarizationMatters,evidenceId);
    }

    public int updateNotarStartTime(String notarizationStartTime,String evidneceId){
        return evidenceMapper.updateNotarStartTime(notarizationStartTime,evidneceId);
    }

    public int updateTranId(String transactionId,String evidenceId){
        return evidenceMapper.updateTranId(transactionId,evidenceId);
    }

    public int updateTranStatus(String transactionStatus,String evidenceId){
        return evidenceMapper.updateTranStatus(transactionStatus,evidenceId);
    }

    public int updateNotarMoney(String notarizationMoney,String evidenceId){
        return evidenceMapper.updateNotarMoney(notarizationMoney,evidenceId);
    }


}
